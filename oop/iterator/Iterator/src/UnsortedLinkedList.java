import java.util.Iterator;

/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진 
 * 반복자 패턴
 * 단일 연결구조 기반 비정렬 범용 리스트
 * UnsortedArrayList처럼 getClonedIfCloneable를 만들어 복제하여 저장하고
 * 복제하여 반환할 수 있음. 보통은 모든 T가 복제가 필요한 것은 아니며 효율성 때문에 
 * 이와 같이 구현하지 않음
 */
public class UnsortedLinkedList<T> implements Iterable<T> {
	private static class Node<T>{
		private T item = null;
		private Node<T> next = null;
		public Node() {}
		public Node(Node<T> next) {
			this.next = next;
		}
	}
	private class LinkedListIterator implements Iterator<T>{
		private Node<T> curr = head;
		@Override public boolean hasNext() {
			return curr!=null;
		}
		@Override public T next() {
			T ret = curr.item;
			curr = curr.next;
			return ret;
		}
		
	}
	
	private Node<T> head = null;
	private int size = 0;
	
	public boolean isFull() {
		return false;
	}
	
	public boolean isEmpty() {
		return size==0;
	}
	
	public int size() {
		return size;
	}
	
	public void pushFront(T item) {
		Node<T> newNode = new Node<>();
		newNode.item = item;
		newNode.next = head;
		head = newNode;
		++size;
	}
	
	public T popFront() {
		if(isEmpty()) throw new IllegalStateException();
		Node<T> popNode = head;
		head = head.next;
		--size;
		return popNode.item;
	}
	
	public T peekFront() {
		if(isEmpty()) throw new IllegalStateException();
		return head.item;
	}
	
	public boolean find(T item) {
		Node<T> curr = head;
		while(curr!=null) {
			if(curr.item.equals(item)) return true;
			curr = curr.next;
		}
		return false;
	}
	
	public void remove(T item) {
		if(isEmpty()) return;
		Node<T> dummy = new Node<>(head);
		Node<T> prev = dummy;
		Node<T> curr = head;
		while(curr!=null) {
			if(curr.item.equals(item)) {
				prev.next = curr.next;
				--size;
				break;
			}
			prev = curr;
			curr = curr.next;
		}
		head = dummy.next;
	}
	
	@Override public Iterator<T> iterator() {
		return new LinkedListIterator();
	}
}
