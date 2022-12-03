/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * QuackObservable.java
 * 복합 패턴: 관찰자 패턴 
 * 개별 오리가 꽥꽥했을 때 그것을 자동으로 알 수 있도록 확장함
 * default 메소드를 사용하지 않으면 원하지 않는 하위 타입들도 두 메소드를 모두 구현해야 함
 */
public interface QuackObservable {
	default void registerObserver(Observer observer) {}
	default void notifyObservers() {}
}
