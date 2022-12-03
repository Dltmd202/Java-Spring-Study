import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

public class BFSIterator<T> implements Iterator<Node<T>> {
    Queue<Node<T>> queue = new ArrayDeque<>();

    public BFSIterator(Node<T> node){
        queue.add(node);
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Node<T> next() {
        return queue.poll();
    }
}
