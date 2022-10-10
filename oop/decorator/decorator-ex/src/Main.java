import java.io.*;

public class Main {
    public static void main(String[] args) {
        factoryTest();

    }

    public static void test(){
        Beverage beverage = new Espresso();
        System.out.println(beverage.getDescription() + " $" + beverage.cost());

        Beverage beverage2 = new HouseBlend();
        beverage2 = new Mocha(beverage2);
        beverage2 = new Mocha(beverage2);

        System.out.println(beverage2.getDescription() + " $" + beverage2.cost());
    }

    public static void main2(String[] args) {
        int c;

        try {
            InputStream in = new LowerCaseInputStream(new BufferedInputStream(new FileInputStream("./test.txt")));

            while ((c = in.read()) >= 0){
                System.out.println((char) c);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void factoryTest(){
        Beverage beverage = BeverageFactory.createCoffee("HouseBlend", "Mocha", "Whip", "Mocha");
        System.out.println(beverage.cost());
        System.out.println(beverage.getDescription());
    }


}