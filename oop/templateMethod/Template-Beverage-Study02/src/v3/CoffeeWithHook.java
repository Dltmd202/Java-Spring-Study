package v3;

import java.util.Scanner;

public class CoffeeWithHook extends CaffeineBeverage{
    @Override
    protected void brew(){
        System.out.println("커피를 내림");
    }

    @Override
    protected void addCondiment(){
        System.out.println("밀크와 설탕 추가");
    }

    @Override
    protected boolean customerWantsCondiments() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("밀크와 설탕을 추가하시겠습니까 (y/n)?");
        String answer = scanner.nextLine().toLowerCase();
        scanner.close();
        return answer.equals("y");
    }
}
