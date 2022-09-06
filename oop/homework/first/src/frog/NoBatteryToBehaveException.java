package frog;

public class NoBatteryToBehaveException extends RuntimeException{
    public NoBatteryToBehaveException(String message) {
        super(message);
    }
}