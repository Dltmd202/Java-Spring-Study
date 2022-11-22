/**
 * 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @verison 2022년도 2학기
 * @author 김상진
 * 상태 패턴
 * GumballState.java
 * 컴볼기기들의 상태가 제공해야 하는 interface
 * Context Driven Transition (문맥 중심 전이)
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
// 싱글톤으로 모델링한 경우에만 이 방법을 사용할 수 있음
// 상태 전이가 고정되어 있지 않으면 올바른 상태 객체를 반환할 수 없음
/*
public interface GumballState {
	default	GumballState insertCoin() { return this; } 
	default GumballState ejectCoin() { return this; }
	default GumballState turnCrank() { return this; }
	default GumballState dispense() { return this; }
	default GumballState refill() { return this; }
}
*/

//반환값과 상관없이 전이 메소드의 인자로 문맥 객체를 전달할 수 있음
//상태 객체를 반환하는 버전의 경우: 상태 전이가 고정되지 않은 경우에도 올바른 상태 객체를 반환할 수 있음
/*
public interface GumballState {
	default	GumballState insertCoin(GumballMachine gumballMachine) { return this; } 
	default GumballState ejectCoin(GumballMachine gumballMachine) { return this; }
	default GumballState turnCrank(GumballMachine gumballMachine) { return this; }
	default GumballState dispense(GumballMachine gumballMachine) { return this; }
	default GumballState refill(GumballMachine gumballMachine) { return this; }
}
*/