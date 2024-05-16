package com.example.texteditorv2;

public class RedBlackTree {
    private RBTreeNode root;

    RedBlackTree(){
        root = null;
    }

    // Insert the piece / text into the tree structure. root is always black
    void insert(Piece piece){
        RBTreeNode node = new RBTreeNode(piece);

        if (root == null){
            root = node;
            root.color = false;
        } else {
            insertRec(root, node);
        }
        fixInsert(node);
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
        // TODO - Fix the R/B properties after text insertion - Depth, violations of leaves etc.
        RBTreeNode parent, grandParent, uncle; // tree hierarchy minus children / leaves

        while (node != root && node.color && node.parent.color){
            parent = node.parent;
            grandParent = parent.parent;

            if (parent == grandParent.left) {
                uncle = grandParent.right;
                if (uncle != null && uncle.color){
                    grandParent.color = true;
                    parent.color = false;
                    uncle.color = false;
                    node = grandParent;
                } else {
                    if (node == parent.right) {
                        rotateLeft(parent);
                        node = parent;
                        parent = node.parent;
                    }
                    rotateRight(grandParent);
                    boolean tempColor = parent.color;
                    parent.color = grandParent.color;
                    grandParent.color = tempColor;
                    node = parent;
                }
            } else {
                uncle = grandParent.left;
                if (uncle != null && node.color){
                    grandParent.color = true; // red
                    parent.color = false; // black
                    uncle.color = false;
                    node = grandParent;
                } else {
                    if (node == parent.left){
                        rotateRight(parent);
                        node = parent;
                        parent = node.parent;
                    }
                    rotateLeft(grandParent);
                    boolean tempColor = parent.color;
                    parent.color = grandParent.color;
                    grandParent.color = tempColor;
                    node = parent;
                }
            }
        }
        root.color = false;
    }

// update to accurately track changes
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

    // Rotation functions to help with fixing / balancing the tree

    private void rotateLeft(RBTreeNode node){
        RBTreeNode temp = node.right;
        node.right = temp.left;

        if (temp.left != null) {
            temp.left.parent = node;
        }
        temp.parent = node.parent;

        if (node.parent == null){
            root = temp;
        } else if (node == node.parent.left) {
            node.parent.left = temp;
        } else {
            node.parent.right = temp;
        }
        temp.left = node;
        node.parent = temp;
    }

    private void rotateRight(RBTreeNode node){
        RBTreeNode temp = node.left;
        node.left = temp.right;

        if (temp.right != null) {
            temp.right.parent = node;
        }
        temp.parent = node.parent;

        if (node.parent == null) {
            root = temp;
        } else if (node == node.parent.right) {
            node.parent.right = temp;
        } else {
            node.parent.left = temp;
        }
        temp.right = node;
        node.parent = temp;
    }




}
