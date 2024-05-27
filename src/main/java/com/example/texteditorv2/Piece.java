package com.example.texteditorv2;

import java.util.Objects;

public class Piece {
    private int start;
    private int length;

    public Piece(int start, int length) {
        this.start = start;
        this.length = length;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Piece piece = (Piece) obj;
        return start == piece.start && length == piece.length;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, length);
    }

    @Override
    public String toString() {
        return "Piece{start=" + start + ", length=" + length + '}';
    }
}



