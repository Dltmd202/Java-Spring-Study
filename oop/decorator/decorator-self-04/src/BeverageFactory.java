import java.util.HashMap;
import java.util.Map;

public class BeverageFactory {

    private static Map<String, Restriction> restrictionTable;

    private static String[] compoentsName = {"Milk", "Mocha", "Whip"};

    static {
        restrictionTable = new HashMap<>();


        Restriction restrictionMilk = new Restriction(1);
        restrictionMilk.getExclusionList().add("DarkRoast");
        restrictionTable.put("Milk",
                restrictionMilk);

        Restriction restrictionWhip = new Restriction(2);
        restrictionWhip.getExclusionList().add("DarkRoast");
        restrictionTable.put("Whip",
                restrictionWhip);

    }

    public static Beverage createCoffee(String coffee, String... list){
    }
}
