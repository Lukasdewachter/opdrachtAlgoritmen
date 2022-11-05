//from www.java2s.com
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        List<Trajectory> list = new ArrayList<Trajectory>();
        List<Trajectory> list2 = new ArrayList<Trajectory>();
        int[] x = {6,5,3,6,1};
        int[] y = {1,3,1,4,2};
        int[] Vx = {10,5,8,5,1};
        int[] Vy = {5,2,3,2,1};
        int[] x2 = {8,6,7,10,5};
        int[] y2 = {1,3,1,4,2};
        int[] Vx2 = {5,5,8,3,1};
        int[] Vy2 = {1,2,2,2,1};
        for(int i=0; i<5;i++){
            Trajectory trajectory = new Trajectory(x[i], y[i], Vx[i], Vy[i]);
            Trajectory trajectory2 = new Trajectory(x2[i],y2[i],Vx2[i], Vy2[i]);
            list.add(trajectory);
            list2.add(trajectory2);
        }
        frame.setVisible(true);
        frame.add(new TestPane(list, list2));
        frame.pack();

    }
}

