package org.msdemt;

import org.msdemt.printer.BinaryTreeInfo;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 继承BinaryTreeInfo是为了实现打印的功能
 *
 * @param <E>
 */
public class BinarySearchTree<E> implements BinaryTreeInfo {
    private int size;
    private Node<E> root;
    private Comparator<E> comparator;

    private static class Node<E> {
        E element;
        Node<E> left;
        Node<E> right;
        Node<E> parent;

        public Node(E element, Node<E> parent) {
            this.element = element;
            this.parent = parent;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        public boolean hasTwoChildren() {
            return left != null && right != null;
        }
    }

    public BinarySearchTree() {
        this(null);
    }

    public BinarySearchTree(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    public void add(E element) {
        elementNotNullCheck(element);

        //添加第一个节点
        if (root == null) {
            root = new Node<>(element, null);
            size++;
            return;
        }

        //添加的不是第一个节点
        //找到父节点
        Node<E> parent = root;
        Node<E> node = root;
        int cmp = 0;
        do {
            cmp = compare(element, node.element);
            //保存父节点
            parent = node;
            if (cmp > 0) {
                node = node.right;
            } else if (cmp < 0) {
                node = node.left;
            } else {
                node.element = element;
                return;
            }
        } while (node != null);

        //看看插入到父节点的哪个位置
        Node<E> newNode = new Node<>(element, parent);
        if (cmp > 0) {
            parent.right = newNode;
        } else {
            parent.left = newNode;
        }
        size++;
    }

    public boolean contains(E element) {
        return node(element) != null;
    }

    public void remove(E element) {
        remove(node(element));
    }

    private void remove(Node<E> node) {
        if (node == null) {
            return;
        }

        size--;

        //度为2的节点
        if (node.hasTwoChildren()) {
            //找到后继节点
            Node<E> s = successor(node);
            //用后继节点的值覆盖度为2的节点的值，前驱或后继节点的度只能为0或1
            node.element = s.element;
            //删除后继节点，将后继节点s赋给node,在后续的流程中删除该度为0或1的节点
            node = s;
        }

        //删除node节点（此时node的度为1或者0）
        Node<E> replacement = node.left != null ? node.left : node.right;

        if (replacement != null) {
            //node是度为1的节点
            //更改parent
            replacement.parent = node.parent;
            //更改parent的left、right指向
            if (node.parent == null) {
                //node是度为1的节点并且是根节点
                root = replacement;
            } else if (node == node.parent.left) {
                node.parent.left = replacement;
            } else {
                //此时的情况为 node == node.parent.right
                node.parent.right = replacement;
            }
        } else if (node.parent == null) {
            //node是叶子节点并且是根节点
            root = null;
        } else {
            //node是叶子节点，但不是根节点
            if (node == node.parent.left) {
                node.parent.left = null;
            } else {
                //此时的情况为 node == node.parent.right
                node.parent.right = null;
            }
        }

    }

    /**
     * 查找node的前驱节点
     * <p>
     * 前驱节点：中序遍历时的前一个节点
     * 如果是二叉搜索树，前驱节点就是前一个比它小的节点
     * 判断条件：
     * 1. 如果node的左节点不为空，则node的前驱节点为node的左子树的右节点的右节点的右节点。。。直到右节点为空
     * predecessor = node.left.right.right.right...  终止条件 right为null
     * 2. 如果node的左节点为空，父节点不为空，则node的前驱节点为node的父节点的父节点的父节点。。。直到node在父节点的右子树中
     * predecessor = node.parent.parent.parent...  终止条件 node在parent的右子树中
     * 3. 如果node的左子树为空且父节点为空，则该node没有前驱节点，例如没有左子树的根节点
     *
     * @param node
     * @return
     */
    private Node<E> predecessor(Node<E> node) {
        if (node == null) {
            return null;
        }
        //前驱节点在左子树中（left.right.right.right...）
        Node<E> p = node.left;
        if (p != null) {
            while (p.right != null) {
                p = p.right;
            }
            return p;
        }

        //node的左子树为空
        //从父节点、祖父节点中寻找前驱节点
        while (node.parent != null && node == node.parent.left) {
            node = node.parent;
        }
        //上面循环结束时的情况如下
        //node.parent == null 或者 node == node.parent.right
        //此时node.parent为前驱节点
        return node.parent;

    }

    /**
     * 查找node的后继节点
     * <p>
     * 后继节点：中序遍历时的后一个节点
     * 如果是二叉搜索树，后继节点就是后一个比它大的节点
     * 判断条件
     * 1. 如果node的右节点不为空，则node的后继节点为node的右子树的左节点的左节点的左节点。。。直到左节点为空
     * successor = node.right.left.left.left... 终止条件 left为null
     * 2. 如果node的右节点为空，父节点不为空，则node的后继节点为node的父节点的父节点的父节点。。。直到node在父节点的左子树中
     * successor = node.parent.parent.parent... 终止条件 node在parent的左子树中
     * 3. 如果node的右子树为空且父节点为空，则该node没有前驱节点，例如没有右子树的根节点
     *
     * @param node
     * @return
     */
    private Node<E> successor(Node<E> node) {
        if (node == null) {
            return null;
        }

        //后继节点在右子树中（right.left.left.left...）
        Node<E> p = node.right;
        if (p != null) {
            while (p.left != null) {
                p = p.left;
            }
            return p;
        }

        //从父节点、祖父节点中寻找前驱节点
        while (node.parent != null && node == node.parent.right) {
            node = node.parent;
        }

        return node.parent;
    }

    /**
     * 根据元素找到node
     *
     * @param element
     * @return
     */
    private Node<E> node(E element) {
        Node<E> node = root;
        while (node != null) {
            int cmp = compare(element, node.element);
            if (cmp == 0) {
                return node;
            } else if (cmp > 0) {
                node = node.right;
            } else {
                node = node.right;
            }
        }
        return null;
    }

    /**
     * 返回值等于0，表示e1和e2相等；返回值大于0，表示e1大于e2；返回值小于0，表示e1小于e2
     *
     * @param e1
     * @param e2
     * @return
     */
    private int compare(E e1, E e2) {
        if (comparator != null) {
            return comparator.compare(e1, e2);
        }
        return ((Comparable<E>) e1).compareTo(e2);
    }

    private void elementNotNullCheck(E element) {
        if (element == null) {
            throw new IllegalArgumentException("element must not be null");
        }
    }
    //
    ///**
    // * 使用递归实现前序遍历
    // * 前序遍历访问顺序：根节点、前序遍历左子树、前序遍历右子树
    // */
    //public void preorderTraversal() {
    //    preorderTraversal(root);
    //}
    //
    //private void preorderTraversal(Node<E> node) {
    //    if (node == null) {
    //        return;
    //    }
    //    System.out.println(node.element);
    //    preorderTraversal(node.left);
    //    preorderTraversal(node.right);
    //}
    //
    ///**
    // * 使用递归实现中序遍历
    // * 中序遍历访问顺序：中序遍历左子树、根节点、中序遍历右子树
    // */
    //public void inorderTraversal() {
    //    inorderTraversal(root);
    //}
    //
    //private void inorderTraversal(Node<E> node) {
    //    if (node == null) {
    //        return;
    //    }
    //    inorderTraversal(node.left);
    //    System.out.println(node.element);
    //    inorderTraversal(node.right);
    //}
    //
    ///**
    // * 使用递归实现后续遍历
    // * 后续遍历访问顺序：后续遍历左子树、后续遍历右子树、根节点
    // */
    //public void postorderTraversal() {
    //    postorderTraversal(root);
    //}
    //
    //private void postorderTraversal(Node<E> node) {
    //    if (node == null) {
    //        return;
    //    }
    //    postorderTraversal(node.left);
    //    postorderTraversal(node.right);
    //    System.out.println(node.element);
    //}
    //
    ///**
    // * 使用队列Queue实现层序遍历
    // * 希望能默写出来
    // */
    //public void levelOrderTraversal() {
    //    if (root == null) {
    //        return;
    //    }
    //
    //    Queue<Node<E>> queue = new LinkedList<>();
    //    queue.offer(root);
    //
    //    while (!queue.isEmpty()) {
    //        Node<E> node = queue.poll();
    //        System.out.println(node.element);
    //
    //        if (node.left != null) {
    //            queue.offer(node.left);
    //        }
    //
    //        if (node.right != null) {
    //            queue.offer(node.right);
    //        }
    //    }
    //}

    /**
     * 递归实现前序遍历，支持外界遍历元素时自定义操作 visitor
     * 增强遍历接口，支持遍历到指定节点后停止 visitor.stop
     *
     * @param visitor
     */
    public void preorder(Visitor<E> visitor) {
        if (visitor == null) {
            return;
        }
        preorder(root, visitor);
    }

    private void preorder(Node<E> node, Visitor<E> visitor) {
        if (node == null || visitor.stop) {
            return;
        }
        //if (visitor.stop) {
        //    return;
        //}
        visitor.stop = visitor.visit(node.element);
        preorder(node.left, visitor);
        preorder(node.right, visitor);
    }

    public void inorder(Visitor<E> visitor) {
        if (visitor == null) {
            return;
        }
        inorder(root, visitor);
    }

    private void inorder(Node<E> node, Visitor<E> visitor) {
        if (node == null || visitor.stop) {
            return;
        }

        inorder(node.left, visitor);
        if (visitor.stop) {
            return;
        }
        visitor.stop = visitor.visit(node.element);
        inorder(node.right, visitor);
    }

    public void postorder(Visitor<E> visitor) {
        if (visitor == null) {
            return;
        }
        postorder(root, visitor);
    }

    private void postorder(Node<E> node, Visitor<E> visitor) {
        if (node == null || visitor.stop) {
            return;
        }
        postorder(node.left, visitor);
        postorder(node.right, visitor);
        if (visitor.stop) {
            return;
        }
        visitor.stop = visitor.visit(node.element);
    }

    public void levelOrder(Visitor<E> visitor) {
        if (root == null || visitor == null) {
            return;
        }

        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            if (visitor.visit(node.element)) {
                //遍历到指定数据返回true，停止遍历
                return;
            }
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
    }

    public static abstract class Visitor<E> {
        boolean stop;

        /**
         * 如果返回true，表示停止遍历
         *
         * @param element
         * @return
         */
        public abstract boolean visit(E element);
    }

    /**
     * 获取树的高度
     *
     * @return
     */
    public int height() {
        if (root == null) {
            return 0;
        }

        //树的高度
        int height = 0;
        //存储每一层的元素数量
        int levelSize = 1;
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            levelSize--;

            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }

            if (levelSize == 0) {
                //意味着即将要访问下一层
                levelSize = queue.size();
                height++;
            }
        }
        return height;
    }

