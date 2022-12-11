import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

public class TreeIteratorBFS<T> implements Iterator<Node<T>> {
    Queue<Iterator<Node<T>>> queue = new ArrayDeque<>();

    public TreeIteratorBFS(Queue<Iterator<Node<T>>> queue) {
        this.queue = queue;
    }

    @Override
    public boolean hasNext() {
        if(queue.isEmpty()) return false;
        else{
            Iterator<Node<T>> iterator = queue.peek();
            if(iterator.hasNext()) return true;
            else{
                queue.poll();
                return hasNext();
            }
        }
    }

    @Override
    public Node<T> next() {
        Node<T> node = queue.peek().next();
        queue.add(node.childIterator());

        if(node instanceof NonLeaf)
            while(!queue.peek().hasNext()) queue.poll();
        return node instanceof NonLeaf ? next(): node;
    }
}
