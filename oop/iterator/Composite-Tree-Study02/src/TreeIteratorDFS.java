import java.util.Iterator;
import java.util.Stack;

public class TreeIteratorDFS<T> implements Iterator<Node<T>> {
    Stack<Iterator<Node<T>>> stack = new Stack<>();

    public TreeIteratorDFS(Iterator<Node<T>> iterator) {
        stack.push(iterator);
    }

    @Override
    public boolean hasNext() {
        if(stack.empty()) return false;

        Iterator<Node<T>> iterator = stack.peek();
        if(iterator.hasNext()) return true;
        stack.pop();
        return hasNext();
    }

    @Override
    public Node<T> next() {
        Node<T> node = stack.peek().next();

        if(node instanceof NonLeaf){
            stack.push(node.childIterator());
            return next();
        }
        return node;
    }
}
