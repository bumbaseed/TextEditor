package com.example.texteditorv2;

class PieceTable {
    private final StringBuilder buffer;
    private final RedBlackTree pieces;

    PieceTable() {
        buffer = new StringBuilder();
        pieces = new RedBlackTree();
        System.out.println("Initialized PieceTable");
        logTreeStructure(); // Log initial tree structure
    }

    void insert(int position, String text) {
        if (text.isEmpty()) return;

        System.out.println("Insert called with position: " + position + ", text: " + text);

        if (pieces.getRoot() == null) {
            // Initialize with the first piece if the tree is empty
            Piece firstPiece = new Piece(0, text.length());
            pieces.insert(firstPiece);
            buffer.append(text);
            System.out.println("Inserted initial piece: " + firstPiece);
            System.out.println("Tree structure after initial piece insert: ");
            logTreeStructure(); // Log tree structure after initial piece
            return;
        }

        RBTreeNode node = findPieceIndex(position);
        if (node == null) {
            Piece newPiece = new Piece(buffer.length(), text.length());
            pieces.insert(newPiece);
            buffer.append(text);
            System.out.println("Inserted new piece at the end: " + newPiece);
            logTreeStructure();
            return;
        }

        Piece piece = node.getPiece();
        int offset = position - calculatePosition(node);
        // System.out.println("Inserting at position: " + position + ", found piece: " + piece + ", offset: " + offset);

        if (offset == 0){
            Piece newPiece = new Piece(piece.getStart(), text.length());
            pieces.insert(newPiece);
            buffer.insert(piece.getStart(), text);
            updateStartPosition(piece.getStart() + offset + text.length(), text.length());
        } else if (offset == piece.getLength()) {
            Piece newPiece = new Piece(piece.getStart() + piece.getLength(), text.length());
            pieces.insert(newPiece);
            buffer.insert(piece.getStart() + piece.getLength(), text);
            updateStartPosition(piece.getStart() + offset + text.length(), text.length());
        } else {
            Piece leftPiece = new Piece(piece.getStart(), offset);
            Piece rightPiece = new Piece(piece.getStart() + offset + text.length(), piece.getLength() - offset);
            Piece newPiece = new Piece(piece.getStart() + offset, text.length());


            // Update tree
            // pieces.delete(piece); // needed delete function to remove a piece when replaced.
            pieces.delete(piece);
            pieces.insert(leftPiece);
            pieces.insert(newPiece);
            pieces.insert(rightPiece);

            // Update buffer
            buffer.insert(piece.getStart() + offset, text);
            updateStartPosition(piece.getStart() + offset + text.length(), text.length());

            // update subtree length
            updateSubtreeLength(leftPiece);
            updateSubtreeLength(newPiece);
            updateSubtreeLength(rightPiece);
        }
        System.out.println("Buffer after insertion: " + buffer.toString());
        // Log tree structure
        logTreeStructure();
    }

    private void updateSubtreeLength(Piece piece){
        RBTreeNode node = findNode(piece.getStart());
        while (node != null){
            node.setSubtreeLength(node.getSubtreeLength() + piece.getLength());
            node = node.getParent();
        }
    }

    private void updateStartPosition(int startPosition, int textLength){
        RBTreeNode node = findNode(startPosition);
        while (node != null){
            if (node.getPiece().getStart() >= startPosition){
                node.getPiece().setStart(node.getPiece().getStart() + textLength);
            }
            node = successor(node);
        }
    }

    // helper method which aids in the sync of the tree.
    private RBTreeNode successor(RBTreeNode node){
        if (node.getRight() != null) {
            return minimum(node.getRight());
        }
        RBTreeNode parent = node.getParent();
        while (parent != null && node == parent.getRight()){
            node = parent;
            parent = parent.getParent();
        }
        return parent;
    }

    private RBTreeNode minimum(RBTreeNode node){
        while (node.getLeft() != null){
            node = node.getLeft();
        }
        return node;
    }

    private RBTreeNode findNode(int position){
        return findNodePosition(position, pieces.getRoot());
    }

    private int calculatePosition(RBTreeNode node) {
        int position = 0;

        while (node != null) {
            if (node.getParent() != null && node == node.getParent().getRight()) {
                position += (node.getParent().getLeft() != null ? node.getParent().getLeft().getSubtreeLength() : 0) + node.getParent().getPiece().getLength();
            }
            node = node.getParent();
        }
        return position;
    }

    String getText() {
        StringBuilder result = new StringBuilder();
        buildText(result, pieces.getRoot());
        return result.toString();
    }

    private void buildText(StringBuilder result, RBTreeNode node) {
        if (node != null) {
            buildText(result, node.getLeft()); // recursion left
            Piece piece = node.getPiece();
            if (piece != null) {
                result.append(buffer.substring(piece.getStart(), piece.getStart() + piece.getLength()));
            }
            buildText(result, node.getRight()); // recursion right
        }
    }

    private RBTreeNode findNodePosition(int position, RBTreeNode node) {
        if (node == null) {
            return null;
        }

        int leftLength = (node.getLeft() != null) ? node.getLeft().getSubtreeLength() : 0;
        int totalLength = leftLength + node.getPiece().getLength();

        System.out.println("Visiting node with piece start: " + node.getPiece().getStart() + ", length: " + node.getPiece().getLength() + ", calculated leftLength: " + leftLength);

        if (position < leftLength) {
            return findNodePosition(position, node.getLeft());
        } else if (position <= totalLength ) {
            return node;
        } else if (node.getRight() != null){
            return findNodePosition(position - totalLength, node.getRight());
        } else {
            return null;
        }
    }

    private RBTreeNode findPieceIndex(int position) {
        return findNodePosition(position, pieces.getRoot());
    }

    private void logTreeStructure() {
        System.out.println("Current tree structure:");
        logTreeNode(pieces.getRoot(), 0);
    }

    private void logTreeNode(RBTreeNode node, int depth) {
        if (node == null) return;
        logTreeNode(node.getLeft(), depth + 1);
        System.out.println("Depth: " + depth + ", Piece: " + node.getPiece() + ", Subtree Length: " + node.getSubtreeLength());
        logTreeNode(node.getRight(), depth + 1);
    }


}

