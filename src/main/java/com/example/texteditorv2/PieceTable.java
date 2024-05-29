package com.example.texteditorv2;


class PieceTable {
    final int averageBufferSize = 65535; // vs code buffer size
    private StringBuilder buffer;
    private RedBlackTree pieces;

    PieceTable() {
        buffer = new StringBuilder();
        pieces = new RedBlackTree();
        int lineCount = 1;
        int length = 0;
        System.out.println("Initialized PieceTable");
    }


    void insert(int offset, String value) {
        if (value.isEmpty()) return;

        if (buffer.length() == 0) {
            Piece newPiece = new Piece(0, value.length());
            pieces.insert(newPiece);
            buffer.append(value);
            return;
        }

        System.out.println("Insert called with offset: " + offset + ", value: " + value);

        NodePosition nodePos = nodeAt(offset);
        if (nodePos == null) {
            int startPos = buffer.length();
            Piece newPiece = new Piece(startPos, value.length());
            pieces.insert(newPiece);
            buffer.append(value);
            System.out.println("Inserted new piece at the end: " + newPiece);
            return;
        }

        Piece piece = nodePos.node.getPiece();
        int nodeOffset = offset - nodePos.nodeStartOffset;

        if (nodeOffset == piece.getLength() && nodePos.node == pieces.getRoot() && value.length() < averageBufferSize) {
            appendToNode(nodePos.node, value);
            return;
        }

        if (nodeOffset == 0) {
            insertContentToNodeLeft(value, nodePos.node);
        } else if (nodeOffset == piece.getLength()) {
            insertContentToNodeRight(value, nodePos.node);
        } else {
            splitNode(nodePos.node, nodeOffset, value);
        }

        System.out.println("Buffer after insertion: " + buffer);
    }

    private void appendToNode(RBTreeNode node, String value) {
        Piece piece = node.getPiece();
        int newLength = piece.getLength() + value.length();
        piece.setLength(newLength);
        buffer.insert(piece.getStart() + piece.getLength() - value.length(), value);
        updateTreeMetadata(node, value.length(), 0);
    }

    private void insertContentToNodeLeft(String value, RBTreeNode node) {
        int startPos = node.getPiece().getStart();
        Piece newPiece = new Piece(startPos, value.length());
        pieces.insert(newPiece);
        buffer.insert(startPos, value);
        updateTreeMetadata(node, value.length(), 0);
    }

    private void insertContentToNodeRight(String value, RBTreeNode node) {
        int startPos = node.getPiece().getStart() + node.getPiece().getLength();
        Piece newPiece = new Piece(startPos, value.length());
        pieces.insert(newPiece);
        buffer.insert(startPos, value);
        updateTreeMetadata(node, value.length(), 0);
    }

    private void splitNode(RBTreeNode node, int offset, String value) {
        Piece piece = node.getPiece();
        int leftLength = offset;
        int rightLength = piece.getLength() - offset;

        Piece leftPiece = new Piece(piece.getStart(), leftLength);
        Piece rightPiece = new Piece(piece.getStart() + offset + value.length(), rightLength);
        Piece newPiece = new Piece(piece.getStart() + offset, value.length());

        pieces.delete(piece);
        pieces.insert(leftPiece);
        pieces.insert(newPiece);
        pieces.insert(rightPiece);

        buffer.insert(piece.getStart() + offset, value);
        updateTreeMetadata(node, value.length(), 0);
    }

