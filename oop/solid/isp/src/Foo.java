public class Foo {
}


interface A{
    void foo();
}

interface B extends A{
    void bar();
}

class X implements B{

    @Override
    public void foo() {

    }

    @Override
    public void bar() {

    }
}

class Y implements A{
    @Override
    public void foo() {

    }
}