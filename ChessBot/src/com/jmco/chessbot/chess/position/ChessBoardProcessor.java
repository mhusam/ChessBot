package com.jmco.chessbot.chess.position;

import com.jmco.chessbot.chess.commands.Stockfish;
import com.jmco.recorder.DesktopScreenRecorder;
import com.jmco.utils.ImageUtils;
import com.jmco.utils.OSListenerBuilder;
import com.jmco.utils.UIUtils;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mhusam [m-husam@hotmail.com]
 * @since 1.0.0
 * @datetime Jun 30, 2017 2:05:27 PM
 */
public class ChessBoardProcessor {

    private final List<BufferedImage> capturedImages = new ArrayList<>();
    private final List<MovePosition> moves = new ArrayList<>();
    private CaptureProcessorListener captureProcessorLlistener;
    private List<SquarePosition> positions;
    private Stockfish engine;
    private ChessMoveStopListener stopListener;

    public ChessBoardProcessor() {
        this(null);
    }

    public ChessBoardProcessor(CaptureProcessorListener captureProcessorLlistener) {
        this.captureProcessorLlistener = captureProcessorLlistener;
        startEngine();
        initMoveStopListener();
    }

    private void initMoveStopListener() {
        stopListener = new ChessMoveStopListener(this);
        OSListenerBuilder.getInstance().addKeyboardListener(stopListener);
    }

    private void startEngine() {
        engine = new Stockfish();
        engine.startEngine();
    }

    private void wait(int thr) {
        try {
            Thread.sleep(thr);
        } catch (InterruptedException e) {

        }
    }

    private boolean addMove(MovePosition move) {
        if (move != null && move.from != null && move.to != null) {
            if (moves.size() > 0) {
                MovePosition lastMove = moves.get(moves.size() - 1);
                if (Square.isKingWhiteLeftMove(lastMove) && Square.isEquals(move, Square.A1, Square.D1)) {
                    return false;
                }
                if (Square.isKingWhiteRightMove(lastMove) && Square.isEquals(move, Square.H1, Square.F1)) {
                    return false;
                }
                if (Square.isKingBlackLeftMove(lastMove) && Square.isEquals(move, Square.A8, Square.D8)) {
                    return false;
                }
                if (Square.isKingBlackRightMove(lastMove) && Square.isEquals(move, Square.H8, Square.F8)) {
                    return false;
                }
                if (!move.equals(lastMove)) {
                    moves.add(move);
                    return true;
                }
            } else {
                moves.add(move);
                return true;
            }
        }
        return false;
    }

    private boolean validateImageMove(BufferedImage image, BufferedImage chessBoardImage) {
        if (!ChessBoardUtils.isMovingStopped(chessBoardImage)) {
            return false;
        }

        if (moves.size() % 2 != 0) {
            if (ChessBoardUtils.isEqualsImages(image, chessBoardImage)) {
                return false;
            }
            if (ChessBoardUtils.isEqualsPositions(image, chessBoardImage, positions)) {
                return false;
            }
            if (ChessBoardUtils.isOneDiff(image, chessBoardImage, positions)) {
                return false;
            }
        }
        return true;
    }

    public void doChessStuff(BufferedImage chessBoardImage, BufferedImage after200) {
        int imagesSize = capturedImages.size();

        if (imagesSize == 0) {
            capturedImages.add(chessBoardImage);
            positions = ChessBoardUtils.findSquarePositionsConvert(chessBoardImage);
            doFirstMove();
            return;
        }

        if (!validateImageMove(capturedImages.get(imagesSize - 1), chessBoardImage)) {
            wait(100);
            return;
        }

        try {
            int movesSize = moves.size();

            if (movesSize % 2 != 0) {
                if (!ChessBoardUtils.isEqualsImages(after200, chessBoardImage)) {
                    return;
                }

                capturedImages.add(chessBoardImage);
                imagesSize = capturedImages.size();

                BufferedImage image1 = capturedImages.get(imagesSize - 2);
                BufferedImage image2 = capturedImages.get(imagesSize - 1);

                MovePosition move = null;
                if (movesSize > 0) {
                    move = ChessBoardUtils.findMove(image1, image2, moves.get(movesSize - 1), positions);
                } else {
                    move = ChessBoardUtils.findMove(image1, image2, positions);
                }
                if (move != null && move.from != null && move.to != null) {
                    if (addMove(move)) {
                        if (captureProcessorLlistener != null) {
                            captureProcessorLlistener.onMove(move);
                        }
                    }
                } else {
                    capturedImages.remove(capturedImages.size() - 1);
                }
            } else {
                robotMove();
            }
        } catch (Exception e) {
            UIUtils.popupError(e, "ChessBoardProcessor::doChessStuff");
        }
    }

