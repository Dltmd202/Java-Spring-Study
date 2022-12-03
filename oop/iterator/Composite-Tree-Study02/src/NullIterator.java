import java.util.Iterator;

public class NullIterator<T> implements Iterator<Node<T>> {
    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Node<T> next() {
        return null;
    }
}
