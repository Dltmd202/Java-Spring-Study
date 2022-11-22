import java.util.ListIterator;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        UnsortedLinkedList<Integer> ua = new UnsortedLinkedList<>();

        ua.pushBack(1);
        ua.pushBack(2);
        ua.pushBack(3);

        ListIterator<Integer> iterator = ua.iterator();

        while(iterator.hasNext()){
            int i = iterator.nextIndex();
            Integer next = iterator.next();
            if(next == 1){
                iterator.add(4);
            }
            System.out.println(i + " " + next + " ");
        };
        System.out.println();



        while (iterator.hasPrevious()){
            System.out.print(iterator.previous() + " ");
        }
        System.out.println();

        iterator = ua.iterator();

        System.out.println("fuck");
        while(iterator.hasNext()){
            Integer next = iterator.next();
            if(next == 3) iterator.remove();
            System.out.print(next + " ");
        }
        System.out.println();

        while (iterator.hasPrevious()){
            System.out.print(iterator.previous() + " ");
        }
        System.out.println();


        iterator = ua.iterator();
        while(iterator.hasNext()){
            Integer next = iterator.next();
            if(next == 2) iterator.remove();
            System.out.print(next + " ");
        }
        System.out.println();

        while (iterator.hasPrevious()){
            System.out.print(iterator.previous() + " ");
        }
        System.out.println();
    }
}