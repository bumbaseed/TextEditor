package com.example.texteditorv2;


class PieceTable {
    final int averageBufferSize = 65535;
    private final StringBuilder buffer;
    private final RedBlackTree pieces;

    PieceTable() {
        buffer = new StringBuilder();
        pieces = new RedBlackTree();
        int lineCount = 1;
        int length = 0;
        System.out.println("Initialized PieceTable");
    }


    void insert(int position, String text) {
        if (text.isEmpty()) return;

        if (buffer.length() == 0 && position != 0) {
            throw new IllegalArgumentException("Cannot insert at position: " + position + " in empty buffer");
        }

        if (buffer.length() == 0) {
            Piece newPiece = new Piece(0, text.length());
            pieces.insert(newPiece);
            buffer.append(text);
            return;
        }

        System.out.println("Insert called with position: " + position + ", text: " + text);

        if (pieces.getRoot() == null) {
            if (position != 0) {
                throw new IllegalArgumentException("Invalid insert position for an empty tree");
            }
            Piece firstPiece = new Piece(0, text.length());
            pieces.insert(firstPiece);
            buffer.append(text);
            System.out.println("Inserted initial piece: " + firstPiece);
            return;
        }

        RBTreeNode node = findPieceIndex(position);
        if (node == null) {
            int startPos = buffer.length();
            if (startPos < 0) {
                throw new IllegalArgumentException("Invalid startPos");
            }
            Piece newPiece = new Piece(buffer.length(), text.length());
            pieces.insert(newPiece);
            buffer.append(text);
            System.out.println("Inserted new piece at the end: " + newPiece);
            return;
        }

        Piece piece = node.getPiece();
        int offset = position - calculatePosition(node);

        if (offset == piece.getLength() && node == pieces.getRoot() && text.length() < averageBufferSize){
            appendToNode(node ,text);
            return;
        }

        if (offset < 0 || offset > piece.getLength()) {
            throw new IllegalArgumentException("Invalid insert position");
        }

        if (offset == 0) {
            int startPos = piece.getStart();
            if (startPos < 0) {
                throw new IllegalArgumentException("Invalid start position");
            }
            Piece newPiece = new Piece(startPos, text.length());
            pieces.insert(newPiece);
            buffer.insert(startPos, text);
            updateTreeData(node, text.length(),0);
        } else if (offset == piece.getLength()) {
            int startPos = piece.getStart() + piece.getLength();
            if (startPos < 0) {
                throw new IllegalArgumentException("Invalid start position");
            }
            Piece newPiece = new Piece(startPos, text.length());
            pieces.insert(newPiece);
            buffer.insert(startPos, text);
            updateTreeData(node, text.length(),0);
        } else {
            int leftStartPos = piece.getStart();
            int leftLength = offset;
            int rightStartPos = piece.getStart() + offset + text.length();
            int rightLength = piece.getLength() - offset;

            if (leftStartPos < 0 || leftLength < 0 || rightStartPos < 0 || rightLength < 0) {
                throw new IllegalArgumentException("Invalid piece split");
            }

            Piece leftPiece = new Piece(leftStartPos, leftLength);
            Piece rightPiece = new Piece(rightStartPos, rightLength);
            Piece newPiece = new Piece(piece.getStart() + offset, text.length());

            pieces.delete(piece);
            pieces.insert(leftPiece);
            pieces.insert(newPiece);
            pieces.insert(rightPiece);

            buffer.insert(piece.getStart() + offset, text);
            updateTreeData(node, text.length(),0);

            updateSubtreeLength(leftPiece);
            updateSubtreeLength(newPiece);
            updateSubtreeLength(rightPiece);
        }
        System.out.println("Buffer after insertion: " + buffer);
        logTreeStructure();
    }

    private void appendToNode(RBTreeNode node, String text){
        Piece piece = node.getPiece();
        int newLength = piece.getLength() + text.length();
        piece.setLength(newLength);
        buffer.insert(piece.getStart() + piece.getLength(), text);
        updateTreeData(node, text.length(),0);
    }

    private void updateSubtreeLength(Piece piece) {
        RBTreeNode node = findNode(piece.getStart());
        while (node != null) {
            node.setSubtreeLength(node.getSubtreeLength() + piece.getLength());
            node = node.getParent();
        }
    }

    private void updateStartPosition(int startPosition, int textLength) {
        RBTreeNode node = findNode(startPosition);
        while (node != null) {
            if (node.getPiece().getStart() >= startPosition) {
                node.getPiece().setStart(node.getPiece().getStart() + textLength);
            }
            node = successor(node);
        }
    }


