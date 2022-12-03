/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * Quackable.java
 * 복합 패턴
 * 꽥꽥 소리 Interface
 * QuackObservable interface를 상속함
 * 모든 quack하는 것을 개별 관찰하기 위해
 */
public interface Quackable extends QuackObservable{
	void quack();
}
