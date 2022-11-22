/**
 * 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 상태 패턴
 * GumballState.java
 * 상태 열거형
 * Context Driven Transition (문맥 중심 전이)
 * 열거형으로 상태 객체들을 정의. 한 자바 파일에 모든 상태 구현.
 * 상태 객체의 메소드가 다음 상태를 반환하는 버전
 * 상태 객체 간의 coupling tight해지는 단점이 있지만 
 * 열거형으로 구현되기 때문에 이것이 문제되지 않음
 */
public enum GumballState{
	HASCOINSTATE {
		@Override
		public GumballState insertCoin(GumballMachine gumballMachine) {
			System.out.println("이미 동전이 있음");
			return this;
		}
		
		@Override
		public GumballState ejectCoin(GumballMachine gumballMachine) {
			System.out.println("취소되었음");
			return NOCOINSTATE;
		}

		@Override
		public GumballState turnCrank(GumballMachine gumballMachine) {
			System.out.println("손잡이를 돌렸음");
			return SOLDSTATE;
		}

		@Override
		public GumballState dispense(GumballMachine gumballMachine) {
			System.out.println("손잡이를 돌려야 껌볼이 나옴");
			return this;
		}
		
		@Override
		public GumballState refill(GumballMachine gumballMachine) {
			System.out.println("껌볼이 없는 경우에는 껌볼을 채울 수 있음");
			return this;
		}
	},
	NOCOINSTATE {
		@Override
		public GumballState insertCoin(GumballMachine gumballMachine) {
			System.out.println("동전이 삽입되었음");
			return HASCOINSTATE;
		}

		@Override
		public GumballState ejectCoin(GumballMachine gumballMachine) {
			System.out.println("반환할 동전 없음");
			return this;
		}

		@Override
		public GumballState turnCrank(GumballMachine gumballMachine) {
			System.out.println("동전이 없어 손잡이를 돌릴 수 없음");
			return this;
		}

		@Override
		public GumballState dispense(GumballMachine gumballMachine) {
			System.out.println("동전을 투입해야 구입할 수 있음");
			return this;
		}
		
		@Override
		public GumballState refill(GumballMachine gumballMachine) {
			System.out.println("껌볼이 없는 경우에만 껌볼을 채울 수 있음");
			return this;
		}
	},
	SOLDSTATE {
		@Override
		public GumballState insertCoin(GumballMachine gumballMachine) {
			System.out.println("동전을 투입할 수 있는 단계가 아님");
			return this;
		}
		
		@Override
		public GumballState ejectCoin(GumballMachine gumballMachine) {
			System.out.println("반환할 동전이 없음");
			return this;

		}

		@Override
		public GumballState turnCrank(GumballMachine gumballMachine) {
			System.out.println("이미 손잡이를 돌렸음");
			return this;
		}

		@Override
		public GumballState dispense(GumballMachine gumballMachine) {
			System.out.println("껌볼이 나옴");
			gumballMachine.dispense();
			return gumballMachine.getNumberOfGumballs()==0? SOLDOUTSTATE: NOCOINSTATE;
		}
		
		@Override
		public GumballState refill(GumballMachine gumballMachine) {
			System.out.println("껌볼이 없는 경우에는 껌볼을 채울 수 있음");
			return this;
		}
	},
	SOLDOUTSTATE{
		@Override
		public GumballState insertCoin(GumballMachine gumballMachine) {
			System.out.println("껌볼이 없어 판매가 중단됨");
			return this;
		}
		
		@Override
		public GumballState ejectCoin(GumballMachine gumballMachine) {
			System.out.println("껌볼이 없어 판매가 중단됨");
			return this;
		}

		@Override
		public GumballState turnCrank(GumballMachine gumballMachine) {
			System.out.println("껌볼이 없어 판매가 중단됨");
			return this;
		}

		@Override
		public GumballState dispense(GumballMachine gumballMachine) {
			System.out.println("껌볼이 없어 판매가 중단됨");
			return this;
		}
		
		@Override
		public GumballState refill(GumballMachine gumballMachine) {
			System.out.println("껌볼을 채음");
			gumballMachine.refill(20);
			return NOCOINSTATE;
		}
	};
	public abstract GumballState insertCoin(GumballMachine gumballMachine);
	public abstract GumballState ejectCoin(GumballMachine gumballMachine);
	public abstract GumballState turnCrank(GumballMachine gumballMachine);
	public abstract GumballState dispense(GumballMachine gumballMachine);
	public abstract GumballState refill(GumballMachine gumballMachine);
	/*
	public GumballState insertCoin(GumballMachine gumballMachine){ return this; }
	public GumballState ejectCoin(GumballMachine gumballMachine){ return this; }
	public GumballState turnCrank(GumballMachine gumballMachine){ return this; }
	public GumballState dispense(GumballMachine gumballMachine){ return this; }
	public GumballState refill(GumballMachine gumballMachine){ return this; }
	*/
}
