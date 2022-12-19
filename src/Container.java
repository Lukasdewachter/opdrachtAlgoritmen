import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Container {
    private int id;
    private int size;
    private int width,length,containerX,containerY;
    private Slot slot;
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
    public Container(int id, int size, Color color,int containerX,int containerY){
        this.id = id;
        this.size = size;
        this.color = color;
        this.width =0;
        this.length=0;
        this.containerX = containerX;
        this.containerY = containerY;
        this.slot=null;
    }
    public void setSlot(Slot slot){
        this.slot = slot;
    }

    public double getX() {
        if(size ==1){
            return slot.getXCoordinate();
        } else if (size==2) {
            return (slot.getXCoordinate()+0.5);
        } else if (size==3) {
            return (slot.getXCoordinate()+1);
        }
        else{
            System.out.println("unknown length");
        }
        return  0;
    }

    public double getY() {
        return y;
    }

    public void setCoordinates(){
        this.x = slot.getXCoordinate();
        this.y = slot.getYCoordinate();
    }

    public Slot getSlot() {
        return slot;
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
    public void drawContainer(Graphics2D g2d){
        setLength(containerX);
        setWidth(containerY);
        g2d.setColor(color);
        double newX = 50+((x)*length);
        double newY = 50+((y)*width);
        Rectangle2D rect = new Rectangle2D.Double(newX,newY,length*size,width);
        g2d.fill(rect);
        g2d.setColor(Color.black);
        if (isTop && containerX>30){
            g2d.drawString("id: "+Integer.toString(id)+" "+Integer.toString(size), (int) newX, (int) newY+10);
        }
    }
}