package com.example.texteditorv2;
    class NodePosition {
        RBTreeNode node;
        int remainder;
        int nodeStartOffset;

    NodePosition(RBTreeNode node, int remainder, int nodeStartOffset) {
        this.node = node;
        this.remainder = remainder;
        this.nodeStartOffset = nodeStartOffset;
    }
}
