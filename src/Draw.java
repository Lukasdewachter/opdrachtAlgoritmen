import java.awt.*;
import java.awt.geom.*;
import java.util.Scanner;
import javax.swing.*;

public class Draw extends JComponent{
    private int w;
    private int h;
    private Crane c;
    public Draw(int w, int h){
        this.w=w;
        this.h=h;
        c = new Crane(200,50);
    }
    @Override
    protected void paintComponent(Graphics g) {
        Scanner sc = new Scanner(System.in);
        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2d.setRenderingHints(rh);
        c.drawCrane(g2d);
        Ellipse2D.Double e = new Ellipse2D.Double(200,200,8,8);
        AffineTransform af = g2d.getTransform(); //terug naar originele transformatie
        g2d.translate(300,0);
        g2d.fill(e);


    }
}
