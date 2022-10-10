import javax.swing.*;

public class SwingObserverExample {
    JFrame jFrame;

    public static void main(String[] args) {
        SwingObserverExample example = new SwingObserverExample();
        example.go();
    }

    public void go(){
        jFrame = new JFrame();

        JButton button = new JButton("할까? 말까?");
        button.addActionListener(e -> System.out.println("하지 마!"));
        button.addActionListener(e -> System.out.println("해"));
    }

}
