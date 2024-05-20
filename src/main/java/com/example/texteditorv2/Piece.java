package com.example.texteditorv2;


public class Piece {
    private int start;
    private int length;

    public Piece(int start, int length) {
        if (start < 0 || length < 0) {
            throw new IllegalArgumentException("Start and length must be non-negative");
        }
        this.start = start;
        this.length = length;
    }


    public int getStart() {
        return start;
    }

    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "Piece{" + "start=" + start + ", length=" + length + '}';
    }

    public void setStart(int i) {
        this.start = start;
    }

    public void setLength(int i) {
        this.length = length;
    }
}

