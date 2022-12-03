/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * QuackCounter.java
 * 복합 패턴: 장식 패턴
 * 꽥꽥한 횟수를 계산하여 주는 장식자
 * 여러 오리를 장식하더라도 꽥꽥 수는 공유함
 */
public class QuackCounter implements Quackable {
	private Quackable duck;
	private static int numberOfQuacks = 0;
	public QuackCounter(Quackable duck){
		this.duck = duck;
	}
	@Override
	public void quack(){
		duck.quack();
		++numberOfQuacks;
	}
	public static int getQuacks(){
		return numberOfQuacks;
	}
}
