import java.util.Objects;

/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 프록시 패턴: 보호 프록시
 * 구체적 대상 객체
 * Person의 Rating 유지
 * 자신은 자신의 rating을 평가할 수 없음
 * 자신의 정보는 자신만 수정할 수 있음
 */
public class PersonBeanImpl implements PersonBean {
	private String name;
	private Gender gender;
	private String interest;
	private int rating = 0;
	private int ratingCount = 0;
	@Override
	public String getName() {
		return name;
	}
	@Override
	public Gender getGender() {
		return gender;
	}

	@Override
	public String getInterest() {
		return interest;
	}

	@Override
	public int getHotOrNotRating() {
		if(ratingCount==0) return 0;
		return rating/ratingCount;
	}

	@Override
	public void setName(String name) {
		this.name = Objects.requireNonNull(name);
	}

	@Override
	public void setGender(Gender gender) {
		this.gender = Objects.requireNonNull(gender);
	}

	@Override
	public void setInterest(String interest) {
		this.interest = Objects.requireNonNull(interest);
	}

	@Override
	public void setHotOrNotRating(int rating) {
		this.rating += rating;
		++ratingCount;
	}

}
