package com.jmco.chessbot.chess.commands;

import com.jmco.utils.UIUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 *
 * @author mhusam [m-husam@hotmail.com]
 * @since 1.0.0
 * @datetime Jun 30, 2017 2:05:27 PM
 */
public class Stockfish {

    public static final String STOCKFISH = "stockfish.exe";

    private Process engineProcess;
    private BufferedReader processReader;
    private OutputStreamWriter processWriter;

    private String getEnginePath() throws Exception {
        InputStream src = (InputStream) Stockfish.class.getResource(STOCKFISH).openStream();
        File exeTempFile = File.createTempFile("engineStockfish", ".exe");
        FileOutputStream out = new FileOutputStream(exeTempFile);
        byte[] temp = new byte[2048];
        int rc;
        while ((rc = src.read(temp)) > 0) {
            out.write(temp, 0, rc);
        }
        src.close();
        out.close();
        //exeTempFile.deleteOnExit();
        return exeTempFile.getAbsolutePath();
    }

    public boolean startEngine() {
        try {
            engineProcess = Runtime.getRuntime().exec(getEnginePath());
            processReader = new BufferedReader(new InputStreamReader(engineProcess.getInputStream()));
            processWriter = new OutputStreamWriter(engineProcess.getOutputStream());
        } catch (Exception e) {
            UIUtils.popupError(e, "Stockfish:startEngine");
            return false;
        }
        return true;
    }

    public void sendCommand(String command) {
        try {
            processWriter.write(command + "\n");
            processWriter.flush();
        } catch (IOException e) {
            UIUtils.popupError(e, "Stockfish:sendCommand");
        }
    }

    public String getOutput(int waitTime) {
        StringBuilder buffer = new StringBuilder();
        try {
            Thread.sleep(waitTime);
            sendCommand("isready");
            while (true) {
                String text = processReader.readLine();
                if (text.equals("readyok")) {
                    break;
                } else {
                    buffer.append(text).append("\n");
                }
            }
        } catch (IOException | InterruptedException e) {
            UIUtils.popupError(e, "Stockfish:getOutput");
        }
        return buffer.toString();
    }

    public String getBestMove(String fen, int waitTime) {
        return getBestMove(fen, waitTime, false);
    }

    public String getBestMove(String fen, int waitTime, boolean pander) {
        sendCommand("position startpos moves " + fen);
        sendCommand("go movetime " + waitTime);
        String output = getOutput(waitTime + 20);
        try {
            String line = output.substring(output.lastIndexOf("bestmove"), output.length() - 1);
            if (pander) {
                String[] parts = line.split(" ");
                return parts[parts.length - 1];
            } else {
                return line.split("bestmove ")[1].split(" ")[0];
            }
        } catch (Exception e) {
            UIUtils.popupError(e, "Stockfish:getBestMove");
            return getBestMove(fen, waitTime);
        }
    }

    public void stopEngine() {
        try {
            sendCommand("quit");
            processReader.close();
            processWriter.close();
        } catch (IOException e) {
            UIUtils.popupError(e, "Stockfish:stopEngine");
        }
    }

    public String getLegalMoves(String fen) {
        sendCommand("position fen " + fen);
        sendCommand("d");
        return getOutput(0).split("Legal moves: ")[1];
    }

    public void drawBoard(String fen) {
        sendCommand("position startpos moves " + fen);
        sendCommand("d");
    }

    public float getEvalScore(String fen, int waitTime) {
        sendCommand("position fen " + fen);
        sendCommand("go movetime " + waitTime);

        float evalScore = 0.0f;
        String[] dump = getOutput(waitTime + 20).split("\n");
        for (int i = dump.length - 1; i >= 0; i--) {
            if (dump[i].startsWith("info depth ")) {
                try {
                    evalScore = Float.parseFloat(dump[i].split("score cp ")[1]
                            .split(" nodes")[0]);
                } catch (NumberFormatException e) {
                    UIUtils.popupError(e, "Stockfish:getEvalScore");
                    evalScore = Float.parseFloat(dump[i].split("score cp ")[1]
                            .split(" upperbound nodes")[0]);
                }
            }
        }
        return evalScore / 100;
    }
}
