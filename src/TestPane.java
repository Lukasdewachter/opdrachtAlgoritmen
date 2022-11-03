import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
class TestPane extends JPanel {
    public Graphics2D g2d;
    String text;
    Boolean ready = false;
    private Crane crane;
    List<Trajectory> trajectories;
    Trajectory trajectory;
    double x = 500,y = 50,xDelta = 0, yDelta = 0, xEnd = 0, yEnd=0 , yHead;
    int count = 0;
    public TestPane(List<Trajectory>t) {
        this.trajectories=t;
        crane = new Crane(50,200,x,y, y);
        trajectory = trajectories.get(count);
        loadTrajectory();
        Timer timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                x = crane.getX();
                yHead = crane.getYHead();
                double row = (yHead-5)/50;
                double collumn = x/100;
                if(collumn != xEnd || row != yEnd) {
                    if (collumn != xEnd) {
                        if (collumn < xEnd) {
                            if (x +xDelta >xEnd * 100) {
                                x = xEnd * 100;
                            } else {
                                x += xDelta;
                            }
                        } else {
                            if (x - xDelta < xEnd * 100) {
                                x = xEnd * 100;
                            } else {
                                x -= xDelta;
                            }
                        }
                        crane.setX(x);
                    }
                    if (row != yEnd) {
                        if (row < yEnd) {
                            if (row * 50 + yDelta > yEnd * 50) {
                                yHead = (yEnd * 50) + 5;
                            } else {
                                yHead += yDelta;
                            }
                        } else {
                            if (row * 50 - yDelta < yEnd * 50) {
                                yHead = (yEnd * 50) + 5;
                            } else {
                                yHead -= yDelta;
                            }
                        }
                        crane.setYHead(yHead);
                    }
                }else{
                    if (count<4){
                        try {
                            Thread.sleep(1200);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        count ++;
                        trajectory = trajectories.get(count);
                        loadTrajectory();

                    }else ready=true;
                }
                repaint();
            }
        });
        timer.start();
        Thread stopThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if(ready){
                        System.out.println("Ready");
                        timer.stop();}
                    }
            }
        });
        stopThread.start();
    }
    public void loadTrajectory(){
        xEnd = trajectory.getX();
        yEnd = trajectory.getY();
        xDelta = trajectory.getVx();
        yDelta = trajectory.getVy();
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1200, 300);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g2d = (Graphics2D) g;
        super.paintComponent(g);
        g.setColor(getBackground());
        g2d.setColor(Color.BLACK);
        g2d.drawString("Current task: C:"+Integer.toString((int)xEnd)+"   R: "+Integer.toString((int)yEnd)+" " +
                "     Speed X:"+Integer.toString((int)xDelta)+"  Y: "+Integer.toString((int)yDelta),500,10);
        g2d.drawString("Crane X: "+Double.toString(x-100)+" Y: "+Double.toString(yHead-55), 500,30);
        for(int i=1 ; i<6; i++){
            int multiplier = 50*i;
            g2d.drawLine(100,multiplier,1100,multiplier);
        }
        for(int i=1; i<12; i++){
            int multiplier = 100*i;
            g2d.drawLine(multiplier,50,multiplier,250);
        }
        g2d.setColor(Color.RED);
        crane.drawCrane(g2d);
        TextArea text = new TextArea();
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2d.setRenderingHints(rh);
    }
}