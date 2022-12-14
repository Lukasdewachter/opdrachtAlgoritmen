import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Crane {
    private boolean hasContainer,isCompleted, blocked;
    Container container;
    private int width, length, id,containerX,containerY;
    private double  x,y,xmin, xmax, xspeed,yspeed,ymin,ymax,xEnd,yEnd;
    private double [] restricted;
    private String currentMove;
    Color body, head;
    Assignment currentAssignment;
    List<Slot> slots;
    HashMap<Integer,Container> containers;
    List<String>moves;
    List<Crane>cranes;

    public Crane(int length, int width, double x, double y, double ymin, double ymax,int id,double xspeed,double yspeed,double xmin,double xmax, int containerX, int containerY, List<Slot> slots,HashMap<Integer,Container>containers){
        this.containers = containers;
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
        this.currentAssignment=null;
        this.slots= slots;
        this.xEnd =0;
        this.yEnd =0;
        this.cranes=null;
        this.restricted = null;
        this.currentMove =null;
        this.blocked = false;
        this.moves = new ArrayList<>();
        body = new Color(109, 128, 161);
        head = new Color(100,149,237);
    }

    public void setContainer(boolean hasContainer, Container container) {
        this.hasContainer = hasContainer;
        this.container = container;
    }
    public void setCranes(List<Crane>cranes){
        this.cranes = cranes;
    }
    public boolean getHasContainer(){
        return hasContainer;
    }

    public boolean moveCrane(){
        //calculates distance between cranes also using the move it wants to make
        double distance=calculateDistance(0);
        if(distance>1){
            this.blocked = false;
        }
        if(!blocked) {
            if (x != xEnd || y != yEnd) {
                if (x != xEnd) {
                    if (x < xEnd && calculateDistance(xspeed)>1) {
                        if (x + xspeed > xEnd) {
                            x = xEnd;
                        } else {
                            x += xspeed;
                        }
                    } else if (x > xEnd && calculateDistance(xspeed*-1)>1){
                        if (x - xspeed < xEnd) {
                            x = xEnd;
                        } else {
                            x -= xspeed;
                        }
                    }else{
                        setBlocked(true);
                    }
                    //setX(x);
                    if (hasContainer) {
                        if (container.getSize() == 1) {
                            container.setX(x);
                        } else if (container.getSize() == 2) {
                            container.setX(x - 0.5);
                        } else if (container.getSize() == 3) {
                            container.setX(x - 1);
                        }
                    }
                }
                if (y != yEnd) {
                    if (y < yEnd) {
                        if (y + yspeed > yEnd) {
                            y = yEnd;
                        } else {
                            y += yspeed;
                        }
                    } else if (y > yEnd){
                        if (y - yspeed < yEnd) {
                            y = yEnd;
                        } else {
                            y -= yspeed;
                        }
                    }else{
                       setBlocked(true);
                    }
                    if (hasContainer) {
                        container.setY(y);

                    }

                }
            } else {
                return true;
            }
        }else {
            Crane crane = null;
            if (id == 0) {
                crane = cranes.get(1);
            } else if (id == 1) {
                crane = cranes.get(0);
            } else {
                System.out.println("Error no crane");
            }
            if (crane != null) {
                if (crane.isBlocked()) {
                    crane.getX();
                    if (id == 0) {
                        x -= xspeed;
                    }
                    if (id == 1) {
                        x += xspeed;
                    }
                }
            }
            setBlocked(false);
        }
        return false;
    }
    public void moveCraneOneStep(Assignment otherAssignment){
        if(otherAssignment != null) {
            Crane otherCrane = null;
            for(Crane cr : cranes){
                if(id != cr.getId()){
                    otherCrane = cr;
                }
            }
            Container otherContainer = containers.get(otherAssignment.getContainerId());
            if (id == 1) {
                if (hasContainer) {
                    if (container.getSize() == 1) {
                        container.setX(x + xspeed);
                    } else if (container.getSize() == 2) {
                        container.setX((x + xspeed) - 0.5);
                    } else if (container.getSize() == 3) {
                        container.setX((x + xspeed) - 1);
                    }
                }
                x += xspeed;
                if (x - 2 >= otherCrane.getX()) {
                    for(Crane cr : cranes){
                        cr.setBlocked(false);
                    }
                }
            } else {
                if (hasContainer) {
                    if (container.getSize() == 1) {
                        container.setX(x - xspeed);
                    } else if (container.getSize() == 2) {
                        container.setX((x - xspeed) - 0.5);
                    } else if (container.getSize() == 3) {
                        container.setX((x - xspeed) - 1);
                    }
                }
                x -= xspeed;
                if (x + 2 <= otherCrane.getX()) {
                    for(Crane cr : cranes){
                        cr.setBlocked(false);
                    }
                }
            }
        }else{
            if (id == 1) {
                if (hasContainer) {
                    if (container.getSize() == 1) {
                        container.setX(x + xspeed);
                    } else if (container.getSize() == 2) {
                        container.setX((x + xspeed) - 0.5);
                    } else if (container.getSize() == 3) {
                        container.setX((x + xspeed) - 1);
                    }
                }
                x += xspeed;
                this.blocked = false;
            } else {
                if (hasContainer) {
                    if (container.getSize() == 1) {
                        container.setX(x - xspeed);
                    } else if (container.getSize() == 2) {
                        container.setX((x - xspeed) - 0.5);
                    } else if (container.getSize() == 3) {
                        container.setX((x - xspeed) - 1);
                    }
                }
                x -= xspeed;
                this.blocked = false;
            }
        }
    }
    public void moveFromRestricted(int time){
        setBlocked(false);
        if(id ==0){
            for(Slot s : slots){
                if(s.getXCoordinate() == restricted[0]-2 && s.getYCoordinate() == y){
                    setCurrentAssignment(new Assignment(s.getId(),-1,false),time);
                }
            }
        }else if(id ==1){
            for(Slot s : slots){
                if(s.getXCoordinate() == restricted[1]+2 && s.getYCoordinate() == y){
                    setCurrentAssignment(new Assignment(s.getId(),-1,false),time);
                    break;
                }
            }
        }
        moveCrane();
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
    public double calculateDistance(double vx){
        double distance=99;
        for(Crane c : cranes){
            if(c.getId() != getId()){
                if(c.getX() < getX()){
                    distance = (getX()+vx)-c.getX();
                }
                if(c.getX() > getX()){
                    distance = c.getX()-(getX()+vx);
                }
                break;
            }
        }
        return distance;
    }
    public int getId(){
        return this.id;
    }
    public void setCurrentAssignment(Assignment currentAssignment,int time) {
        this.currentAssignment = currentAssignment;
        if (currentAssignment != null) {
            if (!hasContainer) {
                if (currentAssignment.getContainerId() != -1) {
                    Container c = containers.get(currentAssignment.getContainerId());
                    currentMove = id + ";" + currentAssignment.getContainerId() + ";" + c.getX() + ";" + c.getY();
                    setXEnd(c.getX());
                    setYEnd(c.getY());
                } else {
                    Slot s = slots.get(currentAssignment.getSlotId());
                    currentMove = id + "; ;" + s.getXCoordinate() + ";" + s.getYCoordinate();
                    setXEnd(slots.get(currentAssignment.getSlotId()).getXCoordinate());
                    setYEnd(slots.get(currentAssignment.getSlotId()).getYCoordinate());
                }
            } else {
                System.out.println("heeft al container");
            }
        } else {
            currentMove = currentMove + ";" + x + ";" + y + ";" + time;
            moves.add(currentMove);
        }
    }
    public Assignment getCurrentAssignment() {
        return currentAssignment;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
    public boolean isBlocked() {
        return blocked;
    }
    public void setRestricted(double[] restricted){
        this.restricted = restricted;
    }
    public void setHasContainer(boolean hasContainer,int time) {
        double tempx = slots.get(currentAssignment.getSlotId()).getXCoordinate();
        double tempy = slots.get(currentAssignment.getSlotId()).getYCoordinate();
        if(container.getSize()==2){
            tempx+=0.5;
        }
        if(container.getSize()==3){
            tempx+=1;
        }
        setXEnd(tempx);
        setYEnd(tempy);
        this.hasContainer = hasContainer;
        if(hasContainer) {
            currentMove = currentMove + ";" + time;
        }
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Container getContainer(){
        if(container != null){
            return container;
        }
        else return null;
    }
    public void setCompleted(Boolean b){
        this.isCompleted = b;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public double getXMin() {
        return xmin;
    }

    public double getXMax() {
        return xmax;
    }

    public void setXEnd(double xEnd) {
        this.xEnd = xEnd;
    }

    public void setYEnd(double yEnd) {
        this.yEnd = yEnd;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public void setLength(int length) {
        this.length = length;
    }

    public List<String> getMoves() {
        return moves;
    }
    public void addMove(String move){
        moves.add(move);
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
        if(currentAssignment != null) {
            g2d.drawString("Crane " + (id + 1) + ": " + Double.toString(x) + " Y: " + Double.toString(y) + "        Assignment: " + Integer.toString(currentAssignment.getContainerId()) + " to slot " + Integer.toString(currentAssignment.getSlotId()), 100 + 500 * id, 30);
        }
        g2d.setColor(Color.RED);
        Rectangle2D.Double maxRect = new Rectangle2D.Double(50+(containerX*xmin),50+(containerY*ymin),(containerX*((xmax+1)-xmin)),(containerY*(ymax-ymin)));
        g2d.draw(maxRect);
    }
}