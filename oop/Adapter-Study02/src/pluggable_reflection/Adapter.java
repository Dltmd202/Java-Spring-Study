package pluggable_reflection;

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
        }catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
