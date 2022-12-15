import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
public class Container {
    private int id;
    private int size;
    private int width,length,containerX,containerY;
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
    public Container(int id, int length, Color color,int containerX,int containerY){
        this.id = id;
        this.size = size;
        slots = new ArrayList<>();
        this.color = color;
        this.width =0;
        this.length=0;
        this.containerX = containerX;
        this.containerY = containerY;
    }
    public void addSlot(Slot slot){
        slots.add(slot);
    }

    public double getY() {
        double medY=0;
        for(Slot slot : slots){
            if(slot!=null)medY +=slot.getyCoordinate();
        }
        medY = medY/slots.size();
        return medY;
    }

    public double getX() {
        return x;
    }

    public void setCoordinates(){
        this.x = slots.get(0).getxCoordinate();
        this.y = slots.get(0).getyCoordinate();
    }

    public List<Slot> getSlots() {
        return slots;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getId() {
        return id;
    }

    public void setZCoordinate(int z){
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

    public void setX(double x) {
        this.x = x;
    }
    public int getSize() {
        return size;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setSlots(List<Slot> slots) {
        this.slots = slots;
    }

    public String printSlots(){
        StringBuilder sb = new StringBuilder();
        for (Slot s : slots){
            if(s!=null) sb.append("sId: " +Integer.toString(s.getId()));
        }
        return sb.toString();
    }
    public void drawContainer(Graphics2D g2d){
        setLength(containerX);
        setWidth(containerY);
        g2d.setColor(color);
        double newX = 50+((x)*length);
        double newY = 50+((y)*width);
        Rectangle2D rect = new Rectangle2D.Double(newX,newY,length,width);
        g2d.fill(rect);
        g2d.setColor(Color.black);
        if (isTop && containerX>30){
            g2d.drawString("id: "+Integer.toString(id), (int) newX, (int) newY+10);
        }
    }
}