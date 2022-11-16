//from www.java2s.com
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
/*
    - Transformeren naar andere layouts
*/
public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        List<Trajectory> list = new ArrayList<Trajectory>();
        List<Trajectory> list2 = new ArrayList<Trajectory>();
        int[] x = {6,5,5,6,1};
        int[] y = {1,3,1,4,2};
        int[] Vx = {10,5,8,5,1};
        int[] Vy = {5,2,3,2,1};
        int[] x2 = {8,4,2,10,5};
        int[] y2 = {1,3,1,4,2};
        int[] Vx2 = {5,5,8,3,1};
        int[] Vy2 = {1,2,2,2,1};
        for(int i=0; i<5;i++){
            Trajectory trajectory = new Trajectory(x[i], y[i], Vx[i], Vy[i]);
            Trajectory trajectory2 = new Trajectory(x2[i],y2[i],Vx2[i], Vy2[i]);
            list.add(trajectory);
            list2.add(trajectory2);
        }
        List<Slot> slots1 = new ArrayList<>();
        List<Slot> slots2 = new ArrayList<>();
        List<Slot> slots3 = new ArrayList<>();
        List<Slot> slots4 = new ArrayList<>();
        Slot s1 = new Slot(0,0, 0,5);
        slots1.add(s1);
        Slot s2 = new Slot(0,1, 0,5);
        slots2.add(s2);
        Slot s3 = new Slot(0,2, 0,5);
        slots3.add(s1);
        slots3.add(s2);
        slots4.add(s3);
        List<Container> containers = new ArrayList<>();
        Container c1 = new Container(1,1,slots1);
        containers.add(c1);
        Container c2 = new Container(2,1,slots2);
        containers.add(c2);
        Container c3 = new Container(3,2,slots3);
        containers.add(c3);
        frame.setVisible(true);
        frame.add(new TestPane(list, list2, containers));
        frame.pack();

    }
}

