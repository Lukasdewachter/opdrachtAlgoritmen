import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;
public class Container {
    private int id;
    private int length;
    private List<Slot> slots;
    private double x,y;
    public Container(int id, int length, List<Slot> slots){
        this.id = id;
        this.length = length;
        this.slots=slots;
        this.x=0;
        this.y=0;
    }
    public void setCoordinates(){
        if(slots.size()==1){
            x = slots.get(0).getX();
            y = slots.get(0).getY();
        }else{

        }
    }
    boolean canStack(){
        if(slots.)
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