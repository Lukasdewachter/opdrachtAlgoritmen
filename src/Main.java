import javax.swing.*;
import java.awt.*;
import java.applet.*;
public class Main {
    //tutorial: https://www.youtube.com/watch?v=zCiMlbu1-aQ&ab_channel=choobtorials

    public static void main(String[] args) throws Exception {
        JFrame f = new JFrame();
        Draw d = new Draw(640, 480);
        f.setSize(640,480);
        f.setTitle("Opdracht");
        f.add(d);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

}