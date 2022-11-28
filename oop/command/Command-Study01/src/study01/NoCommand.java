package study01;

public class NoCommand implements Command {

    public static final NoCommand unique = new NoCommand();

    @Override
    public void execute() {}
}
