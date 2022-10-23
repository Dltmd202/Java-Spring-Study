import java.nio.file.Watchable;

public class Main {
    public static void main(String[] args) {
        WeatherData wd = new WeatherData();
        CurrentConditionDisplay display = new CurrentConditionDisplay(wd);

        wd.measurementChanged(1, 2, 3);
        wd.measurementChanged(1, 2, 3);
        wd.measurementChanged(1, 2, 3);
        wd.measurementChanged(1, 2, 3);
    }
}