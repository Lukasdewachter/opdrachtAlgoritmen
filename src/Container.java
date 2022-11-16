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
    private double x,y,z;
    private boolean isParent;
    private boolean isChild;
    private boolean isTop;
    private boolean isBottom;
    private ArrayList<Integer> slotIds;
    public Container(int id, int length, List<Slot> slots){
        this.id = id;
        this.length = length;
        this.slots=slots;
        this.x=0;
        this.y=0;
        this.slotIds = new ArrayList<>();
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

    private void setCoordinates() {
        if(length==1){

        }
    }

    public void drawContainer(Graphics2D g2d){
        setCoordinates();
        g2d.setColor(Color.green);
        Rectangle2D rect = new Rectangle2D.Double(100+x*100, 50+y*50,100,50);
        g2d.fill(rect);
        g2d.setColor(Color.black);
        g2d.drawString("id: "+Integer.toString(id), (int) (140+x*100), (int) (72+y*50));
    }

}