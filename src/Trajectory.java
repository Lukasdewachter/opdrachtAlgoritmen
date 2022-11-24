public class Trajectory {
    private int x,y, containerId;
    private double v;
    public Trajectory(int x, int y, double v, int containerId){
        this.x=x;
        this.y=y;
        this.v=v;
        this.containerId=containerId;
    }

    public int getContainerId() {
        return containerId;
    }

    public double getV(){return v;}
    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }
}
