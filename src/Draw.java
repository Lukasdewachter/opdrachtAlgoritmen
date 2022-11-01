import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.*;
import java.util.Scanner;
import javax.swing.*;

public class Draw extends JComponent implements ActionListener {
    Timer t = new Timer(500, this);
    private int w;
    private double x;
    private double y;
    private  double velX;
    private double velY;
    private int h;
    private Crane c;
    public Draw(int w, int h){
        this.w=w;
        this.h=h;
        c = new Crane(200,50);
        x=5;
        y=5;

    }
    @Override
    protected void paintComponent(Graphics g) {
        Scanner sc = new Scanner(System.in);
        this.x = sc.nextDouble();
        this.y = sc.nextDouble();
        this.velX = sc.nextDouble();
        this.velY = sc.nextDouble();
        Graphics2D g2d = (Graphics2D) g;
        Ellipse2D circle = new Ellipse2D.Double(x,y,30,30);
        g2d.fill(circle);
        t.setDelay(600000000);
        t.start();
        /*
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2d.setRenderingHints(rh);
        c.drawCrane(g2d);
        Ellipse2D.Double e = new Ellipse2D.Double(200,200,8,8);
        AffineTransform reset = g2d.getTransform(); //terug naar originele transformatie
        g2d.translate(300,0);
        g2d.setTransform(reset);
        g2d.fill(e);

        Rectangle2D.Double rect = new Rectangle2D.Double(300,250,40,80);
        g2d.setColor(Color.GREEN); //roteert wel
        g2d.rotate(Math.toRadians(20),300,150);
        g2d.fill(rect);
        g2d.setColor(Color.BLACK); //roteert niet
        g2d.setTransform(reset);
        g2d.fill(rect);
*/

    }
    public void actionPerformed(ActionEvent e){
        x += velX;
        y += velY;
        repaint();
        try {
            Thread.sleep(1000000);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}
