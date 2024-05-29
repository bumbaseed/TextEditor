package com.example.texteditorv2;

// TODO - Add comments to class, will help in future.
public class RedBlackTree {
    private RBTreeNode root;

    public RBTreeNode getRoot() {
        return root;
    }

    public void insert(Piece piece) {
        RBTreeNode newNode = new RBTreeNode(piece);
        insertNode(newNode);
    }

    public void delete(Piece piece) {
        RBTreeNode node = findNode(piece.getStart());
        if (node != null && node.getPiece().equals(piece)) {
            deleteNode(node);
        }
    }
    // x starts at root, traverses to find the appropriate position for new node.
    // y tracks parent of x. y will be parent of the new node after loop is completed
    private void insertNode(RBTreeNode node) {
        RBTreeNode y = null;
        RBTreeNode x = root;
        while (x != null) {
            y = x;
            if (node.getPiece().getStart() < x.getPiece().getStart()) {
                x = x.getLeft();
            } else {
                x = x.getRight();
            }
        }
        node.setParent(y);
        if (y == null) {
            root = node;
        } else if (node.getPiece().getStart() < y.getPiece().getStart()) {
            y.setLeft(node);
        } else {
            y.setRight(node);
        }
        node.setLeft(null);
        node.setRight(null);
        node.setColor(true);

        fixInsert(node);
    }

    private void fixInsert(RBTreeNode node) {
        while (node != null && node != root && node.getParent().isColor()) {
            if (node.getParent() == node.getParent().getParent().getLeft()) {
                RBTreeNode y = node.getParent().getParent().getRight();
                if (y != null && y.isColor()) {
                    node.getParent().setColor(false);
                    y.setColor(false);
                    node.getParent().getParent().setColor(true);
                    node = node.getParent().getParent();
                } else {
                    if (node == node.getParent().getRight()) {
                        node = node.getParent();
                        rotateLeft(node);
                    }
                    node.getParent().setColor(false);
                    node.getParent().getParent().setColor(true);
                    rotateRight(node.getParent().getParent());
                }
            } else {
                RBTreeNode y = node.getParent().getParent().getLeft();
                if (y != null && y.isColor()) {
                    node.getParent().setColor(false);
                    y.setColor(false);
                    node.getParent().getParent().setColor(true);
                    node = node.getParent().getParent();
                } else {
                    if (node == node.getParent().getLeft()) {
                        node = node.getParent();
                        rotateRight(node);
                    }
                    node.getParent().setColor(false);
                    node.getParent().getParent().setColor(true);
                    rotateLeft(node.getParent().getParent());
                }
            }
        }
        root.setColor(false);
    }

    // y is used to identify the node to be deleted or its successor
    // x is the childd of y that replaces y in the tree
    private void deleteNode(RBTreeNode node) {
        RBTreeNode y = (node.getLeft() == null || node.getRight() == null) ? node : successor(node);
        RBTreeNode x = (y.getLeft() != null) ? y.getLeft() : y.getRight();
        if (x != null) {
            x.setParent(y.getParent());
        }
        if (y.getParent() == null) {
            root = x;
        } else if (y == y.getParent().getLeft()) {
            y.getParent().setLeft(x);
        } else {
            y.getParent().setRight(x);
        }
        if (y != node) {
            node.setPiece(y.getPiece());
            node.setSubtreeLength(y.getSubtreeLength());
            node.setSubtreeLFLeft(y.getSubtreeLFLeft());


        }
        if (!y.isColor()) {
            fixDelete(x, y.getParent());
        }
    }

