public class Main {
    public static void main(String[] args) {
        WeatherData weatherData = new WeatherData();
        CurrentConditionDisplay currentConditionDisplay = new CurrentConditionDisplay();
        weatherData.registerObserver(currentConditionDisplay);
        weatherData.registerObserver(new StatisticDisplay());

        weatherData.setMeasurement(30, 65, 30.4f);
        weatherData.measurementChanged();
        weatherData.setMeasurement(28, 55, 29.2f);
        weatherData.measurementChanged();
        weatherData.setMeasurement(29, 50, 30.8f);
        weatherData.measurementChanged();
        weatherData.setMeasurement(28, 55, 30.2f);
        weatherData.measurementChanged();
        weatherData.setMeasurement(31, 55, 29.2f);
        weatherData.measurementChanged();
        weatherData.setMeasurement(30, 60, 28.2f);
        weatherData.measurementChanged();
        weatherData.removeObserver(currentConditionDisplay);

    }
}