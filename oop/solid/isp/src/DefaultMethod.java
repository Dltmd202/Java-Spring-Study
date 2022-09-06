public class DefaultMethod {
}

interface ICalculator{
    int add(int x, int y);
    int sub(int x, int y);

    default int mul(int x, int y){
        return x * y;
    }
}


interface Multiplier{
    int mul(int x, int y);
}

class Calculator implements ICalculator, Multiplier{

    @Override
    public int add(int x, int y) {
        return 0;
    }

    @Override
    public int sub(int x, int y) {
        return 0;
    }

    @Override
    public int mul(int x, int y) {
        return ICalculator.super.mul(x, y);
    }

}