/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기 
 * @author 김상진
 * 팩토리 메소드 패턴: Head First Pattern 예제
 * Pizza.java: 피자
 * 모든 피자를 아우르는 추상 타입
 * 생성 메소드 + 전략 패턴
 */
public abstract class Pizza {
	private String name;
	protected Pizza(String name){
		this.name = name;
	}
	public String toString(){
		return name;
	}
	public void prepare(){
		System.out.printf("준비중: %s%n", name);
	} 
	public void bake(){
		System.out.println("25분 동안 350도에서 굽다.");
	}
	public void cut(){
		System.out.println("피자를 8조각으로 짜른다.");
	}
	public void box(){
		System.out.println("포장합니다!");
	}
}