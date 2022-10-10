import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class BeverageFactory {
    static Beverage createCoffee(String... beverage){
        Class<?> coffeeClass;
        Class<?> condimentClass;

        try {
            coffeeClass = Class.forName(beverage[0]);
            if(coffeeClass == CondimentDecorator.class)
                throw new IllegalArgumentException("첫 번째 인자는 음료여야 합니다.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Beverage coffee;
        try {
            coffee = (Beverage) coffeeClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        try{
            for (String s : beverage) {
                condimentClass = Class.forName(s);
//                if(condimentClass != CondimentDecorator.class)
//                    throw new IllegalArgumentException("첫 번째 인자를 제외한 인자는 모두 컨디먼트여야 합니다.");
                Constructor<?> constructor = condimentClass.getConstructor(Beverage.class);
                coffee = (Beverage) constructor.newInstance(coffee);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return coffee;
    }
}
