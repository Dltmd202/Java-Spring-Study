import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진 
 * 반복자 패턴
 * 이진 검색 트리 반복자
 */
public class BST_BFS_Iterator<T> implements Iterator<T> {
	private Queue<TreeNode<T>> BFS = new ArrayDeque<>();
	
	public BST_BFS_Iterator(TreeNode<T> root) {
		if(root!=null) BFS.add(root);
	}
	
	@Override
	public boolean hasNext() {
		return !BFS.isEmpty();
	}

	@Override
	public T next() {
		TreeNode<T> currNode = BFS.poll();
		if(currNode.left!=null) BFS.add(currNode.left);
		if(currNode.right!=null) BFS.add(currNode.right);
		return currNode.key;
	}
}
