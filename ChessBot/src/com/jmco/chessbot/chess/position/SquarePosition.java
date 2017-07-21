package com.jmco.chessbot.chess.position;

import java.util.Objects;

/**
 *
 * @author mhusam [m-husam@hotmail.com]
 * @since 1.0.0
 * @datetime Jun 30, 2017 2:05:27 PM
 */
public class SquarePosition {

    public Square square;
    public Point point1;
    public Point point2;

    public SquarePosition() {
    }

    public SquarePosition(Square square) {
        this.square = square;
    }

    public SquarePosition(Square square, Point point1, Point point2) {
        this.square = square;
        this.point1 = point1;
        this.point2 = point2;
    }

    public Square getSquare() {
        return square;
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    public Point getPoint1() {
        return point1;
    }

    public void setPoint1(Point point1) {
        this.point1 = point1;
    }

    public Point getPoint2() {
        return point2;
    }

    public void setPoint2(Point point2) {
        this.point2 = point2;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.square);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SquarePosition other = (SquarePosition) obj;
        return this.square == other.square;
    }

    @Override
    public String toString() {
        return String.valueOf(square);
    }
}
