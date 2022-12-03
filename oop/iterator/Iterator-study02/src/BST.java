import java.util.ArrayList;
import java.util.Iterator;

class TreeNode<T>{
    T key;
    TreeNode<T> left = null;
    TreeNode<T> right = null;

    public TreeNode(T key){
        this.key = key;
    }

    @Override
    public String toString() {
        return key.toString();
    }
}

public class BST <T extends Comparable<T>> implements Iterable<T> {
    @Override
    public Iterator<T> iterator() {
        if(currentType!=TraversalType.BFS){
            ArrayList<T> list = new ArrayList<>();
            switch (currentType){
                case BFS:
                    preorder(root, list);
                    break;
                case PREORDER:
                    postorder(root, list);
                    break;
                case POSTORDER:
                    postorder(root, list);
                    break;
                default:
                    inorder(root, list);
                    break;
            }
            return list.iterator();
        }
        else return new BST_BFS_Iterator<>(root);
    }

    public enum TraversalType {PREORDER, INORDER, POSTORDER, BFS};
    private TreeNode<T> root = null;
    private int size = 0;
    private TraversalType currentType = TraversalType.INORDER;

    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public boolean isFull(){
        return false;
    }

    public void setTraversalType(TraversalType traversalType){
        currentType = traversalType;
    }

    public void add(T key){
        if(isEmpty()) root = new TreeNode<T>(key);
        else {
            TreeNode<T> parentNode = findNode(root, key);
            if(parentNode.key.equals(key)) return;
            else if(parentNode.key.compareTo(key) > 0)
                parentNode.left = new TreeNode<>(key);
            else parentNode.right = new TreeNode<>(key);
        }
        ++size;
    }

    public boolean find(T key){
        if(isEmpty()) return false;
        return findNode(root, key).key == key;
    }

    public void remove(T key){
        if(isEmpty()) return;
        root = removeNode(root, key);
    }

    private TreeNode<T> removeNode(TreeNode<T> currNode, T key) {
        if(currNode == null) return currNode;
        int comp = currNode.key.compareTo(key);
        if(comp == 0) currNode = removeNode(currNode);
        else if(comp > 0) currNode.left = removeNode(currNode.left, key);
        else currNode.right = removeNode(currNode.right, key);
        return currNode;
    }

    private TreeNode<T> removeNode(TreeNode<T> currNode){
        TreeNode<T> subTree = null;
        if(currNode.left != null && currNode.right != null){
            TreeNode<T> prevNode = getPreviousNode(currNode.left);
            T prevKey = prevNode.key;
            currNode.left = removeNode(currNode.left, prevNode.key);
            currNode.key = prevKey;
            return currNode;
        }
        else if(currNode.left != null) subTree = currNode.left;
        else if(currNode.right != null) subTree = currNode.right;
        --size;
        return subTree;
    }
    private TreeNode<T> getPreviousNode(TreeNode<T> currNode){
        if(currNode.right != null) return getPreviousNode(currNode.right);
        else return currNode;
    }

    private TreeNode<T> findNode(TreeNode<T> currNode, T key) {
        if(currNode.key.equals(key)) return currNode;
        TreeNode<T> nextNode = currNode.key.compareTo(key) > 0 ? currNode.left : currNode.right;
        return nextNode == null ? currNode : findNode(nextNode, key);
    }

    private void inorder(TreeNode<T> currNode, ArrayList<T> list) {
        if(currNode.left!=null) inorder(currNode.left, list);
        list.add(currNode.key);
        if(currNode.right!=null) inorder(currNode.right, list);
    }

    private void preorder(TreeNode<T> currNode, ArrayList<T> list) {
        list.add(currNode.key);
        if(currNode.left!=null) preorder(currNode.left, list);
        if(currNode.right!=null) preorder(currNode.right, list);
    }

    private void postorder(TreeNode<T> currNode, ArrayList<T> list) {
        if(currNode.left!=null) postorder(currNode.left, list);
        if(currNode.right!=null) postorder(currNode.right, list);
        list.add(currNode.key);
    }
}
