import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NonLeaf<T> extends Node<T> {
    private List<Node<T>> childs = new ArrayList<>();
    public NonLeaf(T name) {
        super(name);
    }

    public int numberOfChilds(){
        return childs.size();
    }

    @Override
    public boolean equals(Object other) {
        if(!super.equals(other)) return false;
        NonLeaf<?> node = (NonLeaf<?>)other;
        return childs.equals(node.childs);
    }

    @Override
    public String list() {
        String output = getLabel() + "\n";
        indent += " ".repeat(5);
        for (Node<T> child : childs) {
            output += indent + child.list();
        }
        indent = indent.substring(0, indent.length() - 5);
        return output;
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
        if(index >= 0 && index < childs.size())
            return childs.get(index);
        else throw new IndexOutOfBoundsException();
    }



    @Override
    public Iterator<Node<T>> iterator() {
        return new BFSIterator<>(this);
    }

    @Override
    public Iterator<Node<T>> childIterator() {
        return childs.iterator();
    }
}
