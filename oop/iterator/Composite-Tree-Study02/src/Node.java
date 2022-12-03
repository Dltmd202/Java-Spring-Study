import java.util.Iterator;
import java.util.Objects;

public abstract class Node<T> implements Iterable<Node<T>> {
    public static String indent = "";
    private T label;
    private boolean hasChanged = false;

    public T getLabel(){
        return label;
    }

    public Node(T name){
        label = name;
    }

    public boolean hasChanged(){
        return hasChanged;
    }

    public void setChanged(boolean flag){
        hasChanged = flag;
    }

    public abstract String list();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<?> node = (Node<?>) o;
        return hasChanged == node.hasChanged && Objects.equals(label, node.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, hasChanged);
    }

    public int numberOfChilds(){
        return 0;
    }

    public abstract void add(Node<T> node);
    public abstract void remove(Node<T> node);
    public abstract Node<T> getChild(int index);
    public abstract Iterator<Node<T>> iterator();
    public abstract Iterator<Node<T>> childIterator();
}
