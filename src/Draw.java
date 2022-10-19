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
        Rectangle2D.Double r = new Rectangle2D.Double(50,75,100,200);
        g2d.setColor(new Color(100,149,237));
        g2d.fill(r);
    }
}
