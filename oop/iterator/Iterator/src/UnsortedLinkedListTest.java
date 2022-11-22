import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진 
 * 반복자 패턴 
 * 단일 연결구조 기반 비정렬 범용 리스트 테스트 프로그램
 */
class UnsortedLinkedListTest {
	
	@Test
	void pushFront_popFrontTest() {
		UnsortedLinkedList<Integer> list = new UnsortedLinkedList<>();
		list.pushFront(10);
		list.pushFront(5);
		list.pushFront(3);
		String output = "";
		while(!list.isEmpty()) {
			output += list.popFront()+",";
		}
		assertEquals(output,"3,5,10,");
	}
	
	@Test
	void removeTest() {
		UnsortedLinkedList<Integer> list = new UnsortedLinkedList<>();
		list.pushFront(10);
		list.pushFront(5);
		list.pushFront(3);
		list.remove(3);
		assertEquals(5,list.peekFront());
		list.remove(10);
		assertEquals(1,list.size());
	}
	
	@Test
	void iteratorTest() {
		UnsortedLinkedList<Integer> list = new UnsortedLinkedList<>();
		list.pushFront(10);
		list.pushFront(5);
		list.pushFront(3);
		String output = "";
		for(var n: list)
			output += n+",";
		assertEquals(3,list.size());
		assertEquals(output,"3,5,10,");
	}
	
}
