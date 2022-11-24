//from www.java2s.com
import javax.swing.*;
import org.json.JSONTokener;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.JSONArray;

import java.awt.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
/*
    - Transformeren naar andere layouts
*/
public class Main {
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        List<Trajectory> list = new ArrayList<Trajectory>();
        List<Trajectory> list2 = new ArrayList<Trajectory>();
        Object obj = new JSONParser().parse(new FileReader("./input/vectors.json"));
        JSONTokener tokener = new JSONTokener(String.valueOf(obj));
        JSONObject object = new JSONObject(tokener);
        JSONArray jsonArray = object.getJSONArray("vectors1");
        JSONArray jsonArray2 = object.getJSONArray("vectors2");
        for(int i=0; i<jsonArray.length(); i++){
            JSONObject object1 = jsonArray.getJSONObject(i);
            int x = object1.getInt("x");
            int y = object1.getInt("y");
            double v = object1.getDouble("v");
            Trajectory tr1 = new Trajectory(x,y,v);
            list.add(tr1);
            JSONObject object2 = jsonArray2.getJSONObject(i);
            int x2 = object2.getInt("x");
            int y2 = object2.getInt("y");
            double v2 = object2.getDouble("v");
            Trajectory tr2 = new Trajectory(x2,y2,v2);
            list2.add(tr2);
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

