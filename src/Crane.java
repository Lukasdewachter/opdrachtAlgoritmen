import java.awt.*;
import java.awt.geom.*;

public class Crane {
    private int width;
    private int height;
    private double x;
    private double y;
    private double yHead;
    public Crane(int width, int height, double x, double y, double yHead){
        this.width = width;
        this.height = height;
        this.x=x;
        this.y=y;
        this.yHead = yHead + 5;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setYHead(double yHead) {
        this.yHead = yHead;
    }

    public double getYHead() {
        return yHead;
    }

    public void drawCrane(Graphics2D g2d){
        Rectangle2D.Double r = new Rectangle2D.Double(x+25,y,width,height);
        g2d.setColor(new Color(109, 128, 161));
        g2d.fill(r);
        Rectangle2D.Double l = new Rectangle2D.Double(x+18.75,yHead,width*1.25,height*0.2);
        g2d.setColor(new Color(100,149,237));
        g2d.fill(l);
    }
}
