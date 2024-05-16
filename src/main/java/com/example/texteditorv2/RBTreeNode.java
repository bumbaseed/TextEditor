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
}
