import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Queue;

public class StatisticDisplay implements Observer{

    private Queue<Float> temperatureLog = new ArrayDeque<>();
    private Queue<Float> humidityLog = new ArrayDeque<>();
    private Queue<Float> pressureLog = new ArrayDeque<>();

    public void display(){
        System.out.println("===날씨 통계 정보===");
        System.out.printf("최근 최대 온도: %.2f%n",
                temperatureLog.stream().max(Comparator.comparing(Float::valueOf)).get());
        System.out.printf("최근 최대 습도: %.2f%n",
                humidityLog.stream().max(Comparator.comparing(Float::valueOf)).get());
        System.out.printf("최근 최대 기압: %.2f%n",
                pressureLog.stream().max(Comparator.comparing(Float::valueOf)).get());
    }

    @Override
    public void update(float temperature, float humidity, float pressure) {
        if(temperatureLog.size() == 5) temperatureLog.poll();
        if(humidityLog.size() == 5) temperatureLog.poll();
        if(pressureLog.size() == 5) pressureLog.poll();
        temperatureLog.add(temperature);
        humidityLog.add(humidity);
        pressureLog.add(pressure);
        if(temperatureLog.size() == 5) display();
    }
}
