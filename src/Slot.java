import java.awt.*;
import java.util.*;
public class Slot {
    private final int id;
    private final int xCoordinate, yCoordinate;
    private Stack<Container> containerStack;
    private final int maxHeight;
    private int containerX,containerY;
    private boolean active;

    public Slot(int id, int xCoordinate, int yCoordinate, int maxHeight, int containerX,int containerY) {
        this.id = id;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.maxHeight = maxHeight;
        this.containerX = containerX;
        this.containerY = containerY;
        this.active = false;
        containerStack = new Stack<>();
    }
    //container toevoegen aan slot
    public void addContainer(Container container) {
        //TODO : find out why random slots keep stacking
        if (!containerStack.isEmpty()) {
            // The java.util.Stack.peek() method in Java is used to retrieve or fetch the first element of the Stack or the element present at the top of the Stack. The element retrieved does not get deleted or removed from the Stack.
            containerStack.peek().setIsTop(false);
        }
        container.setIsTop(true);
        container.setZCoordinate(containerStack.size() + 1);
        containerStack.push(container);
    }
    //container verwijderen van slot
    public void removeContainer(Container container) {
        container.setIsTop(false);
        container.setZCoordinate((int) (container.getZCoordinate() - 1));
        if(!containerStack.isEmpty()) {
            if (containerStack.peek().getId() == container.getId()) {
                containerStack.pop();
            }
            if (!containerStack.isEmpty()) {
                containerStack.peek().setIsTop(true);
            }
        }
    }
    //controleren of het de max hoogte niet overschreidt
    public boolean safeHeight(int height) {
        if (containerStack.size() > height) {
            return false;
        }
        return true;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    //kijken welke container van boven zit
    public Container getTopContainer() {
        if (!containerStack.isEmpty()) {
            return containerStack.peek();
        }
        return null;
    }
    public Boolean hasContainers(){
        return !containerStack.isEmpty();
    }
    //kijken welke container vanonder zit
    public Container getBottomContainer(){
        if(!containerStack.isEmpty()){
            return containerStack.firstElement();
        }
        return null;
    }
    public int getStackSize(){
        return containerStack.size();
    }
    public int getId() {
        return id;
    }

    public int getXCoordinate() {
        return xCoordinate;
    }

    public int getYCoordinate() {
        return yCoordinate;
    }
    public int getTopContainerLength(){
        if(containerStack.size()<1){
            return 0;
        }
        else return getTopContainer().getSize();
    }
    public Stack<Container> getContainerStack() {
        return containerStack;
    }

    public  void print(){
        System.out.println("id: "+id+"   x: "+xCoordinate+" y: "+yCoordinate);

    }
    public void drawSlot(Graphics2D g2d){
        g2d.setColor(Color.BLACK);
        double newX = 50+((xCoordinate)*containerX);
        double newY = 50+((yCoordinate)*containerY);
        g2d.drawString(Integer.toString(id), (int) newX+(containerX/2)-5, (int) newY+(containerY/2));
        g2d.setColor(Color.white);
        g2d.drawString(Integer.toString(containerStack.size()), (int) newX+(containerX/2)-5, (int) newY+(containerY/2)+10);
    }
}
