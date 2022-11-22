/**
 * 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * 2022년도 2학기
 * 상태 패턴
 * GumballState.java
 * 컴볼기기들의 상태가 제공해야 하는 interface
 * State Driven Transition (상태 중심 전이)
 * @author 김상진
 */
public interface GumballState {
	void insertCoin();
	void ejectCoin();
	void turnCrank();
	void dispense();
	void refill();
}
