import java.util.Iterator;
import java.util.Stack;

public class DFSIterator<T> implements Iterator<Node<T>> {
    Stack<Node<T>> stack = new Stack<>();

    public DFSIterator(Node<T> node){
        stack.push(node);
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public Node<T> next() {
        Node<T> node = stack.pop();
        if(node instanceof NonLeaf) {
            for(int i=0; i<node.numberOfChilds(); i++)
                stack.push(node.getChild(i));
        }
        return node;
    }
}
