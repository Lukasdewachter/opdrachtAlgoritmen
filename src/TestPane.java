import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.List;
class TestPane extends JPanel {
    public Graphics2D g2d;
    private Color normal, correct;
    private Crane crane;
    private Crane crane2;
    List<Container> containers;
    List<Trajectory> trajectories1, trajectories2;
    Trajectory trajectory1, trajectory2;
    double x1 = 500, y1 = 50, xDelta1 = 0, yDelta1 = 0, xEnd1 = 0, yEnd1 =0;
    double x2 = 900, y2 = 50, xDelta2 = 0, yDelta2 = 0, xEnd2 = 0, yEnd2 =0;
    double xMax=0, xMax2=5;
    double xMin=4, xMin2=8;
    int count1 = 0,count2=0;
    int time =0;
    private Timer timer;
    private Boolean c1 = false, c2 = false;
    ActionListener al;
    public TestPane(List<Trajectory>t1,List<Trajectory>t2, List<Container> containers ) {
        this.trajectories1 =t1;
        this.trajectories2 = t2;
        correct = new Color(46, 255, 0);
        normal = new Color(100,149,237);
        crane = new Crane(50,200, x1, y1,xMax,xMin,0);
        crane2 = new Crane(50,200,x2,y2,xMax2,xMin2,1);
        trajectory1 = trajectories1.get(count1);
        trajectory2 = trajectories2.get(count2);
        loadTrajectory1();
        loadTrajectory2();
        this.containers = containers;
        al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                time ++;
                if(crane.moveCrane(xEnd1, yEnd1, xDelta1, yDelta1)&&!crane.overlapCraneArea(crane2)) {
                    if (count1 < 4) {
                        try {
                            Thread.sleep(1200);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        count1++;
                        trajectory1 = trajectories1.get(count1);
                        loadTrajectory1();
                    } else {
                        c1 = true;
                        setReady();
                    }
                }
                if(crane2.moveCrane(xEnd2, yEnd2, xDelta2, yDelta2)&&!crane2.overlapCraneArea(crane)) {
                    if (count2 < 4) {
                        try {
                            Thread.sleep(1200);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        count2++;
                        trajectory2 = trajectories2.get(count2);
                        loadTrajectory2();
                    } else {
                        c2=true;
                        setReady();
                    }
                }
                repaint();
            }
        };
        timer = new Timer(10,al);
        timer.start();
    }
    public void setReady(){
        if(c1 && c2){
            timer.stop();
        }
    }
    public void loadTrajectory1(){
        xEnd1 = trajectory1.getX();
        yEnd1 = trajectory1.getY();
        xDelta1 = trajectory1.getVx();
        yDelta1 = trajectory1.getVy();
    }
    public void loadTrajectory2(){
        xEnd2 = trajectory2.getX();
        yEnd2 = trajectory2.getY();
        xDelta2 = trajectory2.getVx();
        yDelta2 = trajectory2.getVy();
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1200, 300);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g2d = (Graphics2D) g;
        super.paintComponent(g);
        g2d.setColor(Color.RED);
        Rectangle2D rect = new Rectangle2D.Double(500,50,200,200);
        g2d.fill(rect);
        g.setColor(Color.black);
        g2d.drawString("Time: "+time,550,265);
        g2d.drawString("Current task 1: C:"+Integer.toString((int) xEnd1)+"   R: "+Integer.toString((int) yEnd1)+" " +
                "     Speed X:"+Integer.toString((int) xDelta1)+"  Y: "+Integer.toString((int) yDelta1),100,10);
        g2d.drawString("Current task 2: C:"+Integer.toString((int) xEnd2)+"   R: "+Integer.toString((int) yEnd2)+" " +
                "     Speed X:"+Integer.toString((int) xDelta2)+"  Y: "+Integer.toString((int) yDelta2),600,10);
        for(int i=1 ; i<6; i++){
            int multiplier = 50*i;
            g2d.drawLine(100,multiplier,1100,multiplier);
        }
        for(int i=1; i<12; i++){
            int multiplier = 100*i;
            g2d.drawLine(multiplier,50,multiplier,250);
        }
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2d.setRenderingHints(rh);
        for(Container c : containers){
            c.drawContainer(g2d);
        }
        crane.drawCrane(g2d);
        crane2.drawCrane(g2d);

    }
}