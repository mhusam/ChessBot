package com.jmco.chessbot.chess.position;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author mhusam [m-husam@hotmail.com]
 * @since 1.0.0
 * @datetime Jun 30, 2017 2:05:27 PM
 */
public class ChessBoardUtils {

    public static int MOVE_NUMBER = 1;

    private static final int HASH_X = 5;
    private static final int HASH_Y = 5;
    private static final int HASH_LI = 6;

    private static final String IMAGE_TYPE_JPEG = "JPEG";
    private static final String IMAGE_TYPE_JPG = "JPG";

    public static final String IMAGE_MIME_TYPE = IMAGE_TYPE_JPEG;

    public static Square[][] SQUARES = new Square[][]{
        {Square.A8, Square.B8, Square.C8, Square.D8, Square.E8, Square.F8, Square.G8, Square.H8},
        {Square.A7, Square.B7, Square.C7, Square.D7, Square.E7, Square.F7, Square.G7, Square.H7},
        {Square.A6, Square.B6, Square.C6, Square.D6, Square.E6, Square.F6, Square.G6, Square.H6},
        {Square.A5, Square.B5, Square.C5, Square.D5, Square.E5, Square.F5, Square.G5, Square.H5},
        {Square.A4, Square.B4, Square.C4, Square.D4, Square.E4, Square.F4, Square.G4, Square.H4},
        {Square.A3, Square.B3, Square.C3, Square.D3, Square.E3, Square.F3, Square.G3, Square.H3},
        {Square.A2, Square.B2, Square.C2, Square.D2, Square.E2, Square.F2, Square.G2, Square.H2},
        {Square.A1, Square.B1, Square.C1, Square.D1, Square.E1, Square.F1, Square.G1, Square.H1}
    };

    public static MovePosition findMove(BufferedImage before, BufferedImage after, List<SquarePosition> positions) throws Exception {
        return findMove(before, after, null, positions);
    }

    public static MovePosition findMove(BufferedImage before, BufferedImage after, MovePosition previousMove, List<SquarePosition> positions) throws Exception {
        List<SquarePosition> diffPoints = new ArrayList<>();

        before = convertAndAdjustImage(before);
        after = convertAndAdjustImage(after);
        
        int w = before.getWidth();
        int h = before.getHeight();

        if (positions == null) {
            positions = findSquarePositions(before);
        }

        int[] p1 = before.getRGB(0, 0, w, h, null, 0, w);
        int[] p2 = after.getRGB(0, 0, w, h, null, 0, w);

        double fx = 0;
        double fy = 0;

        for (int i = 0; i < p1.length; i++, fx++) {
            if (p1[i] != p2[i]) {
                addPointSquareRange(positions, diffPoints, fx, fy);
            }

            if (fx == w) {
                fx = 0;
                fy++;
            }
        }

        MovePosition move = new MovePosition();

        if (diffPoints.size() != 2) {
            diffPoints.remove(previousMove.from);
            diffPoints.remove(previousMove.to);
        }

        if (diffPoints.size() == 4) {
            findKingMove(diffPoints, move);
        }

        if (diffPoints.size() == 2 && move.from == null) {

            boolean isPointEmpty = !isEmptyCell(after, diffPoints.get(0));

            if (isPointEmpty) {
                move.from = diffPoints.get(1);
                move.to = diffPoints.get(0);
            } else {
                move.from = diffPoints.get(0);
                move.to = diffPoints.get(1);
            }
        }
        
        MOVE_NUMBER++;
        return move;
    }
    
    public static BufferedImage convertAndAdjustImage(BufferedImage board) {
        adjustImage(board);
        return convert2bw(board);
    }

    public static void adjustImage(BufferedImage board) {
        int w = board.getWidth();
        int h = board.getHeight();

        int wDiff = w + (w % 8);
        int hDiff = h + (h % 8);

        if (wDiff != hDiff) {
            if (wDiff < hDiff) {
                hDiff = wDiff;
            } else {
                wDiff = hDiff;
            }
        }

        Image img = board.getScaledInstance(wDiff, hDiff, Image.SCALE_DEFAULT);
        board = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = board.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
    }

    public static BufferedImage convert2bw(BufferedImage image) {
        float valFloat = 800f / 1000f;
        BufferedImage bi = brighten(image, valFloat);
        BufferedImage bw = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        Graphics g = bw.createGraphics();
        g.drawImage(bi, 0, 0, null);
        g.dispose();

        return bw;
    }

    public static BufferedImage brighten(BufferedImage src, float level) {
        BufferedImage dst = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
        float[] scales = {2.2f, 2.2f, 2.2f};
        float[] offsets = new float[4];
        RescaleOp rop = new RescaleOp(scales, offsets, null);

        Graphics2D g = dst.createGraphics();
        g.drawImage(src, rop, 0, 0);
        g.dispose();
        
        return dst;
    }