    void delete(int startPos, int endPos) {
        System.out.println("Delete operation:");
        System.out.println("Start position: " + startPos);
        System.out.println("End position: " + endPos);

        if (startPos < 0 || endPos > buffer.length() || startPos > endPos) {
            throw new IllegalArgumentException("Invalid DELETE range");
        }

        NodePosition startNode = nodeAt(startPos);
        NodePosition endNode = nodeAt(endPos);

        if (startNode == null || endNode == null) {
            return;
        }

        Piece startPiece = startNode.node.getPiece();
        Piece endPiece = endNode.node.getPiece();

        int startOffset = startPos - startNode.nodeStartOffset;
        int endOffset = endPos - endNode.nodeStartOffset;

        if (startPiece == endPiece) {
            int deletedLength = endOffset - startOffset;
            if (deletedLength == startPiece.getLength()) {
                pieces.delete(startPiece);
                updateTreeMetadata(startNode.node, -startPiece.getLength(), 0);
            } else {
                startPiece.setStart(startPiece.getStart() + startOffset);
                startPiece.setLength(startPiece.getLength() - deletedLength);
                updateTreeMetadata(startNode.node, -deletedLength, 0);
            }
        } else {
            if (startOffset == 0) {
                pieces.delete(startPiece);
                updateTreeMetadata(startNode.node, -startPiece.getLength(), 0);
            } else {
                startPiece.setLength(startOffset);
                updateTreeMetadata(startNode.node, startOffset - startPiece.getLength(), 0);
            }

            if (endOffset == endPiece.getLength()) {
                pieces.delete(endPiece);
                updateTreeMetadata(endNode.node, -endPiece.getLength(), 0);
            } else {
                endPiece.setStart(endPiece.getStart() + endOffset);
                endPiece.setLength(endPiece.getLength() - endOffset);
                updateTreeMetadata(endNode.node, -endOffset, 0);
            }

            RBTreeNode node = startNode.node.getRight();
            while (node != null && node != endNode.node) {
                pieces.delete(node.getPiece());
                updateTreeMetadata(node, -node.getPiece().getLength(), 0);
                node = node.getRight();
            }
        }

        int deletedLength = endPos - startPos;
        buffer.delete(startPos, endPos);
        updateTreeMetadata(endNode.node, -deletedLength, 0);
        System.out.println("Buffer after delete operation: " + buffer);
    }

    private void updateTreeMetadata(RBTreeNode node, int lengthDiff, int lineCountDiff) {
        while (node != null) {
            node.setSubtreeLength(node.getSubtreeLength() + lengthDiff);
            node.setSubtreeLFLeft(node.getSubtreeLFLeft() + lineCountDiff);
            if (node.getParent() != null && node == node.getParent().getLeft()) {
                node.getParent().setSizeLeft(node.getParent().getSizeLeft() + lengthDiff);
            }
            node = node.getParent();
        }
    }

    private NodePosition nodeAt(int offset) {
        RBTreeNode node = pieces.getRoot();
        int nodeStartOffset = 0;

        while (node != null) {
            if (node.getSizeLeft() > offset) {
                node = node.getLeft();
            } else if (node.getSizeLeft() + node.getPiece().getLength() >= offset) {
                return new NodePosition(node, offset - node.getSizeLeft(), nodeStartOffset + node.getSizeLeft());
            } else {
                offset -= node.getSizeLeft() + node.getPiece().getLength();
                nodeStartOffset += node.getSizeLeft() + node.getPiece().getLength();
                node = node.getRight();
            }
        }
        return null;
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

    void loadContent(String content) {
        // Clear the existing piece table and buffer to reset the editor
        pieces = new RedBlackTree();
        buffer = new StringBuilder();

        // Split the loaded content into chunks for loading efficiency
        int chunkSize = 1024 * 1024; // 1MB chunk size. can be scaled to handle larger files in future
        int contentLength = content.length();
        int offset = 0;

        while (offset < contentLength) {
            int length = Math.min(chunkSize, contentLength - offset);
            String chunk = content.substring(offset, offset + length);

            // Insert each character in the chunk as a separate piece to maintain current piece structure.
            for (int i = 0; i < chunk.length(); i++) {
                insert(offset + i, String.valueOf(chunk.charAt(i)));
            }
            offset += length;
        }
        System.out.println("Content loaded successfully.");
    }
}




