import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class NonLeaf<T> extends Node<T>{
    private List<Node<T>> childs = new ArrayList<>();
    public NonLeaf(T name) {
        super(name);
    }

    @Override
    public String list() {
        String output = getLabel()+"\n";
        indent += " ".repeat(5);
        for(var node: childs){
            output += indent + node.list();
        }
        indent = indent.substring(0,indent.length()-5);
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
        if(index>=0&&index<childs.size())
            return childs.get(index);
        else throw new IndexOutOfBoundsException("해당 색인에 해당되는 노드가 없음");
    }

    @Override
    protected Iterator<Node<T>> childIterator() {
        return null;
    }

    @Override
    public Iterator<Node<T>> iterator() {
        return null;
    }
}
