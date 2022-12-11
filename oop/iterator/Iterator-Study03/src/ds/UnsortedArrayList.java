package ds;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
public class UnsortedArrayList<T> implements Iterable<T> {
    private int capacity = 5;
    @SuppressWarnings("unchecked")
    private T[] items = (T[])(new Object[capacity]);
    private int size = 0;

    private class ArrayListIterator implements Iterator<T>{
        private int index = 0;
        @Override public boolean hasNext() {
            return index<size;
        }

        @Override public T next() {
            // return items[index++];
            return getClonedIfCloneable(items[index++]);
        }
    }

    public boolean isFull(){
        return false;
    }

    public boolean isEmpty(){
        return size==0;
    }

    public int size() {
        return size;
    }

    @SuppressWarnings("unchecked")
    private T getCloned(T item) {
        T cloned = null;
        try {
            Class<?> C = item.getClass();
            Method cloneMethod = C.getMethod("clone");
            cloned = (T)cloneMethod.invoke(item);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return cloned;
    }

    private T getClonedIfCloneable(T item) {
        if(item instanceof Cloneable) return getCloned(item);
        return item;
    }

    public T peekBack() {
        if(isEmpty()) throw new IllegalStateException();
        // return items[size-1];
        return getClonedIfCloneable(items[size-1]);
    }

    private void increaseCapacity() {
        capacity *= 2;
        items = Arrays.copyOf(items, capacity);
    }

    public void pushBack(T item){
        if(size==capacity) increaseCapacity();
        //list[size] = item;
        items[size] = getClonedIfCloneable(item);
        ++size;
    }

    public T popBack() {
        if(isEmpty()) throw new IllegalStateException();
        T retval = items[size-1];
        items[size-1] = null; // 선택사항
        --size;
        return retval;
    }

    public T get(int index){
        if(index<0 && index>=size) throw new IndexOutOfBoundsException("유효하지 않은 색인 사용");
        //return items[index];
        return getClonedIfCloneable(items[index]);
    }

    public void remove(T item) {
        for(int i=0; i<size; i++)
            if(items[i].equals(item)) {
                for(int j=i+1; j<size; j++) {
                    items[j-1] = items[j];
                }
                --size;
                items[size] = null; // 선택사항
                break;
            }
    }

    @Override public Iterator<T> iterator() {
        return new ArrayListIterator();
    }
}