    // w is sibling of x. allows different operations to be performed based on the color of the child node.
    private void fixDelete(RBTreeNode node, RBTreeNode parent) {
        while (node != root && (node == null || !node.isColor())) {
            if (node == parent.getLeft()) {
                RBTreeNode w = parent.getRight();
                if (w != null && w.isColor()) {
                    w.setColor(false);
                    parent.setColor(true);
                    rotateLeft(parent);
                    w = parent.getRight();
                }
                assert w != null;
                if ((w.getLeft() == null || !w.getLeft().isColor()) && (w.getRight() == null || !w.getRight().isColor())) {
                    w.setColor(true);
                    node = parent;
                    parent = node.getParent();
                } else {
                    if (w.getRight() == null || !w.getRight().isColor()) {
                        if (w.getLeft() != null) {
                            w.getLeft().setColor(false);
                        }
                        w.setColor(true);
                        rotateRight(w);
                        w = parent.getRight();
                    }
                    w.setColor(parent.isColor());
                    parent.setColor(false);
                    if (w.getRight() != null) {
                        w.getRight().setColor(false);
                    }
                    rotateLeft(parent);
                    node = root;
                }
            } else {
                RBTreeNode w = parent.getLeft();
                if (w != null && w.isColor()) {
                    w.setColor(false);
                    parent.setColor(true);
                    rotateRight(parent);
                    w = parent.getLeft();
                }
                assert w != null;
                if ((w.getLeft() == null || !w.getLeft().isColor()) && (w.getRight() == null || !w.getRight().isColor())) {
                    w.setColor(true);
                    node = parent;
                    parent = node.getParent();
                } else {
                    if (w.getLeft() == null || !w.getLeft().isColor()) {
                        if (w.getRight() != null) {
                            w.getRight().setColor(false);
                        }
                        w.setColor(true);
                        rotateLeft(w);
                        w = parent.getLeft();
                    }
                    w.setColor(parent.isColor());
                    parent.setColor(false);
                    if (w.getLeft() != null) {
                        w.getLeft().setColor(false);
                    }
                    rotateRight(parent);
                    node = root;
                }
            }
        }
        if (node != null) {
            node.setColor(false);
        }
    }

    private void updateNodeMetadata(RBTreeNode node, int lengthDiff, int lineFeedCntDiff) {
        while (node != null) {
            node.setSubtreeLength(node.getSubtreeLength() + lengthDiff);
            node.setSubtreeLFLeft(node.getSubtreeLFLeft() + lineFeedCntDiff);
            int sizeLeftDiff = 0;
            node.setSizeLeft(node.getSizeLeft() + sizeLeftDiff);
            node = node.getParent();
        }
    }
        private void rotateLeft(RBTreeNode node) {
        RBTreeNode y = node.getRight();
        node.setRight(y.getLeft());
        if (y.getLeft() != null) {
            y.getLeft().setParent(node);
        }
        y.setParent(node.getParent());
        if (node.getParent() == null) {
            root = y;
        } else if (node == node.getParent().getLeft()) {
            node.getParent().setLeft(y);
        } else {
            node.getParent().setRight(y);
        }
        y.setLeft(node);
        node.setParent(y);
    }

    private void rotateRight(RBTreeNode node) {
        RBTreeNode y = node.getLeft();
        node.setLeft(y.getRight());
        if (y.getRight() != null) {
            y.getRight().setParent(node);
        }
        y.setParent(node.getParent());
        if (node.getParent() == null) {
            root = y;
        } else if (node == node.getParent().getRight()) {
            node.getParent().setRight(y);
        } else {
            node.getParent().setLeft(y);
        }
        y.setRight(node);
        node.setParent(y);
    }

    public RBTreeNode findNode(int start) {
        RBTreeNode current = root;
        while (current != null) {
            if (start < current.getPiece().getStart()) {
                current = current.getLeft();
            } else if (start > current.getPiece().getStart()) {
                current = current.getRight();
            } else {
                return current;
            }
        }
        return null;
    }

    private RBTreeNode successor(RBTreeNode node) {
        if (node.getRight() != null) {
            return findMinimum(node.getRight());
        }
        RBTreeNode parent = node.getParent();
        while (parent != null && node == parent.getRight()) {
            node = parent;
            parent = parent.getParent();
        }
        return parent;
    }

    private RBTreeNode findMinimum(RBTreeNode node) {
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }
}





