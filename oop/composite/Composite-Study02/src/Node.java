import java.util.Iterator;

public abstract class Node<T> implements Iterable<Node<T>>{
    public static String indent = "";
    private T label;
    private boolean hasChanged = false;
    public Node(T name){
        this.label = name;
    }

    public T getLabel(){
        return label;
    }
    public boolean hasChanged(){
        return hasChanged;
    }
    public void setChanged(boolean flag){
        hasChanged = flag;
    }
    public abstract String list();

    @Override public boolean equals(Object other) {
        if(other==null||getClass()!=other.getClass()) return false;
        if(this==other) return true;
        Node<?> node = (Node<?>)other;
        return label.equals(node.label)&&hasChanged==node.hasChanged;
    }
    public int numberOfChilds(){
        return 0;
    }

    public abstract void add(Node<T> node);
    public abstract void remove(Node<T> node);
    public abstract Node<T> getChild(int index);
    protected abstract Iterator<Node<T>> childIterator();
}
