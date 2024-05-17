package com.example.texteditorv2;

public class RedBlackTree {
    private RBTreeNode root;

    RedBlackTree(){
        root = null;
    }

    void insert(Piece piece){
        RBTreeNode node = new RBTreeNode(piece);
        System.out.println("Inserting piece: " + piece);

        if (root == null){
            root = node;
            root.setColor(false);
            System.out.println("Inserted root node: " + node);
        } else {
            insertRec(root, node);
            fixInsert(node);
            updateSubtreeTextLength(node);
        }
        logTreeStructure(); // Log tree structure after insertion
    }

    private void insertRec(RBTreeNode current, RBTreeNode newNode) {
        current.setSubtreeLength(current.getSubtreeLength() + newNode.getPiece().getLength());

        if (newNode.getPiece().getStart() < current.getPiece().getStart()) {
            if (current.getLeft() == null){
                current.setLeft(newNode);
                newNode.setParent(current);
                System.out.println("Inserted left node: " + newNode + " under parent: " + current);
            } else {
                insertRec(current.getLeft(), newNode);
            }
        } else {
            if (current.getRight() == null){
                current.setRight(newNode);
                newNode.setParent(current);
                System.out.println("Inserted right node: " + newNode + " under parent: " + current);
            } else {
                insertRec(current.getRight(), newNode);
            }
        }
    }


    private RBTreeNode findNode(RBTreeNode node, int index) {
        if (node == null) return null;

        int leftLength = (node.getLeft() != null) ? node.getLeft().getSubtreeLength() : 0;
        int totalLength = leftLength + node.getPiece().getLength();

        if (index < leftLength) {
            return findNode(node.getLeft(), index);
        } else if (index < totalLength) {
            return node;
        } else if (node.getRight() != null) {
            return findNode(node.getRight(), index - totalLength);
        } else {
            return null;
        }
    }

    RBTreeNode getRoot(){
        return root;
    }

    private void fixInsert(RBTreeNode node) {
        RBTreeNode parent, grandParent, uncle;

        while (node != root && node.isColor() && node.getParent().isColor()) {
            parent = node.getParent();
            grandParent = parent.getParent();

            if (parent == grandParent.getLeft()) {
                uncle = grandParent.getRight();
                if (uncle != null && uncle.isColor()){
                    grandParent.setColor(true);
                    parent.setColor(false);
                    uncle.setColor(false);
                    node = grandParent;
                } else {
                    if (node == parent.getRight()) {
                        rotateLeft(parent);
                        node = parent;
                        parent = node.getParent();
                    }
                    rotateRight(grandParent);
                    boolean tempColor = parent.isColor();
                    parent.setColor(grandParent.isColor());
                    grandParent.setColor(tempColor);
                    node = parent;
                }
            } else {
                uncle = grandParent.getLeft();
                if (uncle != null && uncle.isColor()){
                    grandParent.setColor(true);
                    parent.setColor(false);
                    uncle.setColor(false);
                    node = grandParent;
                } else {
                    if (node == parent.getLeft()){
                        rotateRight(parent);
                        node = parent;
                        parent = node.getParent();
                    }
                    rotateLeft(grandParent);
                    boolean tempColor = parent.isColor();
                    parent.setColor(grandParent.isColor());
                    grandParent.setColor(tempColor);
                    node = parent;
                }
            }
        }
        root.setColor(false);
    }

    private void updateSubtreeTextLength(RBTreeNode node) {
        if (node != null) {
            node.setSubtreeLength(node.getPiece().getLength());
            if (node.getLeft() != null) {
                node.setSubtreeLength(node.getSubtreeLength() + node.getLeft().getSubtreeLength());
            }
            if (node.getRight() != null) {
                node.setSubtreeLength(node.getSubtreeLength() + node.getRight().getSubtreeLength());
            }
            if (node.getParent() != null) {
                updateSubtreeTextLength(node.getParent());
            }
        }
    }

    private void rotateLeft(RBTreeNode node) {
        RBTreeNode temp = node.getRight();
        node.setRight(temp.getLeft());

        if (temp.getLeft() != null) {
            temp.getLeft().setParent(node);
        }
        temp.setParent(node.getParent());

        if (node.getParent() == null) {
            root = temp;
        } else if (node == node.getParent().getLeft()) {
            node.getParent().setLeft(temp);
        } else {
            node.getParent().setRight(temp);
        }
        temp.setLeft(node);
        node.setParent(temp);

        updateSubtreeTextLength(node);
        updateSubtreeTextLength(temp);
    }

