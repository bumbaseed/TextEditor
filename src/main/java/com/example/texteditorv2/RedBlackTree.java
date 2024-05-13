package com.example.texteditorv2;

public class RedBlackTree {
    private RBTreeNode root;

    RedBlackTree(){
        root = null;
    }


    void insert(Piece newPiece){
        RBTreeNode newNode = new RBTreeNode(newPiece);

        if (root == null) {
            root = newNode;
        }else {
            insertRec(root, newNode);
        }
        fixInsert(newNode);
    }

    private void insertRec(RBTreeNode current, RBTreeNode newNode) {
        // TODO - Recursion logic for text insertion.
        current.subtreeLength += newNode.piece.length;

        if (newNode.piece.start < current.piece.start) {
            if (current.left == null){
                current.left = newNode;
                newNode.parent = current;
            } else {
                insertRec(current.left, newNode);
            }

        } else {
            if (current.right == null){
                current.right = newNode;
                newNode.parent = current;
            } else {
                insertRec(current.right, newNode);
            }
        }
    }

    protected Piece get(int index){
        RBTreeNode node = findNode(root, index);
        return node == null ? null : node.piece;
    }

    // TODO -  Requires logic for determining the position in the sorted sequence of nodes.
    private RBTreeNode findNode(RBTreeNode node, int index){
        if (node == null) return null;

        int leftLength = (node.left != null) ? node.left.subtreeLength : 0;

        if (index < leftLength) {
            return findNode(node.left, index);
        } else if (index < leftLength + node.piece.length) {
            return node;
        } else {
            return findNode(node.right, index - (leftLength + node.piece.length));
        }

    }

    RBTreeNode getRoot(){
        return root;
    }

    private void fixInsert(RBTreeNode node) {
        // TODO - Fix the R/B properties after text insertion.
    }


    private void updateSubtreeTextLength(RBTreeNode node) {
        if (node != null){
            node.subtreeLength = node.piece.length;
            if (node.left != null) {
                node.subtreeLength += node.left.subtreeLength;
            } else if (node.right != null) {
                node.subtreeLength += node.right.subtreeLength;
            } else if (node.parent != null) {
                updateSubtreeTextLength(node.parent);
            }
        }
    }





}
