import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Adapter implements Target{
    Object adaptee;
    String cryfunc;

    public Adapter(Object adaptee, String cryfunc) {
        this.adaptee = adaptee;
        this.cryfunc = cryfunc;
    }

    @Override
    public void foo() {
        try {
            Class<?> adapteeClass = adaptee.getClass();
            Method cry = adapteeClass.getMethod(cryfunc);
            cry.invoke(adaptee);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
