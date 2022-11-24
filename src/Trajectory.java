public class Trajectory {
    private int containerId;
    private double x,y,v;
    public Trajectory(double x, double y, double v, int containerId){
        this.x=x;
        this.y=y;
        this.v=v;
        this.containerId=containerId;
    }

    public int getContainerId() {
        return containerId;
    }

    public double getV(){return v;}
    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }
}
