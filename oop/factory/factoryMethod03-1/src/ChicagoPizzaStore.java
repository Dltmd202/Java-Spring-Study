/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 팩토리 메소드 패턴: Head First Pattern 예제
 * ChicagoPizzaStore.java: 시카고 피자 팩토리
 * 팩토리 메소드 패턴: 구체적 팩토리
 */
public class ChicagoPizzaStore extends PizzaStore {
    @Override
    protected Pizza createPizza(String type) {
        Pizza pizza = null;
        switch(type){
            case "치즈": pizza = new ChicagoCheesePizza(); break;
            case "포테이토": pizza = new ChicagoPotatoPizza(); break;
        }
        return pizza;
    }

}
