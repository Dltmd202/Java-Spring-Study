import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class Main {
    @Test
    void addTest(){
        BST<Integer> tree = new BST<>();
        tree.add(7);
        tree.add(5);
        tree.add(3);
        tree.add(9);
        tree.add(11);
        String output = "";
        for (Integer n : tree) {
            output += n + ",";
        }
        assertEquals(output,"3,5,7,9,11,");
    }

    @Test
    void removeTest(){
        BST<Integer> tree = new BST<>();
        tree.add(7);
        tree.add(5);
        tree.add(6);
        tree.add(3);
        tree.add(9);
        tree.add(11);
        tree.remove(5);
        String output = "";
        for(var n: tree)
            output += n+",";
        assertEquals(output,"3,6,7,9,11,");
        tree.remove(7);
        output = "";
        for(var n: tree)
            output += n+",";
        assertEquals(output,"3,6,9,11,");
        tree.remove(11);
        output = "";
        for(var n: tree)
            output += n+",";
        assertEquals(output,"3,6,9,");
    }

    public static void main(String[] args) {
        BST<Integer> tree = new BST<>();
        tree.add(7);
        tree.add(5);
        tree.add(6);
        tree.add(3);
        tree.add(9);
        tree.add(11);

        Iterator<Integer> iterator = tree.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }
}