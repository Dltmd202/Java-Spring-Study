/**
 * 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * 2022년도 2학기
 * 상태 패턴
 * GumballState.java
 * 컴볼기기들의 상태가 제공해야 하는 interface
 * Context Driven Transition (문맥 중심 전이)
 * @author 김상진
 */
public interface GumballState {
	boolean insertCoin();
	boolean ejectCoin();
	boolean turnCrank();
	boolean dispense();
	boolean refill();
}

// 실제 각 상태 객체에서 문자열의 출력은 디버깅 용도로
// 필요 없으면 아래와 같이 구현하는 것이 더 편함
// 이 경우 각 상태 객체는 상태 전이가 필요하거나 상태 객체에서 어떤 작업을
// 해야 하는 메소드들에 대해서만 재정의하면 됨
/*
public interface GumballState {
	default boolean insertCoin() { return false; } 
	default boolean ejectCoin() { return false; }
	default boolean turnCrank() { return false; }
	default boolean dispense() { return false; }
	default boolean refill() { return false; }
}
*/

// boolean 값을 반환하는 대신에 다음 상태를 반환하는 형태로 구현 가능
// 형제 상태 클래스간 tight하게 연결됨
/*
public interface GumballState {
	default	GumballState insertCoin() { return this; } 
	default GumballState ejectCoin() { return this; }
	default GumballState turnCrank() { return this; }
	default GumballState dispense() { return this; }
	default GumballState refill() { return this; }
}
*/