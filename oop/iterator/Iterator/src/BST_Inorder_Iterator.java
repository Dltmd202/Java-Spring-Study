import java.util.Iterator;
import java.util.Stack;

/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진 
 * 반복자 패턴
 * 이진 검색 트리 반복자
 */
public class BST_Inorder_Iterator<T> implements Iterator<T> {
	private Stack<TreeNode<T>> inorder = new Stack<>();
	private TreeNode<T> curr = null;
	
	public BST_Inorder_Iterator(TreeNode<T> root) {
		if(root!=null) curr = root;
	}
	
	@Override public boolean hasNext() {
		return !(curr==null && inorder.isEmpty());
	}

	@Override public T next() {
		T ret = null;
		while(curr!=null) {
			inorder.push(curr);
			curr = curr.left;
		}
		if(!inorder.isEmpty()) {
			curr = inorder.pop();
			ret = curr.key;
			curr = curr.right;
		}
		return ret;
	}
}
