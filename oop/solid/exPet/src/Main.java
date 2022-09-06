public class Main {

}

class Foo{
    int foo(int n){
        int retval = 0;
        if(n > 0){

            for (int i = 0; i < n; i++) {
                retval++;
            }
        }
        if (retval < 0) throw new IllegalStateException();
        return retval;
    }
}

class enhancedPrerequisitesFoo extends Foo{
    @Override
    int foo(int n) {
        if(n % 2 ==0) {
            return super.foo(n);
        } else throw new IllegalStateException();
    }
}

class weakenedPostConditionsFoo extends Foo{
    @Override
    int foo(int n) {
        int res = super.foo(n);
        if(res > -1000){
            return res;
        } else throw new IllegalStateException();
    }
}

class Pet{ }

class Cat extends Pet{ }

class Dog extends Pet { }

class PetShop{
    private Pet pet;


    Pet getPet() {
        return pet;
    }

    /**
     * 상속에 파라미터 타입은 정확히 일치해야 한다.
     * @param pet
     */
    void addPet(Pet pet){
        this.pet = pet;
    }
}

class CatShop extends PetShop{

    @Override
    Cat getPet() {
        return (Cat)super.getPet();
    }

    /**
     * 상속에 파라미터 타입은 정확히 일치해야 한다.
     * @param pet
     */
    @Override
    void addPet(
            // 컴파일 에러
            // Cat pet
            Pet pet
    ) {
        super.addPet(pet);
    }
}

class DogShop extends PetShop{

    @Override
    Pet getPet() {
        return super.getPet();
    }

    /**
     * 상속에 파라미터 타입은 정확히 일치해야 한다.
     * @param pet
     */
    @Override
    void addPet(
            // 컴파일 에러
            // Object pet
            Pet pet
    ) {
        super.addPet((Pet) pet);
    }
}

