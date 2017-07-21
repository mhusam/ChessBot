package com.jmco.chessbot.chess.position;

import java.util.Objects;

/**
 *
 * @author mhusam [m-husam@hotmail.com]
 * @since 1.0.0
 * @datetime Jun 30, 2017 2:05:27 PM
 */
public class MovePosition {

    public SquarePosition from;
    public SquarePosition to;
    public String replaced = "";

    public MovePosition() {
    }

    public MovePosition(SquarePosition from, SquarePosition to) {
        this.from = from;
        this.to = to;
    }

    public MovePosition(SquarePosition from, SquarePosition to, String replaced) {
        this.from = from;
        this.to = to;
        this.replaced = replaced;
    }    
    
    public SquarePosition getFrom() {
        return from;
    }

    public void setFrom(SquarePosition from) {
        this.from = from;
    }

    public SquarePosition getTo() {
        return to;
    }

    public void setTo(SquarePosition to) {
        this.to = to;
    }

    public String getReplaced() {
        return replaced;
    }

    public void setReplaced(String replaced) {
        this.replaced = replaced;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.from);
        hash = 97 * hash + Objects.hashCode(this.to);
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
        final MovePosition other = (MovePosition) obj;
        if (!Objects.equals(this.from, other.from)) {
            return false;
        }
        if (!Objects.equals(this.to, other.to)) {
            return false;
        }
        return true;
    }
    
    public String getStringPosition(){
        return String.format("%s%s%s", from, to, replaced);
    }
    
    @Override
    public String toString() {
        return from + "->" + to + replaced;
    }
}
