/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 프록시 패턴: 보호 프록시
 * 대상 객체 interface
 */
public interface PersonBean {
	enum Gender {MALE, FEMALE}
	String getName();
	Gender getGender();
	String getInterest();
	int getHotOrNotRating();
	void setName(String name);
	void setGender(Gender gender);
	void setInterest(String interest);
	void setHotOrNotRating(int rating);
}
