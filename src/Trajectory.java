public class Trajectory {
    private int x,y;
    private double v;
    public Trajectory(int x, int y, double v){
        this.x=x;
        this.y=y;
        this.v=v;
    }

    public double getV(){return v;}
    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }
}
