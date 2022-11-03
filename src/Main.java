//from www.java2s.com
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JLabel label = new JLabel("Hallo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        List<Trajectory> list = new ArrayList<Trajectory>();
        int[] x = {10,5,3,6,1};
        int[] y = {1,4,1,4,1};
        int[] Vx = {10,5,20,5,1};
        int[] Vy = {10,5,20,5,1};
        for(int i=0; i<5;i++){
            Trajectory trajectory = new Trajectory(x[i], y[i], Vx[i], Vy[i]);
            list.add(trajectory);
        }
        frame.setVisible(true);
        frame.add(new TestPane(list));
        frame.pack();

    }
}

