import java.util.*;
public class Slot {
    private final int id;
    private final int xCoordinate, yCoordinate;
    private Stack<Container> containerStack;
    private final int maxHeight;


    public Slot(int id, int xCoordinate, int yCoordinate, int maxHeight) {
        this.id = id;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.maxHeight = maxHeight;
    }
    //container toevoegen aan slot
    public void addContainer(Container container) {
        if (!containerStack.isEmpty()) {
            // The java.util.Stack.peek() method in Java is used to retrieve or fetch the first element of the Stack or the element present at the top of the Stack. The element retrieved does not get deleted or removed from the Stack.
            containerStack.peek().setIsTop(false);
        }
        container.setIsTop(true);
        container.setZCoordinate(containerStack.size() + 1);
        containerStack.push(container);
    }
    //container verwijderen van slot
    public Container removeContainer(Container container) {
        container.setIsTop(false);
        container.setZCoordinate(container.getZCoordinate() - 1);
        if (containerStack.peek().getId() == container.getId()) {
            containerStack.pop();
        } else {
            return null;
        }
        if (!containerStack.isEmpty()) {
            containerStack.peek().setIsTop(true);
        }
        return container;
    }
    //controleren of het de max hoogte niet overschreidt
    public boolean safeHeight(int height) {
        if (containerStack.size() > height) {
            return false;
        }
        return true;
    }
    //kijken welke container van boven zit
    public Container getTopContainer() {
        if (!containerStack.isEmpty()) {
            return containerStack.peek();
        }
        return null;
    }
    //kijken welke container vanonder zit
    public Container getBottomContainer(){
        if(!containerStack.isEmpty()){
            return containerStack.firstElement();
        }
        return null;
    }
    public int getId() {
        return id;
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }


}
