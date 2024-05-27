package com.example.texteditorv2;


public class RBTreeNode {
    private Piece piece;
    private RBTreeNode left, right, parent;
    private boolean color; // true for red, false for black
    private int subtreeLength;
    private int subtreeLFLeft;
    private int sizeLeft;

    public RBTreeNode(Piece piece) {
        this.piece = piece;
        this.color = true; // new nodes are red by default
        this.subtreeLength = piece.getLength();
        this.subtreeLFLeft = 0;
        this.sizeLeft = 0;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public RBTreeNode getLeft() {
        return left;
    }

    public void setLeft(RBTreeNode left) {
        this.left = left;
    }

    public RBTreeNode getRight() {
        return right;
    }

    public void setRight(RBTreeNode right) {
        this.right = right;
    }

    public RBTreeNode getParent() {
        return parent;
    }

    public void setParent(RBTreeNode parent) {
        this.parent = parent;
    }

    public boolean isColor() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    public int getSubtreeLength() {
        return subtreeLength;
    }

    public void setSubtreeLength(int subtreeLength) {
        this.subtreeLength = subtreeLength;
    }

    public int getSubtreeLFLeft(){
        return subtreeLFLeft;
    }

    public void setSubtreeLFLeft(int subtreeLFLeft) {
        this.subtreeLFLeft = subtreeLFLeft;
    }

    public int getSizeLeft() {
        return sizeLeft;
    }

    public void setSizeLeft(int sizeLeft) {
        this.sizeLeft = sizeLeft;
    }
}


