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
        List<Container> containers = new ArrayList<>();
        List<Slot> slots = new ArrayList<>();
        Object obj2 = new JSONParser().parse(new FileReader("./input/terminal_4_3.json"));
        JSONTokener tokener2 = new JSONTokener(String.valueOf(obj2));
        JSONObject object2 = new JSONObject(tokener2);
        JSONArray jsonSlots = object2.getJSONArray("slots");
        for(int i=0; i<jsonSlots.length(); i++){
            JSONObject o = jsonSlots.getJSONObject(i);
            int id = o.getInt("id");
            int x = o.getInt("x");
            int y = o.getInt("y");
            Slot slot = new Slot(id,x,y,2);
            slots.add(slot);
        }
        JSONArray jsonContainers = object2.getJSONArray("containers");
        for(int i=0; i<jsonContainers.length();i++){
            int red = (int)(Math.random()*256);
            int green = (int)(Math.random()*256);
            int blue = (int)(Math.random()*256);
            Color color = new Color(red,green,blue);
            JSONObject o = jsonContainers.getJSONObject(i);
            int id = o.getInt("id");
            int length = o.getInt("length");
            Container container = new Container(id,length, color);
            containers.add(container);
        }
        JSONArray jsonAssignments = object2.getJSONArray("assignments");
        for(int i=0; i<jsonAssignments.length();i++){
            JSONObject o = jsonAssignments.getJSONObject(i);
            JSONArray slot_id = o.getJSONArray("slot_id");
            int[] slotId = new int[slot_id.length()];
            for(int j=0; j<slotId.length;j++){
                slotId[j] = slot_id.getInt(j);
            }
            int container_id = o.getInt("container_id");
            for(Container c : containers){
                if(c.getId() == container_id){
                    for(int s : slotId){
                        for(Slot slot : slots){
                            if(slot.getId() == s){
                                c.addSlot(slot);
                            }
                        }
                    }
                    c.setCoordinates();
                }
            }
        }
        for(Container c : containers){
            c.print();
        }
        frame.setVisible(true);
        frame.add(new TestPane(list, list2, containers));
        frame.pack();

    }
}