    private void robotMove() {
        try {
            synchronized (this) {
                if (stopListener.isStartWait()) {
                    this.wait();
                }
                doMove();
                wait(100);
            }
        } catch (Exception e) {
            UIUtils.popupError(e, "ChessBoardProcessor::doChessStuff");
        }
    }

    private void doFirstMove() {
        engine.sendCommand("ucinewgame");

        String bestMove = engine.getBestMove("", 0);

        MovePosition move = getMoveFromString(bestMove);

        if (addMove(move)) {
            moveMouse(bestMove);
        }
    }

    private String getBestMove(boolean pander) {
        MovePosition move = moves.get(moves.size() - 1);
        String lastStep = move.getStringPosition().toLowerCase();
        String steps = getStepsFromMoves(lastStep);
        return engine.getBestMove(steps, 0, pander);
    }

    private void doMove() {
        String bestMove = getBestMove(false);

        if (bestMove.contains("none")) {
            if (captureProcessorLlistener != null) {
                captureProcessorLlistener.doStop();
            }
            return;
        }

        MovePosition move = getMoveFromString(bestMove);

        if (bestMove.contains("q")) {
            move.setReplaced("q");
        }

        if (move.equals(moves.get(moves.size() - 1))) {
            MovePosition lastMove = moves.get(moves.size() - 1);
            lastMove.setReplaced("q");

            bestMove = getBestMove(false);
            move = getMoveFromString(bestMove);
            if (bestMove.contains("q")) {
                move.setReplaced("q");
            }
        }

        if (addMove(move)) {
            moveMouse(bestMove);
        }
    }

    private String getStepsFromMoves(String lastStep) {
        if (moves.size() == 1) {
            return lastStep;
        }
        StringBuilder steps = new StringBuilder();
        for (MovePosition step : moves) {
            steps.append(step.getStringPosition().toLowerCase());
            steps.append(" ");
        }

        return steps.toString();
    }

    private void moveMouse(String bestMove) {
        try {
            SquarePosition from = getSquarePosition(bestMove.subSequence(0, 2).toString());
            SquarePosition to = getSquarePosition(bestMove.subSequence(2, 4).toString());

            if (captureProcessorLlistener != null) {
                if (from == null || to == null || bestMove == null || bestMove.isEmpty()) {
                    captureProcessorLlistener.doStop();
                    return;
                }
            }

            int x1 = (int) (DesktopScreenRecorder.CAPTURE_1_X + from.point1.x) + ((int) (from.point2.x - from.point1.x) / 2);
            int y1 = (int) (DesktopScreenRecorder.CAPTURE_1_Y + from.point1.y) + ((int) (from.point2.y - from.point1.y) / 2);

            int x2 = (int) (DesktopScreenRecorder.CAPTURE_1_X + to.point1.x) + ((int) (to.point2.x - to.point1.x) / 2);
            int y2 = (int) (DesktopScreenRecorder.CAPTURE_1_Y + to.point1.y) + ((int) (to.point2.y - to.point1.y) / 2);

            Robot robot = new Robot();
            robot.mouseMove(x1, y1);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseMove(x2, y2);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);

            wait(50);

            if (captureProcessorLlistener != null) {
                captureProcessorLlistener.onMove(new MovePosition(from, to));
            }

            capturedImages.add(ImageUtils.captureImage());
        } catch (Exception e) {
            UIUtils.popupError(e, "ChessBoardProcessor::moveMouse");
        }
    }

    private SquarePosition getSquarePosition(String stat) {
        for (SquarePosition position : positions) {
            if (position.square.toString().toLowerCase().equals(stat.toLowerCase())) {
                return position;
            }
        }
        return null;
    }

    private MovePosition getMoveFromString(String bestMove) {
        MovePosition move = new MovePosition();
        move.from = getSquarePosition(bestMove.subSequence(0, 2).toString());
        move.to = getSquarePosition(bestMove.subSequence(2, 4).toString());

        return move;
    }
}
