package com.example.texteditorv2;

public class RBTreeNode {

        Piece piece;
        RBTreeNode left, right, parent;
        int subtreeLength;
        boolean color; // true = red, false = black.

        public RBTreeNode(Piece piece) {
            this.piece = piece;
            this.color = true;
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

        public int getSubtreeLength() {
            return subtreeLength;
        }

        public void setSubtreeLength(int subtreeLength) {
            this.subtreeLength = subtreeLength;
        }

        public boolean isColor() {
            return color;
        }

        public void setColor(boolean color) {
            this.color = color;
        }
}
