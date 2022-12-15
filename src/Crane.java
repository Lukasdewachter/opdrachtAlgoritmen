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
        double row = (y -5)/50;
        double collumn = x/100;
        if(collumn != xEnd || row != yEnd) {
            if (collumn != xEnd) {
                if (collumn < xEnd) {
                    if (x +xspeed >xEnd * 100) {
                        x = xEnd * 100;
                    } else {
                        x += xspeed;
                    }
                } else {
                    if (x - xspeed < xEnd * 100) {
                        x = xEnd * 100;
                    } else {
                        x -= xspeed;
                    }
                }
                //setX(x);
                if(container != null){
                    container.setX(x/100);
                }
            }
            if (row != yEnd) {
                if (row < yEnd) {
                    if (row * 50 + yspeed > yEnd * 50) {
                        y = (yEnd * 50) + 5;
                    } else {
                        y += yspeed;
                    }
                } else {
                    if (row * 50 - yspeed < yEnd * 50) {
                        y = (yEnd * 50) + 5;
                    } else {
                        y -= yspeed;
                    }
                }
                //setYHead(y);
                if(container!=null){
                    if(container.getSlot().size()>1){
                        container.setY((y -30)/50);
                    }else{
                        container.setY((y -5)/50);
                    }
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