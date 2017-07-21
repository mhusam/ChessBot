package com.jmco.chessbot.chess;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Window;

/**
 *
 * @author mhusam [m-husam@hotmail.com]
 * @since 1.0.0
 * @datetime Jun 30, 2017 2:05:27 PM
 */
public class ChessScreenBoard {

    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;

    private final Graphics2D g2;
    private final Window window;

    public ChessScreenBoard(int x1, int y1, int x2, int y2, Window window) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.g2 = (Graphics2D) window.getGraphics();
        this.window = window;
    }

    public void drawRectangle() {
        window.dispose();

        final Font font = g2.getFont().deriveFont(48f);
        g2.setFont(font);
        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(20));

        if (x1 < x2) {
            g2.drawRect(x1, y1, x2 - x1, y2 - y1);
        } else {
            g2.drawRect(x2, y2, x1 - x2, y1 - y2);
        }

        window.paint(g2);
    }

    public Graphics2D drawWords() {
        final Font font = g2.getFont().deriveFont(48f);
        g2.setFont(font);
        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(20));

        final String message = ".";
        FontMetrics metrics = g2.getFontMetrics();
        g2.drawString(message, (window.getWidth() - metrics.stringWidth(message)) / 2, (window.getHeight() - metrics.getHeight()) / 2);

        return g2;
    }
}
