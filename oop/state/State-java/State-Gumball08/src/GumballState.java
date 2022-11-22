/**
 * 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * 상태 패턴
 * GumballState.java
 * 상태 열거형
 * Context Driven Transition (문맥 기반 전이)
 * 열거형으로 상태 객체들을 정의. 한 자바 파일에 모든 상태 구현.
 * 상태 객체의 메소드 true/false를 반환 
 * @author 김상진
 *
 */
public enum GumballState{
	HASCOINSTATE {
		@Override
		public boolean insertCoin() {
			System.out.println("이미 동전이 있음");
			return false;
		}

		@Override
		public boolean ejectCoin() {
			System.out.println("취소되었음");
			return true;
		}

		@Override
		public boolean turnCrank() {
			System.out.println("손잡이를 돌렸음");
			return true;
		}

		@Override
		public boolean dispense() {
			System.out.println("손잡이를 돌려야 껌볼이 나옴");
			return false;
		}
		
		@Override
		public boolean refill() {
			System.out.println("껌볼이 없는 경우에는 껌볼을 채울 수 있음");
			return false;
		}
	},
	NOCOINSTATE {
		@Override
		public boolean insertCoin() {
			System.out.println("동전이 삽입되었음");
			return true;
		}

		@Override
		public boolean ejectCoin() {
			System.out.println("반환할 동전 없음");
			return false;
		}

		@Override
		public boolean turnCrank() {
			System.out.println("동전이 없어 손잡이를 돌릴 수 없음");
			return false;
		}

		@Override
		public boolean dispense() {
			System.out.println("동전을 투입해야 구입할 수 있음");
			return false;
		}
		
		@Override
		public boolean refill() {
			System.out.println("껌볼이 없는 경우에는 껌볼을 채울 수 있음");
			return false;
		}

	},
	SOLDSTATE {
		@Override
		public boolean insertCoin() {
			System.out.println("동전을 투입할 수 있는 단계가 아님");
			return false;
		}

		@Override
		public boolean ejectCoin() {
			System.out.println("반환할 동전이 없음");
			return false;

		}

		@Override
		public boolean turnCrank() {
			System.out.println("이미 손잡이를 돌렸음");
			return false;
		}

		@Override
		public boolean dispense() {
			System.out.println("껌볼이 나옴");
			return true;
		}
		
		@Override
		public boolean refill() {
			System.out.println("껌볼이 없는 경우에는 껌볼을 채울 수 있음");
			return false;
		}

	},
	SOLDOUTSTATE{
		@Override
		public boolean insertCoin() {
			System.out.println("껌볼이 없어 판매가 중단됨");
			return false;
		}

		@Override
		public boolean ejectCoin() {
			System.out.println("껌볼이 없어 판매가 중단됨");
			return false;
		}

		@Override
		public boolean turnCrank() {
			System.out.println("껌볼이 없어 판매가 중단됨");
			return false;
		}

		@Override
		public boolean dispense() {
			System.out.println("껌볼이 없어 판매가 중단됨");
			return false;
		}
		
		@Override
		public boolean refill() {
			System.out.println("껌볼을 채음");
			return true;
		}

	};
	public abstract boolean insertCoin();
	public abstract boolean ejectCoin();
	public abstract boolean turnCrank();
	public abstract boolean dispense();
	public abstract boolean refill();
	/*
	public boolean insertCoin(){ return false; }
	public boolean ejectCoin(){ return false; }
	public boolean turnCrank(){ return false; }
	public boolean dispense(){ return false; }
	public boolean refill(){ return false; }
	*/
}

