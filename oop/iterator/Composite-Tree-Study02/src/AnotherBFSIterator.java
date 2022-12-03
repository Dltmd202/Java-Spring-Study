import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

public class AnotherBFSIterator<T> implements Iterator<Node<T>> {
    Queue<Node<T>> queue = new ArrayDeque<>();

    public AnotherBFSIterator(Queue<Node<T>> queue) {
        this.queue = queue;
    }

    @Override
    public boolean hasNext() {
        return !queue.isEmpty();
    }

    @Override
    public Node<T> next() {
        Node<T> curr = queue.peek();
        if(curr instanceof NonLeaf){
            for (int i = 0; i < curr.numberOfChilds(); i++) {
                queue.add(curr.getChild(i));
            }
            queue.poll();
            return next();
        } else return queue.poll();
    }

}
