package com.example.texteditorv2;

public class RBTreeNode {

        Piece piece;
        RBTreeNode left, right, parent;
        boolean isRed;
        int subtreeLength;

        public RBTreeNode(Piece piece) {
            this.piece = piece;
            this.left = this.right = this.parent = null;
            this.isRed = true;
            this.subtreeLength = piece.length;
        }
}
