package v7;

public interface QuackObservable{
    default void registerObserver(Observer observer) {}
    default void notifyObservers() {}
}
