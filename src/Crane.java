import java.awt.*;
import java.awt.geom.*;

public class Crane {
    private boolean hasContainer;
    Container container;
    private int width, height, id;
    private double  x,y,yHead, xMin, xMax;
    Color body, head;
    public Crane(int width, int height, double x, double y, double ymin, double ymax,int id,double xspeed,double yspeed,double xmin,double xmax){
        this.width = width;
        this.height = height;
        this.x=x;
        this.y=y;
        this.yHead = y;
        this.id = id;
        this.container = null;
        this.hasContainer = false;
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

    public double getX() {
        return x;
    }
    public void setColor(Color correct){
        this.head = correct;
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

    public double getxMax() {
        return xMax;
    }

    public void setxMax(double xMax) {
        this.xMax = xMax;
    }

    public void setxMin(double xMin) {
        this.xMin = xMin;
    }

    public double getxMin() {
        return xMin;
    }

    public void setYHead(double yHead) {
        this.yHead = yHead;
    }

    public double getYHead() {
        return yHead;
    }
    public boolean moveCrane(double xEnd, double yEnd, double xDelta, double yDelta){
        double row = (yHead-5)/50;
        double collumn = x/100;
        if(collumn != xEnd || row != yEnd) {
            if (collumn != xEnd) {
                if (collumn < xEnd) {
                    if (x +xDelta >xEnd * 100) {
                        x = xEnd * 100;
                    } else {
                        x += xDelta;
                    }
                } else {
                    if (x - xDelta < xEnd * 100) {
                        x = xEnd * 100;
                    } else {
                        x -= xDelta;
                    }
                }
                setX(x);
                if(container != null){
                    container.setX(x/100);
                }
            }
            if (row != yEnd) {
                if (row < yEnd) {
                    if (row * 50 + yDelta > yEnd * 50) {
                        yHead = (yEnd * 50) + 5;
                    } else {
                        yHead += yDelta;
                    }
                } else {
                    if (row * 50 - yDelta < yEnd * 50) {
                        yHead = (yEnd * 50) + 5;
                    } else {
                        yHead -= yDelta;
                    }
                }
                setYHead(yHead);
                if(container!=null){
                    if(container.getSlots().size()>1){
                        container.setY((yHead-30)/50);
                    }else{
                        container.setY((yHead-5)/50);
                    }
                }
            }
        }else{
            return true;
        }
        return false;
    }
    public boolean overlapCraneArea(Crane c) {
        return c.xMin < xMax && xMin < c.xMax;

    }
    //
    public double[] getOverlapArea(Crane c) {

        double maxmin = Math.max(xMin, c.xMin);
        double minmax = Math.min(xMax, c.xMax);
        if (minmax < maxmin)
            return null;
        else
            return new double[]{maxmin, minmax};
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void drawCrane(Graphics2D g2d, int width, int height,int totalVerticalSlots){
        setWidth(width);
        setHeight(height);
        double newX = 50+(x*width);
        double newY = 50+(y*height);
        Rectangle2D.Double r = new Rectangle2D.Double(newX,50,width,totalVerticalSlots*height);
        g2d.setColor(body);
        g2d.fill(r);
        Rectangle2D.Double l = new Rectangle2D.Double((50-0.125*width)+(x*width),50+(yHead*height)+((height*0.8)/2),width*1.25,height*0.2);
        g2d.setColor(head);
        g2d.fill(l);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Crane "+(id +1)+": "+Double.toString(x-100)+" Y: "+Double.toString(yHead-55), 100+500* id,30);
    }
}