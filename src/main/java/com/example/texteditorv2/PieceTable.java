package com.example.texteditorv2;

class PieceTable {
    private final StringBuilder buffer;
    private final RedBlackTree pieces;

    PieceTable() {
        buffer = new StringBuilder();
        pieces = new RedBlackTree();
        pieces.insert(new Piece(0, 0)); // Empty piece to start with
    }


    void insert(int position, String text) {
        if (text.isEmpty()) return;

        RBTreeNode node = findPieceIndex(position);
        if (node == null) {
            System.out.println("No node found at this position: " + position);
            return;
        }

        Piece piece = node.piece;

        int offset = position - calculatePosition(node);

        // Split into pieces
        Piece newPiece1 = new Piece(piece.start, offset);
        Piece newPiece2 = new Piece(buffer.length(), text.length());
        Piece newPiece3 =new Piece(piece.start + offset, piece.length - offset);

        // update buffer
        buffer.insert(buffer.length(), text);

        // update tree
       //  pieces.delete(node.piece); // removes previous piece.
        pieces.insert(newPiece1);
        pieces.insert(newPiece2);
        pieces.insert(newPiece3);
    }

    private int calculateOffset(RBTreeNode node){
        int offset = 0;

        while(node != null){
            if (node.parent != null && node == node.parent.right){
                offset += node.parent.left != null ? node.parent.left.subtreeLength : 0;
                offset += node.parent.piece.length;
            }
            node = node.parent;
        }
        return  offset;
    }


    String getText() {
        StringBuilder result = new StringBuilder();
        buildText(result, pieces.getRoot());
        return result.toString();

    }

    private void getTextRec(RBTreeNode node, StringBuilder result){
        if (node == null) return;
        getTextRec(node.left, result);
        result.append(buffer.substring(node.piece.start, node.piece.start + node.piece.length));
        getTextRec(node.right, result);
    }

    private void buildText(StringBuilder result, RBTreeNode node) {
        if (node != null) {
            buildText(result, node.left); // recursion left
            Piece piece = node.piece;
            if (piece != null) {
                result.append(buffer.substring(piece.start, piece.start + piece.length));
            }
            buildText(result, node.right); // recursion right
        }

    }

    private RBTreeNode findNodePosition(int position, RBTreeNode node) {
        if (node == null) {
            return null;
        }

        int leftLength = (node.left != null) ? node.left.subtreeLength : 0;

        if (position < leftLength){
            return findNodePosition(position, node.left);
        } else if (position < leftLength + node.piece.length) {
            return node;
        } else {
            return findNodePosition(position - (leftLength + node.piece.length), node.right);
        }
    }

    private RBTreeNode findPieceIndex(int position) {
        return findNodePosition(position, pieces.getRoot());
    }

    private int getTextLength(int startIndex, int endIndex) {
        int textLength = 0;
        for (int i = startIndex; i < endIndex; i++) {
            textLength += pieces.get(i).length;
        }
        return textLength;
    }

    private int calculatePosition(RBTreeNode node) {
        int position = node.piece.length;

        while (node.parent != null) {
            if (node == node.parent.right) {
                position += (node.parent.left != null ? node.parent.left.subtreeLength : 0) + node.parent.piece.length;
            }
            node = node.parent;
        }
        return position;
    }
}