import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진 
 * 반복자 패턴
 * 배열 기반 비정렬 범용 리스트 테스트 프로그램
 */
class UnsortedArrayListTest {
	
	@Test
	void pushBack_popBackTest() {
		UnsortedArrayList<Integer> list = new UnsortedArrayList<>();
		list.pushBack(10);
		list.pushBack(5);
		list.pushBack(3);
		list.pushBack(7);
		list.pushBack(9);
		list.pushBack(11);
		String output = "";
		while(!list.isEmpty()) {
			output += list.popBack()+",";
		}
		assertEquals(output,"11,9,7,3,5,10,");
	}
	
	@Test
	void removeTest() {
		UnsortedArrayList<Integer> list = new UnsortedArrayList<>();
		list.pushBack(10);
		list.pushBack(5);
		list.pushBack(3);
		list.pushBack(1);
		list.remove(3);
		assertEquals(1,list.peekBack());
		list.remove(10);
		String output = "";
		while(!list.isEmpty()) {
			output += list.popBack()+",";
		}
		assertEquals(output,"1,5,");		
	}
	
	@Test
	void cloneTest() {
		UnsortedArrayList<Integer> list = new UnsortedArrayList<>();
		list.pushBack(10);
		assertEquals(list.get(0), 10);
		
		class A{
			int a;
			public A(int a) { this.a = a; }
			void set(int a) { this.a = a; }
			int get() { return a; }
		} 
		
		class B implements Cloneable{
			int b;
			public B(int b) { this.b = b; }
			void set(int b) { this.b = b; }
			int get() { return b; }
			@Override public B clone() {
				B cloned = null; 
				try { 
					cloned = (B)super.clone(); 
				} catch(Exception e){} 
				return cloned; 
			}
		} 
		
		UnsortedArrayList<A> list1 = new UnsortedArrayList<>();
		A a = new A(2);
		list1.pushBack(new A(3));
		list1.pushBack(a);
		assertEquals(list1.get(0).get(), 3);
		assertEquals(list1.get(1).get(), 2);
		list1.get(0).set(2);
		a.set(3);
		assertEquals(list1.get(0).get(), 2);
		assertEquals(list1.get(1).get(), 3);
		
		UnsortedArrayList<B> list2 = new UnsortedArrayList<>();
		B b = new B(2);
		list2.pushBack(new B(3));
		list2.pushBack(b);
		assertEquals(list2.get(0).get(), 3);
		assertEquals(list2.get(1).get(), 2);
		b.set(3);
		list2.get(0).set(2);
		assertEquals(list2.get(0).get(), 3);
		assertEquals(list2.get(1).get(), 2);
	}
	
	@Test
	void iteratorTest() {
		UnsortedArrayList<Integer> list = new UnsortedArrayList<>();
		list.pushBack(10);
		list.pushBack(5);
		list.pushBack(3);
		String output = "";
	
		for(var n: list)
			output += n+",";
		/*
		Iterator<Integer> iterator = list.iterator();
		while(iterator.hasNext()) {
			output += iterator.next()+",";
		}
		*/
		/*
		StringBuilder build = new StringBuilder();
		list.forEach(s->build.append(s+","));
		output = build.toString();
		*/
		
		assertEquals(3,list.size());
		assertEquals(output,"10,5,3,");
	}

}
