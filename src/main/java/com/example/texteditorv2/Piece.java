package com.example.texteditorv2;


import com.example.texteditorv2.RedBlackTree;


public class Piece {
    int start;
    int length;

    Piece(int start, int length){
        this.start = start;
        this.length = length;
    }
}

class PieceTable {
    private final StringBuilder buffer;
    private final RedBlackTree pieces;

    PieceTable(){
        buffer = new StringBuilder();
        pieces = new RedBlackTree();
        pieces.insert(new Piece(0,0)); // Empty piece to start with
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
        int piecePosition = piece.start + offset;

        if (offset == 0){
            pieces.insert(new Piece(buffer.length(), text.length()));
        } else if (offset == piece.length) {
            pieces.insert(new Piece(buffer.length(), text.length()));
        } else {
            int newPieceLength = piece.length - offset;
            piece.length = offset;
            pieces.insert(new Piece(buffer.length(), text.length()));
            pieces.insert(new Piece(piecePosition, newPieceLength));
        }
        buffer.append(text);
        System.out.println("Successful insertion: " + text);
    }



    String getText(){
        StringBuilder result = new StringBuilder();
        buildText(result, pieces.getRoot());
        return result.toString();

    }

    private void buildText(StringBuilder result, RBTreeNode node){
        if (node != null) {
            buildText(result, node.left); // recursion left
            Piece piece = node.piece;
            if (piece != null){
                result.append(buffer.substring(piece.start, piece.start + piece.length));
            }
            buildText(result, node.right); // recursion right
        }

    }

    private RBTreeNode findNodePosition(int position, RBTreeNode node){
        if (node == null){
            return null;
        }

        int leftLength = (node.left != null) ? node.left.subtreeLength : 0;

        if (position < leftLength) {
            return findNodePosition(position, node.left);
        }

        int currentPosition = leftLength;
        if (position >= currentPosition && position < currentPosition + node.piece.length){
            return node;
        }

        position -= (currentPosition + node.piece.length);
        return findNodePosition(position, node.right);
    }

    private RBTreeNode findPieceIndex(int position){
        return findNodePosition(position, pieces.getRoot());
    }

    private int getTextLength(int startIndex, int endIndex) {
        int textLength = 0;
        for (int i = startIndex; i < endIndex; i++){
            textLength += pieces.get(i).length;
        }
        return textLength;
    }

    private int calculatePosition(RBTreeNode node){
        int position = node.piece.length;

        while (node.parent != null) {
            if (node == node.parent.right){
                position += (node.parent.left != null ? node.parent.left.subtreeLength : 0) + node.parent.piece.length;
            }
            node = node.parent;
        }
        return position;
    }
}
