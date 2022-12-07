//from www.java2s.com
import javax.swing.*;
import org.json.JSONTokener;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.JSONArray;

import java.awt.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
/*
    - Transformeren naar andere layouts
*/
public class Main {
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        List<Container> containers = new LinkedList<>();
        List<Slot> slots = new ArrayList<>();
        Object obj = new JSONParser().parse(new FileReader("./input/terminal22_1_100_1_10.json"));
        JSONTokener tokener = new JSONTokener(String.valueOf(obj));
        JSONObject object = new JSONObject(tokener);
        int startHeight = object.getInt("maxheight");
        int length = object.getInt("length");
        int width = object.getInt("width");
        JSONArray jsonSlots = object.getJSONArray("slots");
        for(int i=0; i<jsonSlots.length(); i++){
            JSONObject o = jsonSlots.getJSONObject(i);
            int id = o.getInt("id");
            int x = o.getInt("x")+1;
            int y = o.getInt("y")+1;
            Slot slot = new Slot(id,x,y,startHeight);
            slots.add(slot);
        }
        JSONArray jsonContainers = object.getJSONArray("containers");
        for(int i=0; i<jsonContainers.length();i++){
            int red = (int)(Math.random()*256);
            int green = (int)(Math.random()*256);
            int blue = (int)(Math.random()*256);
            Color color = new Color(red,green,blue);
            JSONObject o = jsonContainers.getJSONObject(i);
            int id = o.getInt("id");
            int clength = o.getInt("length");
            Container container = new Container(id,clength, color);
            containers.add(container);
        }
        JSONArray jsonAssignments = object.getJSONArray("assignments");
        List<Assignment>assignments = new ArrayList<>();
        for(int i=0; i<jsonAssignments.length();i++){
            JSONObject o = jsonAssignments.getJSONObject(i);
            int slotId = o.getInt("slot_id");
            int container_id = o.getInt("container_id");
            Assignment assignment = new Assignment(slotId, container_id);
            assignments.add(assignment);
            for(Container c : containers){
                if(c.getId() == container_id ){
                    for(Slot slot : slots) {
                        if (slot.getId() == slotId) {
                            c.addSlot(slot);
                            slot.addContainer(c);
                        }
                    }
                    c.setCoordinates();
                }
            }
        }
        JSONArray jsonCranes = object.getJSONArray("cranes");
        List<Crane>cranes = new ArrayList<>();
        for(int i=0; i<jsonCranes.length();i++) {
            JSONObject o = jsonCranes.getJSONObject(i);
            double x = o.getDouble("x");
            double y = o.getDouble("y");
            double ymin = o.getDouble("ymin");
            double ymax = o.getDouble("ymax");
            int id = o.getInt("id");
            double xspeed = o.getDouble("xspeed");
            double yspeed = o.getDouble("yspeed");
            double xmin = o.getDouble("xmin");
            double xmax = o.getDouble("xmax");
            Crane crane = new Crane(50,200,x,y,ymin,ymax,id,xspeed,yspeed,xmin,xmax);
            cranes.add(crane);
        }
        Object obj2 = new JSONParser().parse(new FileReader("./input/terminal22_1_100_1_10target.json"));
        JSONTokener tokener2 = new JSONTokener(String.valueOf(obj));
        JSONObject object2 = new JSONObject(tokener2);
        JSONArray jsonEndAssignments = object2.getJSONArray("assignments");
        List<Assignment>endAssignments = new ArrayList<>();
        for(int i=0; i<jsonEndAssignments.length();i++) {
            JSONObject o = jsonEndAssignments.getJSONObject(i);
            int slotId = o.getInt("slot_id");
            int containerId = o.getInt("container_id");
            Assignment assignment = new Assignment(slotId,containerId);
            endAssignments.add(assignment);
        }
        frame.setVisible(true);
        TestPane testPane = new TestPane(null,null,containers,slots,length,width);
        frame.add(testPane);
        testPane.moveContainer(3,2,4);
        frame.pack();

    }
}

