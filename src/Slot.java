import java.util.*;
public class Slot {
    private final int id;
    private final int xCoordinate, yCoordinate;
    private Stack<Container> containerStack;

    public Slot(int id, int xCoordinate, int yCoordinate) {
        this.id = id;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }
    public void addContainer(Container container) {
        if (!containerStack.isEmpty()) {
            // The java.util.Stack.peek() method in Java is used to retrieve or fetch the first element of the Stack or the element present at the top of the Stack. The element retrieved does not get deleted or removed from the Stack.
            containerStack.peek().setIsTop(false);
        }
        container.setIsTop(true);
        container.setzCoordinate(containerStack.size() + 1);
        containerStack.push(container);
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