    public static BufferedImage blur(BufferedImage src) {
        BufferedImage bufferedImage = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);

        int s1 = 7;
        int s2 = 7;
        float level = .1f / 9f;

        float[] filter = new float[s1 * s2];

        for (int i = 0; i < s1 * s2; i++) {
            filter[i] = level;
        }

        Kernel kernel = new Kernel(s1, s2, filter);
        BufferedImageOp op = new ConvolveOp(kernel);
        bufferedImage = op.filter(src, null);

        return bufferedImage;
    }

    public static boolean isEmptyCell(List<BufferedImage> emptyCells, BufferedImage before) throws IOException {
        return emptyCells.stream().anyMatch((cell) -> (isEqualsImages(before, cell)));
    }

    public static boolean isEmptyCell(BufferedImage before, SquarePosition square) throws Exception {
        int w = (int) square.point2.x - (int) square.point1.x;
        int h = (int) square.point2.y - (int) square.point1.y;
        int li = w - (w / HASH_LI);

        BufferedImage rgbBefore = before.getSubimage(
                (int) Math.round(square.point1.x) + HASH_X,
                (int) Math.round(square.point1.y) + HASH_Y, li, li);
        int[] p1 = rgbBefore.getRGB(0, 0, rgbBefore.getWidth(), rgbBefore.getHeight(), null, 0, rgbBefore.getWidth());
        
        for (int i = 0; i < p1.length; i++) {
            if (p1[i] != -1) {
                return false;
            }
        }
        return true;
    }

    public static boolean isMovingStopped(BufferedImage board) {
        board = convertAndAdjustImage(board);
        
        int w = board.getWidth();
        int h = board.getHeight();
        int li = w / 8;
        int hash = (int) 0.002 * w + 1;
        
        for (int x = li; x < w - hash; x += li) {
            for (int y = 0; y < h - hash; y++) {
                int colorX = board.getRGB(x, y);
                if (!isColorBlackOrWhite(colorX)) {
                    return false;
                }

                int colorY = board.getRGB(y, x);
                if (!isColorBlackOrWhite(colorY)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isColorBlackOrWhite(int color) {
        int red = (color & 0x00ff0000) >> 16;
        int green = (color & 0x0000ff00) >> 8;
        int blue = color & 0x000000ff;
        if ((red == 0 || red < 220)
                && (green == 0 || green < 220)
                && (blue == 0 || blue < 220)) {
            return false;
        }
        return true;
    }
    
    public static boolean isEqualsImages(BufferedImage image1, BufferedImage image2) {
        int w = image1.getWidth();
        int h = image1.getHeight();

        int[] p1 = image1.getRGB(0, 0, w, h, null, 0, w);
        int[] p2 = image2.getRGB(0, 0, w, h, null, 0, w);

        for (int i = 0; i < p1.length; i++) {
            if (p1[i] != p2[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEqualsPositions(BufferedImage image1, BufferedImage image2, List<SquarePosition> positions) {
        image1 = convertAndAdjustImage(image1);
        image2 = convertAndAdjustImage(image2);

        if (positions == null) {
            positions = findSquarePositions(image1);
        }
        int w = image1.getWidth();
        int h = image1.getHeight();
        int li = w / 8;
        int hash = (int)(w / 8.0) / 4;

        for (SquarePosition point : positions) {
                if (!isEqualCell(image1, image2, point, (int) hash)) {
                    return false;
                }
        }
        return true;
    }

    public static boolean isEqualCell(BufferedImage image1, BufferedImage image2, SquarePosition point, int hash) {
        int x1 = (int) point.point1.x + hash;
        int y1 = (int) point.point1.y + hash;
        int x2 = (int) point.point2.x - hash - x1;
        int y2 = (int) point.point2.y - hash - y1;

        int[] p1 = image1.getRGB(x1, y1, x2, y2, null, 0, x2);

        Boolean isThereOneBlackPoint = Boolean.FALSE;
        for (int i = 0; i < p1.length; i++) {
            if (p1[i] != -1) {
                isThereOneBlackPoint = Boolean.TRUE;
                break;
            }
        }

        int[] p2 = image2.getRGB(x1, y1, x2, y2, null, 0, x2);

        for (int i = 0; i < p1.length; i++) {
            if (p2[i] != -1) {
                if (isThereOneBlackPoint) {
                    return Boolean.TRUE;
                } else {
                    return Boolean.FALSE;
                }
            }
        }

        return isThereOneBlackPoint;
    }

    public static boolean isOneDiff(BufferedImage image1, BufferedImage image2, List<SquarePosition> positions) {
        List<SquarePosition> diffPoints = new ArrayList<>();

        int w = image1.getWidth();
        int h = image1.getHeight();

        int[] p1 = image1.getRGB(0, 0, w, h, null, 0, w);
        int[] p2 = image2.getRGB(0, 0, w, h, null, 0, w);

        double fx = 0;
        double fy = 0;

        for (int i = 0; i < p1.length; i++, fx++) {
            if (p1[i] != p2[i]) {
                addPointSquareRange(positions, diffPoints, fx, fy);
            }

            if (fx == w) {
                fx = 0;
                fy++;
            }
        }
        return diffPoints.size() < 2;
    }

    public static void addPointSquareRange(List<SquarePosition> positions, List<SquarePosition> diffPoints, double x, double y) {
        SquarePosition position = findSquarePositionInPoints(positions, x, y);
        if (position != null && position.square != null && !diffPoints.contains(position)) {
            diffPoints.add(position);
        }
    }

    public static SquarePosition findSquarePositionInPoints(List<SquarePosition> positions, double x, double y) {
        SquarePosition position = new SquarePosition();
        positions.forEach((pos) -> {
            if (x >= pos.point1.x && x <= pos.point2.x) {
                if (y >= pos.point1.y && y <= pos.point2.y) {
                    position.square = pos.square;
                    position.point1 = pos.point1;
                    position.point2 = pos.point2;
                }
            }
        });
        return position;
    }

    public static List<SquarePosition> findSquarePositionsConvert(BufferedImage image) {
        image = convertAndAdjustImage(image);
        return findSquarePositions(image);
    }

    public static List<SquarePosition> findSquarePositions(BufferedImage image) {
        double boardWidth = image.getWidth();
        double cellWidth = boardWidth / 8.0;

        List<SquarePosition> positions = new ArrayList<>();

        int hash = (int) cellWidth / 4;

        double x1 = 0;
        double y1 = 0;
        double x2 = 0;
        double y2 = 0;

        SquarePosition position;
        for (int i = 0; i < 8; i++) {
            y2 += cellWidth;

            for (int j = 0; j < 8; j++) {
                x2 += cellWidth;
                position = new SquarePosition();
                position.square = SQUARES[i][j];
                position.point1 = new Point(x1 + hash, y1 + hash);
                position.point2 = new Point(x2 - hash, y2 - hash);
                positions.add(position);
                x1 += cellWidth;
            }
            x2 = 0;
            x1 = 0;
            y1 += cellWidth;
        }

        return positions;
    }

    private static void findKingMove(List<SquarePosition> diffPoints, MovePosition move) {
        //E1C1
        if (diffPoints.containsAll(Arrays.asList(new SquarePosition(Square.E1),
                new SquarePosition(Square.F1),
                new SquarePosition(Square.H1),
                new SquarePosition(Square.G1)))) {
            diffPoints.clear();

            SquarePosition from = new SquarePosition(Square.E1);
            SquarePosition to = new SquarePosition(Square.G1);
            diffPoints.add(from);
            diffPoints.add(to);
            move.setFrom(from);
            move.setTo(to);
        }

        //E8C8
        if (diffPoints.containsAll(Arrays.asList(new SquarePosition(Square.E8),
                new SquarePosition(Square.F8),
                new SquarePosition(Square.H8),
                new SquarePosition(Square.G8)))) {
            diffPoints.clear();

            SquarePosition from = new SquarePosition(Square.E8);
            SquarePosition to = new SquarePosition(Square.G8);
            diffPoints.add(from);
            diffPoints.add(to);
            move.setFrom(from);
            move.setTo(to);
        }

        //E1C1
        if (diffPoints.containsAll(Arrays.asList(new SquarePosition(Square.C1),
                new SquarePosition(Square.E1),
                new SquarePosition(Square.A1),
                new SquarePosition(Square.D1)))) {
            diffPoints.clear();

            SquarePosition from = new SquarePosition(Square.E1);
            SquarePosition to = new SquarePosition(Square.C1);
            diffPoints.add(from);
            diffPoints.add(to);
            move.setFrom(from);
            move.setTo(to);
        }

        //E8C8
        if (diffPoints.containsAll(Arrays.asList(new SquarePosition(Square.C8),
                new SquarePosition(Square.E8),
                new SquarePosition(Square.A8),
                new SquarePosition(Square.D8)))) {
            diffPoints.clear();

            SquarePosition from = new SquarePosition(Square.E8);
            SquarePosition to = new SquarePosition(Square.C8);
            diffPoints.add(from);
            diffPoints.add(to);
            move.setFrom(from);
            move.setTo(to);
        }
    }
}
