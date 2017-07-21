package com.jmco.chessbot.chess;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 *
 * @author mhusam [m-husam@hotmail.com]
 * @since 1.0.0
 * @datetime Jun 30, 2017 2:05:27 PM
 */
public final class ChessDrawerWindow extends Window implements WindowListener {

    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;
    
    public ChessDrawerWindow() {
        super(null);
        this.x1 = 0;
        this.y1 = 0;
        this.x2 = 0;
        this.y2 = 0;
    }

    public ChessDrawerWindow(int x1, int y1, int x2, int y2, Window owner) {
        super(owner);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        
        setAlwaysOnTop(true);
        setBounds(getGraphicsConfiguration().getBounds());
        setBackground(new Color(0, true));
        setVisible(true);
    }
    
    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        
        g2.setColor(Color.ORANGE);
        g2.setStroke(new BasicStroke(2));

        if (x1 < x2) {
            
            int width = x2 - x1;
            int height = y2 - y1;
            
            int xCell = width / 8;
            int yCell = height / 8;
            
            int xDif = xCell;
            int yDif = yCell;
            
            for(int i = 0; i < 7; i++){
                
                g2.drawLine(x1, y1 + yCell, x2, y2 - height + yCell);
                g2.drawLine(x1 + xCell, y1, x2 - width + xCell, y2);
                
                yCell += yDif;
                xCell += xDif;
            }
            
            g2.drawRect(x1, y1, x2 - x1, y2 - y1);
        }
    }

    @Override
    public void update(Graphics g) {
        super.update(g);
    }
   
    @Override
    public void windowOpened(WindowEvent e) {
        
    }

    @Override
    public void windowClosing(WindowEvent e) {
        dispose();
    }

    @Override
    public void windowClosed(WindowEvent e) {
        dispose();
    }

    @Override
    public void windowIconified(WindowEvent e) {
        
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        
    }

    @Override
    public void windowActivated(WindowEvent e) {
        
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        dispose();
    }

}