    /**
     * 获取树的高度
     * 方法二，使用递归方式
     *
     * @return
     */
    public int height2() {
        return height(root);
    }

    private int height(Node<E> node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    /**
     * 判断是否为完全二叉树
     * <p>
     * 如果树为空，返回false
     * 如果树不为空，开始层序遍历二叉树（用队列）
     * - 如果 node.left != null && node.right != null，将 node.left、node.right按顺序入队
     * - 如果 node.left == null && node.right != null，返回 false
     * - 如果 node.left != null && node.right == null 或者 node.left == null && node.right == null ，则后面的节点都必须为叶子节点
     *
     * @return
     */
    public boolean isComplete() {
        if (root == null) {
            return false;
        }

        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);

        boolean leaf = false;

        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            if (leaf && !node.isLeaf()) {
                return false;
            }

            if (node.left != null && node.right != null) {
                queue.offer(node.left);
                queue.offer(node.right);
            } else if (node.left == null && node.right != null) {
                return false;
            } else {
                //后面遍历的节点都必须是叶子节点
                leaf = true;
                if (node.left != null) {
                    queue.offer(node.left);
                }
            }
        }
        return true;
    }

    /**
     * 判断是否为完全二叉树，较于上面的isComplete()方法可以减少重复判断
     * <p>
     * 如果树为空，返回false
     * 如果树不为空，开始层序遍历二叉树（用队列）
     * - 如果 node.left != null ，将 node.left 入队
     * - 如果 node.left == null && node.right != null，返回false
     * - 如果 node.right != null ，将 node.right 入队
     * - 如果 node.right == null，那么后面遍历的节点应该都为叶子节点，才是完全二叉树，否则返回false
     * - 遍历结束，返回true
     *
     * @return
     */
    public boolean isComplete2() {
        if (root == null) {
            return false;
        }

        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);

        boolean leaf = false;
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            if (leaf && !node.isLeaf()) {
                return false;
            }

            if (node.left != null) {
                queue.offer(node.left);
            } else if (node.right != null) {
                //此时的情况为 node.left == null && node.right != null
                return false;
            }

            if (node.right != null) {
                queue.offer(node.right);
            } else {
                //此时的情况为 node.right == null
                leaf = true;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toString(root, sb, "");
        return sb.toString();
    }

    private void toString(Node<E> node, StringBuilder sb, String prefix) {
        if (node == null) {
            return;
        }

        toString(node.left, sb, prefix + "L---");
        sb.append(prefix).append(node.element).append("\n");
        toString(node.right, sb, prefix + "R---");
    }

    @Override
    public Object root() {
        return root;
    }

    @Override
    public Object left(Object node) {
        return ((Node<E>) node).left;
    }

    @Override
    public Object right(Object node) {
        return ((Node<E>) node).right;
    }

    @Override
    public Object string(Object node) {
        Node<E> myNode = (Node<E>) node;
        String parentString = "null";
        if (myNode.parent != null) {
            parentString = myNode.parent.element.toString();
        }
        return myNode.element + "_P(" + parentString + ")";
    }
}
