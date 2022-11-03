public class Trajectory {
    private double x;
    private double y;
    private double Vx;
    private double Vy;
    public Trajectory(double x, double y, double Vx, double Vy){
        this.x=x;
        this.y=y;
        this.Vx=Vx;
        this.Vy=Vy;
    }

    public double getVx() {
        return Vx;
    }

    public double getVy() {
        return Vy;
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }
}
