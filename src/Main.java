//from www.java2s.com
import javax.swing.JFrame;
import java.awt.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        System.out.println("Naar welke locatie wilt u gaan? ");
        Scanner sc = new Scanner(System.in);
        double inputX = sc.nextDouble();
        double inputY = sc.nextDouble();
        frame.setVisible(true);
        frame.add(new TestPane(inputX, inputY));
        frame.pack();

    }
}

