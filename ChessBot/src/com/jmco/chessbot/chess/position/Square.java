package com.jmco.chessbot.chess.position;

/**
 *
 * @author mhusam [m-husam@hotmail.com]
 * @since 1.0.0
 * @datetime Jun 30, 2017 2:05:27 PM
 */
public enum Square {
    A8(1, 8), B8(1, 8), C8(1, 8), D8(1, 8), E8(1, 8), F8(1, 8), G8(1, 8), H8(1, 8),
    A7(2, 7), B7(2, 7), C7(2, 7), D7(2, 7), E7(2, 7), F7(2, 7), G7(2, 7), H7(2, 7),
    A6(3, 6), B6(3, 6), C6(3, 6), D6(3, 6), E6(3, 6), F6(3, 6), G6(3, 6), H6(3, 6),
    A5(4, 5), B5(4, 5), C5(4, 5), D5(4, 5), E5(4, 5), F5(4, 5), G5(4, 5), H5(4, 5),
    A4(5, 4), B4(5, 4), C4(5, 4), D4(5, 4), E4(5, 4), F4(5, 4), G4(5, 4), H4(5, 4),
    A3(6, 3), B3(6, 3), C3(6, 3), D3(6, 3), E3(6, 3), F3(6, 3), G3(6, 3), H3(6, 3),
    A2(7, 2), B2(7, 2), C2(7, 2), D2(7, 2), E2(7, 2), F2(7, 2), G2(7, 2), H2(7, 2),
    A1(8, 1), B1(8, 1), C1(8, 1), D1(8, 1), E1(8, 1), F1(8, 1), G1(8, 1), H1(8, 1);

    public int xLine;
    public int yLine;

    private Square(int xLine, int yLine) {
        this.xLine = xLine;
        this.yLine = yLine;
    }

    public int getxLine() {
        return xLine;
    }

    public int getyLine() {
        return yLine;
    }
    
    public static boolean isEquals(MovePosition move, Square s1, Square s2){
        return move.from.square.equals(s1) && move.to.square.equals(s2);
    }
    
    public static boolean isKingWhiteRightMove(MovePosition move){
        return Square.isEquals(move, Square.E1, Square.G1);
    }
    
    public static boolean isKingBlackRightMove(MovePosition move){
        return Square.isEquals(move, Square.E8, Square.G8);
    }
    
    public static boolean isKingWhiteLeftMove(MovePosition move){
        return Square.isEquals(move, Square.E1, Square.C1);
    }
    
    public static boolean isKingBlackLeftMove(MovePosition move){
        return Square.isEquals(move, Square.E8, Square.C8);
    }
}
