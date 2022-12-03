import java.util.Iterator;

public class Leaf<T> extends Node<T> {

    public Leaf(T label){
        super(label);
    }

    @Override
    public String list() {
        return getLabel() + "\n";
    }

    @Override
    public void add(Node<T> node) {
        throw new UnsupportedOperationException("단말 노드");
    }

    @Override
    public void remove(Node<T> node) {
        throw new UnsupportedOperationException("단말 노드");
    }

    @Override
    public Node<T> getChild(int index) {
        throw new UnsupportedOperationException("단말 노드");
    }

    @Override
    public Iterator<Node<T>> iterator() {
        return new NullIterator<>();
    }

    @Override
    public Iterator<Node<T>> childIterator() {
        return new NullIterator<>();
    }
}
