import java.awt.*;
import java.awt.geom.*;

public class Crane {
    private boolean hasContainer;
    Container container;
    private int width, length, id,containerX,containerY;
    private double  x,y,xmin, xmax, xspeed,yspeed,ymin,ymax;
    Color body, head;
    public Crane(int length, int width, double x, double y, double ymin, double ymax,int id,double xspeed,double yspeed,double xmin,double xmax, int containerX, int containerY){
        this.width = width;
        this.length = length;
        this.x=x;
        this.y = y;
        this.id = id;
        this.ymin = ymin;
        this.ymax = ymax;
        this.xmin = xmin;
        this.xmax = xmax;
        this.xspeed = xspeed;
        this.yspeed = yspeed;
        this.container = null;
        this.hasContainer = false;
        this.containerX = containerX;
        this.containerY = containerY;
        body = new Color(109, 128, 161);
        head = new Color(100,149,237);
    }

    public void setContainer(boolean hasContainer, Container container) {
        this.hasContainer = hasContainer;
        this.container = container;
    }

    public boolean getHasContainer(){
        return hasContainer;
    }

    public boolean moveCrane(double xEnd, double yEnd){
        if(x != xEnd || y != yEnd) {
            if (x != xEnd) {
                if (x < xEnd) {
                    if (x +xspeed >xEnd) {
                        x = xEnd;
                    } else {
                        x += xspeed;
                    }
                } else {
                    if (x - xspeed < xEnd) {
                        x = xEnd;
                    } else {
                        x -= xspeed;
                    }
                }
                //setX(x);
                if(container != null){
                    container.setX(x);
                }
            }
            if (y != yEnd) {
                if (y < yEnd) {
                    if (y + yspeed > yEnd) {
                        y = yEnd;
                    } else {
                        y += yspeed;
                    }
                } else {
                    if (y - yspeed < yEnd) {
                        y = yEnd;
                    } else {
                        y -= yspeed;
                    }
                }
                if(container != null){
                    container.setY(y);
                }

            }
        }else{
            return true;
        }
        return false;
    }
    /*
    public boolean overlapCraneArea(Crane c) {
        return c.xMin < xMax && xMin < c.xMax;
    }
    public double[] getOverlapArea(Crane c) {

        double maxmin = Math.max(xMin, c.xMin);
        double minmax = Math.min(xMax, c.xMax);
        if (minmax < maxmin)
            return null;
        else
            return new double[]{maxmin, minmax};
    }*/

    public void setWidth(int width) {
        this.width = width;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void drawCrane(Graphics2D g2d){
        double newX = (50+(containerX*0.2)/2)+((x)*containerX);
        double newY = 50+((y)*containerY);
        Rectangle2D.Double r = new Rectangle2D.Double(newX,50,containerX*0.80,containerY*width);
        g2d.setColor(body);
        g2d.fill(r);
        Rectangle2D.Double l = new Rectangle2D.Double((50-0.125*containerX)+((x)*containerX),50+((y)*containerY)+((containerY*0.6)/2),containerX*1.25,containerY*0.4);
        g2d.setColor(head);
        g2d.fill(l);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Crane "+(id +1)+": "+Double.toString(x)+" Y: "+Double.toString(y), 100+500* id,30);
    }
}