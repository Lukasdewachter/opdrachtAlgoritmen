import java.awt.*;
import java.awt.geom.*;

public class Crane {
    private int width, height,craneNr;
    private double  x,y,yHead;
    public Crane(int width, int height, double x, double y, int craneNr){
        this.width = width;
        this.height = height;
        this.x=x;
        this.y=y;
        this.yHead = y + 5;
        this.craneNr = craneNr;
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
            }
        }else{
            return true;
        }
        return false;
    }

    public void drawCrane(Graphics2D g2d){
        Rectangle2D.Double r = new Rectangle2D.Double(x+25,y,width,height);
        g2d.setColor(new Color(109, 128, 161));
        g2d.fill(r);
        Rectangle2D.Double l = new Rectangle2D.Double(x+18.75,yHead,width*1.25,height*0.2);
        g2d.setColor(new Color(100,149,237));
        g2d.fill(l);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Crane "+(craneNr+1)+": "+Double.toString(x-100)+" Y: "+Double.toString(yHead-55), 100+500*craneNr,30);

    }
}
