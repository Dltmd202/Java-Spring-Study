package composite;

import java.util.Iterator;

public class NullIterator<T> implements Iterator<Node<T>> {
    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Node<T> next() {
        throw new UnsupportedOperationException("이것 호출되면 곤란");
    }
}