    private RBTreeNode successor(RBTreeNode node) {
        if (node.getRight() != null) {
            return minimum(node.getRight());
        }
        RBTreeNode parent = node.getParent();
        while (parent != null && node == parent.getRight()) {
            node = parent;
            parent = parent.getParent();
        }
        return parent;
    }

    private RBTreeNode minimum(RBTreeNode node) {
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }

    private RBTreeNode findNode(int position) {
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
            buildText(result, node.getLeft());
            Piece piece = node.getPiece();
            if (piece != null) {
                result.append(buffer.substring(piece.getStart(), piece.getStart() + piece.getLength()));
            }
            buildText(result, node.getRight());
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
        } else if (position < totalLength) {
            return node;
        } else if (position == totalLength && node.getRight() == null) {
            return node;
        } else if (node.getRight() != null) {
            return findNodePosition(position - totalLength, node.getRight());
        } else {
            return null;
        }
    }

    private RBTreeNode findPieceIndex(int position) {
        if (position >= buffer.length()) {
            return null;
        }
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

    void delete(int startPos, int endPos) {
        System.out.println("Delete operation:");
        System.out.println("Start position: " + startPos);
        System.out.println("End position: " + endPos);

        if (startPos < 0 || endPos > buffer.length() || startPos > endPos) {
            throw new IllegalArgumentException("Invalid DELETE range");
        }

        RBTreeNode startNode = findNode(startPos);
        RBTreeNode endNode = findNode(endPos - 1);

        if (startNode == null || endNode == null) {
            return;
        }

        Piece startPiece = startNode.getPiece();
        Piece endPiece = endNode.getPiece();

        System.out.println("Start piece: " + startPiece);
        System.out.println("End piece: " + endPiece);

        int startOffset = startPos - calculatePosition(startNode);
        int endOffset = endPos - calculatePosition(endNode);

        System.out.println("Start offset: " + startOffset);
        System.out.println("End offset: " + endOffset);

        if (startOffset < 0 || startOffset > startPiece.getLength() || endOffset < startOffset || endOffset > endPiece.getLength() + 1) {
            throw new IllegalArgumentException("Invalid offsets");
        }

        if (startPos == endPos) {
            return;
        }

        if (startPiece == endPiece) {
            int pieceStart = calculatePosition(startNode);
            int pieceEnd = pieceStart + startPiece.getLength();
            int deletedLength = Math.min(endPos, pieceEnd) - Math.max(startPos, pieceStart);
            if (deletedLength == startPiece.getLength()) {
                pieces.delete(startPiece);
                updateTreeData(startNode, -startPiece.getLength(), 0);
            } else {
                startPiece.setStart(startPiece.getStart() + (startOffset - pieceStart));
                startPiece.setLength(startPiece.getLength() - deletedLength);
                updateTreeData(startNode, -deletedLength, 0);
            }
        } else {
            if (startOffset == 0) {
                pieces.delete(startPiece);
                updateTreeData(startNode, -startPiece.getLength(), 0);
            } else {
                int deletedLength = startPiece.getLength() - startOffset;
                startPiece.setLength(startOffset);
                updateTreeData(startNode, -deletedLength, 0);
            }

            if (endOffset == endPiece.getLength()) {
                pieces.delete(endPiece);
                updateTreeData(endNode, -endPiece.getLength(), 0);
            } else {
                int deletedLength = endOffset;
                endPiece.setStart(endPiece.getStart() + deletedLength);
                endPiece.setLength(endPiece.getLength() - deletedLength);
                updateTreeData(endNode, -deletedLength, 0);
            }

            RBTreeNode node = successor(startNode);
            while (node != null && node != endNode) {
                RBTreeNode next = successor(node);
                int deletedLength = node.getPiece().getLength();
                pieces.delete(node.getPiece());
                updateTreeData(node , -deletedLength, 0);
                node = next;
            }
        }
        System.out.println("Start pos: " + startPos);
        System.out.println("End pos: " + endPos);
        System.out.println("Buffer before deletion: " + buffer);
        buffer.delete(startPos, endPos);
        updateStartPosition(endPos, startPos - endPos);
        System.out.println("Buffer after deletion: " + buffer);
    }

    protected void updateTreeData(RBTreeNode node, int lengthDiff, int lineCountDiff) {
        while (node != null) {
            node.setSubtreeLength(node.getSubtreeLength() + lengthDiff);
            node.setSubtreeLFLeft(node.getSubtreeLFLeft() + lineCountDiff);
            int sizeLeftDiff = (node.getParent() != null && node == node.getParent().getLeft()) ? lengthDiff : 0;
            node.setSizeLeft(node.getSizeLeft() + sizeLeftDiff);
            node = node.getParent();
        }
    }

}




