import java.util.Iterator;

public class Leaf<T> extends Node<T>{
    public Leaf(T name) {
        super(name);
    }

    @Override
    public String list() {
        return getLabel() + "\n";
    }

    @Override
    public void add(Node<T> node) {
        throw new UnsupportedOperationException();

    }

    @Override
    public void remove(Node<T> node) {
        throw new UnsupportedOperationException();

    }

    @Override
    public Node<T> getChild(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Iterator<Node<T>> childIterator() {
        return new NullIterator<>();
    }

    @Override
    public Iterator<Node<T>> iterator() {
        return new NullIterator<>();
    }
}
