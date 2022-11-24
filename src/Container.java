import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
public class Container {
    private int id;
    private int length;
    private List<Slot> slots;
    private int slotEndIndex;
    private int slotStartIndex;
    //Z = height
    private double x=0,y=0,z;
    private Color color;
    private boolean isParent;
    private boolean isChild;
    private boolean isTop;
    private boolean isBottom;
    private ArrayList<Integer> slotIds;
    public Container(int id, int length, Color color){
        this.id = id;
        this.length = length;
        this.slotIds = new ArrayList<>();
        slots = new ArrayList<>();
        this.color = color;
    }
    public void addSlot(Slot slot){
        slots.add(slot);
    }
    public void setCoordinates(){
        this.x = slots.get(0).getxCoordinate();
        this.y = slots.get(0).getyCoordinate();
    }

    public List<Slot> getSlots() {
        return slots;
    }

    public int getLength() {
        return length;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setZCoordinate(double z){
        this.z=z;
    }
    public double getZCoordinate(){
        return z;
    }
    public void setIsChild(boolean b){
        this.isChild = b;
    }
    public void setIsParent(boolean b){
        this.isParent = b;
    }
    public void setIsTop(boolean b){
        this.isTop = b;
    }
    public void setIsBottom(boolean b){
        this.isBottom = b;
    }
    public boolean isChild(){
        return isChild;
    }
    public boolean isParent(){
        return isParent;
    }
    public boolean isTop(){
        return isTop;
    }
    public boolean isBottom(){
        return isBottom;
    }
    public void print(){
        System.out.println("container: "+getId()+"    length: "+getLength()+"    Slots: "+printSlots());
    }
    public String printSlots(){
        StringBuilder sb = new StringBuilder();
        for (Slot s : slots){
            sb.append("sId: " +Integer.toString(s.getId()));
        }
        return sb.toString();
    }
    public void drawContainer(Graphics2D g2d){
        setCoordinates();
        g2d.setColor(color);
        Rectangle2D rect = new Rectangle2D.Double(100+x*100, 50+y*50,100,50*length);
        g2d.fill(rect);
        g2d.setColor(Color.black);
        g2d.drawString("id: "+Integer.toString(id), (int) (140+x*100), (int) (72+y*50));
    }

}