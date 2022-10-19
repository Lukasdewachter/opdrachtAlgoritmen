import java.awt.*;
import java.awt.geom.*;

public class Crane {
    private int width;
    private int height;
    public Crane(int width, int height){
        this.width = width;
        this.height = height;
    }
    public void drawCrane(Graphics2D g2d){
        Rectangle2D.Double r = new Rectangle2D.Double(50,50,width,height);
        g2d.setColor(new Color(109, 128, 161));
        g2d.fill(r);
        Rectangle2D.Double l = new Rectangle2D.Double(100,45,width*0.15,height+10);
        g2d.setColor(new Color(100,149,237));
        g2d.fill(l);
    }
}
