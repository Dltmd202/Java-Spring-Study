public enum GumballState {
    HASCOINSTATE{
        @Override
        public void insertCoin(GumballMachine gumballMachine) {
            System.out.println("취소되었음");
            gumballMachine.changeState(NOCOINSTATE);
        }

        @Override
        public void ejectCoin(GumballMachine gumballMachine) {
            System.out.println("취소되었음");
            gumballMachine.changeState(NOCOINSTATE);
        }

        @Override
        public void turnCrank(GumballMachine gumballMachine) {
            System.out.println("손잡이를 돌렸음");
            gumballMachine.changeState(SOLDSTATE);
        }

        @Override
        public void dispense(GumballMachine gumballMachine) {
            System.out.println("손잡이를 돌려");
        }
    }, NOCOINSTATE{
        @Override
        public void insertCoin(GumballMachine gumballMachine) {
            System.out.println("동전이 삽입되었음");
            gumballMachine.changeState(HASCOINSTATE);
        }

        @Override
        public void ejectCoin(GumballMachine gumballMachine) {
            System.out.println("반환할 동전 없음");
        }

        @Override
        public void turnCrank(GumballMachine gumballMachine) {
            System.out.println("동전이 없어 손잡이를 돌릴 수 없음");
        }

        @Override
        public void dispense(GumballMachine gumballMachine) {
            System.out.println("동전을 투입해야 구입할 수 있음");
        }

        @Override
        public void refill(GumballMachine gumballMachine) {
            System.out.println("껌볼이 없는 경우에는 껌볼을 채울 수 있음");
        }

    }, SOLDSTATE{
        public void insertCoin(GumballMachine gumballMachine) {
            System.out.println("동전을 투입할 수 있는 단계가 아님");
        }

        @Override
        public void ejectCoin(GumballMachine gumballMachine) {
            System.out.println("반환할 동전이 없음");
        }

        @Override
        public void turnCrank(GumballMachine gumballMachine) {
            System.out.println("이미 손잡이를 돌렸음");
        }
        @Override
        public void dispense(GumballMachine gumballMachine) {
            gumballMachine.dispense();
            System.out.println("껌볼이 나옴");
            if(gumballMachine.isEmpty()){
                gumballMachine.changeState(SOLDOUTSTATE);
            }
            else{
                gumballMachine.changeState(NOCOINSTATE);
            }
        }

        @Override
        public void refill(GumballMachine gumballMachine) {
            System.out.println("껌볼이 없는 경우에는 껌볼을 채울 수 있음");
        }

    }, SOLDOUTSTATE{
        @Override
        public void insertCoin(GumballMachine gumballMachine) {
            System.out.println("껌볼이 없어 판매가 중단됨, 동전 삽입 불가");
        }

        @Override
        public void ejectCoin(GumballMachine gumballMachine) {
            System.out.println("껌볼이 없어 판매가 중단됨, 동전 없음");
        }

        @Override
        public void turnCrank(GumballMachine gumballMachine) {
            System.out.println("껌볼이 없어 판매가 중단됨, 손잡이를 돌릴 수 없음");
        }

        @Override
        public void dispense(GumballMachine gumballMachine) {
            System.out.println("껌볼이 없어 판매가 중단됨");
        }

        @Override
        public void refill(GumballMachine gumballMachine) {
            System.out.println("껌볼을 채음");
            gumballMachine.refill(20);
            gumballMachine.changeState(NOCOINSTATE);
        }
    };

    public void insertCoin(GumballMachine gumballMachine) {}
    public void ejectCoin(GumballMachine gumballMachine) {}
    public void turnCrank(GumballMachine gumballMachine) {}
    public void dispense(GumballMachine gumballMachine) {}
    public void refill(GumballMachine gumballMachine) {}
}