    private void rotateRight(RBTreeNode node) {
        RBTreeNode temp = node.getLeft();
        node.setLeft(temp.getRight());

        if (temp.getRight() != null) {
            temp.getRight().setParent(node);
        }
        temp.setParent(node.getParent());

        if (node.getParent() == null) {
            root = temp;
        } else if (node == node.getParent().getRight()) {
            node.getParent().setRight(temp);
        } else {
            node.getParent().setLeft(temp);
        }
        temp.setRight(node);
        node.setParent(temp);

        updateSubtreeTextLength(node);
        updateSubtreeTextLength(temp);
    }

    void delete(Piece piece) {
        RBTreeNode node = findNode(root, piece.getStart());
        if (node != null && node.getPiece().equals(piece)) {
            delete(node.getPiece());
        }
    }

    private void deleteNode(RBTreeNode node){
        if (node.getLeft() != null && node.getRight() != null){
            RBTreeNode successor = findMinimum(node.getRight());
            node.setPiece(successor.getPiece());
            deleteNode(successor);
        }else {
            RBTreeNode child = (node.getLeft() != null) ? node.getLeft() : node.getRight();
            if (child != null){
                child.setParent(node.getParent());
            }
            if (node.getParent() != null) {
                root = child;
            } else if (node == node.getParent().getLeft()) {
                node.getParent().setLeft(child);
            } else {
                node.getParent().setRight(child );
            }
            if (node.isColor() == false){
                fixDelete(child);
            }
        }
    }

    private void fixDelete(RBTreeNode node){
        while(node != root && (node == null || node.isColor() == false)){
            if (node == node.getParent().getLeft()){
                RBTreeNode sibling = node.getParent().getRight();
                if (sibling.isColor()){
                    sibling.setColor(false);
                    node.getParent().setColor(true);
                    rotateLeft(node.getParent());
                    sibling = node.getParent().getRight();
                }
                if ((sibling.getLeft() == null || sibling.getLeft().isColor() == false) && (sibling.getRight() == null || sibling.getRight().isColor() == false)) {
                    sibling.setColor(true);
                    node = node.getParent();
                }else {
                    if (sibling.getRight() == null || sibling.getRight().isColor() == false){
                        sibling.getLeft().setColor(false);
                        sibling.setColor(true);
                        rotateRight(sibling);
                        sibling = node.getParent().getRight();
                    }
                    sibling.setColor(node.getParent().isColor());
                    node.getParent().setColor(false);
                    sibling.getRight().setColor(false);
                    rotateLeft(node.getParent());
                    node = root;
                }
            } else {
                if (node == node.getParent().getRight()) {
                    RBTreeNode sibling = node.getParent().getLeft();
                    if (sibling.isColor()){
                        sibling.setColor(false);
                        node.getParent().setColor(true);
                        rotateRight(node.getParent());
                        sibling = node.getParent().getLeft();
                    }
                    if ((sibling.getRight() == null || sibling.getRight().isColor() == false) && (sibling.getLeft() == null ||  sibling.getLeft().isColor() == false)) {
                        sibling.setColor(true);
                        node = node.getParent();
                    } else {
                        if (sibling.getLeft() == null || sibling.getLeft().isColor() == false) {
                            sibling.getRight().setColor(false);
                            sibling.setColor(true);
                            rotateLeft(sibling);
                            sibling = node.getParent().getLeft();
                        }
                        sibling.setColor(node.getParent().isColor());
                        node.getParent().setColor(false);
                        sibling.getLeft().setColor(false);
                        rotateRight(node.getParent());
                        node = root;

                    }
                }
            }

        }
        if (node != null){
            node.setColor(false);
        }
    }


    private RBTreeNode findMinimum(RBTreeNode node){
        while (node.getLeft() != null){
            node = node.getLeft();
        }
        return node;
    }

    private void logTreeStructure() {
        System.out.println("Current tree structure:");
        logTreeNode(root, 0);
    }

    private void logTreeNode(RBTreeNode node, int depth) {
        if (node == null) return;
        logTreeNode(node.getLeft(), depth + 1);
        System.out.println("Depth: " + depth + ", Piece: " + node.getPiece() + ", Subtree Length: " + node.getSubtreeLength());
        logTreeNode(node.getRight(), depth + 1);
    }
}


