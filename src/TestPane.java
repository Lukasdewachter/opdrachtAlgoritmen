import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
class TestPane extends JPanel {
    Boolean ready = false;
    private Crane crane;
    List<Trajectory> trajectories;
    Trajectory trajectory;
    double x = 500,y = 50,xDelta = 5, yDelta = 4, xEnd = 150, yEnd = 150, yHead;
    int count = 0;
    public TestPane(List<Trajectory>t) {
        this.trajectories=t;
        crane = new Crane(50,200,x,y, y);
        trajectory = trajectories.get(count);
        Timer timer = new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                x = crane.getX();
                yHead = crane.getYHead();
                double row = (yHead-5)/50;
                double collumn = x/100;
                if(collumn != trajectory.getX()){
                    if(collumn < trajectory.getX()){
                        if(x + trajectory.getVx() > trajectory.getX()*100){
                            x = trajectory.getX()*100;
                        }else{
                            x+=trajectory.getVx();
                        }
                    }
                    else {
                        if(x - trajectory.getVx() < trajectory.getX()*100){
                            x = trajectory.getX()*100;
                        }else {
                            x-=trajectory.getVx();
                        }
                    }
                    crane.setX(x);
                    repaint();
                }else{
                    if(row != trajectory.getY()){
                        if(row < trajectory.getY()){
                            if(row*50 + trajectory.getVy() > trajectory.getY()*50){
                                yHead = (trajectory.getY()*50)+5;
                            }else{
                                yHead+=trajectory.getVy();
                            }
                        }
                        else {
                            if(row*50 - trajectory.getVy() < trajectory.getY()*50){
                                yHead = (trajectory.getY()*50)+5;
                            }else{
                                yHead-=trajectory.getVy();
                            }
                        }
                        crane.setYHead(yHead);
                        repaint();
                    }else {
                        if (count<4){
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                            count ++;
                            trajectory = trajectories.get(count);
                        }else ready=true;
                    }
                }
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

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1200, 300);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g);
        g.setColor(getBackground());
        g2d.setColor(Color.BLACK);
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
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2d.setRenderingHints(rh);
    }
}