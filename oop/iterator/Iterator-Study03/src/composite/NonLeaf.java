package composite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NonLeaf<T> extends Node<T>{
    private List<Node<T>> childs = new ArrayList<>();

    public NonLeaf(T name) {
        super(name);
    }

    @Override
    public String list() {
        return null;
    }

    @Override
    public void add(Node<T> node) {
        childs.add(node);
    }

    @Override
    public void remove(Node<T> node) {
        childs.remove(node);
    }

    @Override
    public Node<T> getChild(int index) {
        return null;
    }

    @Override
    public Iterator<Node<T>> iterator() {

    }

    @Override
    protected Iterator<Node<T>> childIterator() {
        return null;
    }
}
