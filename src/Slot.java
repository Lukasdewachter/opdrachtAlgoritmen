public class Slot {
    private final int id;
    private final int centerX, centerY, xMin, xMax, yMin, yMax,z;
    private Slot parent;
    private Slot child;

    public Slot(int id, int centerX, int centerY, int xMin, int xMax, int yMin, int yMax, int z) {
        this.id = id;
        this.centerX = centerX;
        this.centerY = centerY;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.z = z;
    }

    public int getId() {
        return id;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public int getxMin() {
        return xMin;
    }

    public int getxMax() {
        return xMax;
    }

    public int getyMin() {
        return yMin;
    }

    public int getyMax() {
        return yMax;
    }

    public int getZ() {
        return z;
    }

    public Slot getParent() {
        return parent;
    }

    public void setParent(Slot parent) {
        this.parent = parent;
    }

    public Slot getChild() {
        return child;
    }

    public void setChild(Slot child) {
        this.child = child;
    }
}
