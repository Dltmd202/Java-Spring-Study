import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

public class BST_BFS_Iterator<T> implements Iterator<T> {
    Queue<TreeNode<T>> BFS = new ArrayDeque<>();

    public BST_BFS_Iterator(TreeNode<T> root) {
        if(root != null) BFS.add(root);
    }

    @Override
    public boolean hasNext() {
        return !BFS.isEmpty();
    }

    @Override
    public T next() {
        TreeNode<T> currNode = BFS.poll();
        if(currNode.left != null) BFS.add(currNode.left);
        if(currNode.right!=null) BFS.add(currNode.right);
        return null;
    }
}
