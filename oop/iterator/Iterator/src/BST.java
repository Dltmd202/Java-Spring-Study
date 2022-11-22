import java.util.ArrayList;
import java.util.Iterator;

/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022 년도 2학기
 * @author 김상진 
 * 반복자 패턴
 * 이진 검색 트리
 * 전체를 방문하여 리스트에 보관한 후, 리스트를 이용하여 반복함
 * 효율성 측면에서는 바람직하지는 않음 (반복자를 사용한다고 항상 모든 요소를 방문하는 것은 아님) 
 */

class TreeNode<T>{
	T key;
	TreeNode<T> left = null;
	TreeNode<T> right = null;
	public TreeNode(T key) {
		this.key = key;
	}
	@Override public String toString() {
		return key.toString();
	}
}

public class BST<T extends Comparable<T>> implements Iterable<T>{
	public enum TraversalType {PREORDER, INORDER, POSTORDER, BFS};
	private TreeNode<T> root = null;
	private int size = 0;
	private TraversalType currentType = TraversalType.INORDER;
	
	public BST() {}
	public int size() {
		return size;
	}
	public boolean isEmpty() {
		return size==0;
	}
	public boolean isFull() {
		return false;
	}
	public void setTraversalType(TraversalType traversalType) {
		currentType = traversalType;
	}
	
	public void add(T key) {
		if(isEmpty()) root = new TreeNode<T>(key);
		else {
			TreeNode<T> parentNode = findNode(root, key);
			if(parentNode.key.equals(key)) return;
			else if(parentNode.key.compareTo(key)>0)
				parentNode.left = new TreeNode<T>(key);
			else parentNode.right = new TreeNode<T>(key);
		}
		++size;
	}
	
	public boolean find(T key) {
		if(isEmpty()) return false;
		return findNode(root,key).key == key;
		
	}
	
	public void remove(T key) {
		if(isEmpty()) return;
		root = removeNode(root, key);
	}
	
	private TreeNode<T> findNode(TreeNode<T> currNode, T key) {
		if(currNode.key.equals(key)) return currNode;
		TreeNode<T> nextNode = (currNode.key.compareTo(key)>0)? currNode.left: currNode.right;
		return nextNode==null? currNode: findNode(nextNode, key);

	}
	
	private TreeNode<T> removeNode(TreeNode<T> currNode, T key){
		if(currNode==null) return currNode; // 삭제할 키가 존재하지 않는 경우
		int comp = currNode.key.compareTo(key);
		if(comp==0) currNode = removeNode(currNode);
		else if(comp>0) currNode.left = removeNode(currNode.left, key);
		else currNode.right = removeNode(currNode.right, key);
		return currNode;
	}
	
	private TreeNode<T> removeNode(TreeNode<T> currNode){
		TreeNode<T> subTree = null;
		if(currNode.left!=null&&currNode.right!=null) {
			TreeNode<T> prevNode = getPreviousNode(currNode.left);
			T prevKey = prevNode.key;
			currNode.left = removeNode(currNode.left, prevNode.key);
			currNode.key = prevKey;
			return currNode;
		}
		else if(currNode.left!=null) subTree = currNode.left;
		else if(currNode.right!=null) subTree = currNode.right;
		--size;
		return subTree;
	} 
	
	private TreeNode<T> getPreviousNode(TreeNode<T> currNode){
		if(currNode.right!=null) return getPreviousNode(currNode.right);
		else return currNode;
	}
	
	private void inorder(TreeNode<T> currNode, ArrayList<T> list) {
		if(currNode.left!=null) inorder(currNode.left, list);
		list.add(currNode.key);
		if(currNode.right!=null) inorder(currNode.right, list);
	}
	
	private void preorder(TreeNode<T> currNode, ArrayList<T> list) {
		list.add(currNode.key);
		if(currNode.left!=null) preorder(currNode.left, list);
		if(currNode.right!=null) preorder(currNode.right, list);
	}
	
	private void postorder(TreeNode<T> currNode, ArrayList<T> list) {
		if(currNode.left!=null) postorder(currNode.left, list);
		if(currNode.right!=null) postorder(currNode.right, list);
		list.add(currNode.key);
	}
	
	@Override
	public Iterator<T> iterator() {
		if(currentType!=TraversalType.BFS) {
			ArrayList<T> list = new ArrayList<>(); // snapshot 방식, 추가공간 사용
			switch(currentType) {
			case PREORDER: preorder(root, list); break;
			case POSTORDER: postorder(root, list); break;
			default: inorder(root, list); break;
				// return new BST_Inorder_Iterator<T>(root);
			}
			return list.iterator();
		}
		else return new BST_BFS_Iterator<T>(root);
	}
}
