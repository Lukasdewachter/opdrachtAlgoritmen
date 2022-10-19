import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class Draw extends JComponent{
    private int w;
    private int h;
    public Draw(int w, int h){
        this.w=w;
        this.h=h;
    }
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2d.setRenderingHints(rh);
        Rectangle2D.Double r = new Rectangle2D.Double(50,50,540,380);
        g2d.setColor(new Color(100,149,237));
        g2d.fill(r);
        Line2D.Double l = new Line2D.Double(100,200,200,100);
        g2d.setColor(new Color(2,1,1));
        g2d.draw(l);
        Ellipse2D.Double e = new Ellipse2D.Double(200,200,8,8);
        g2d.fill(e);

    }
}
