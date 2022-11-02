import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.*;
import java.util.Scanner;
import javax.swing.*;


/*
*
*
* Wordt enkel nog gebruikt om dingen uit te kopiëren
* 
*
*
* */
public class Draw extends JComponent implements ActionListener {
    Timer t = new Timer(0, this);
    private int w;
    private double x;
    private double y;
    private double newX;
    private double newY;
    private  double velX;
    private double velY;
    private int h;
    private Crane c;
   //private Ellipse2D circle = new Ellipse2D.Double(5,5,30,30);
    public Draw(int w, int h){
        this.w=w;
        this.h=h;
        c = new Crane(200,50);
        x=0;
        y=0;
        this.newX=0;
        this.newY=0;
    }
    @Override
    protected void paintComponent(Graphics g) {
        System.out.println("x: "+x+ " y: "+y);
        Graphics2D g2d = (Graphics2D) g;
        Ellipse2D circle = new Ellipse2D.Double(x,y,30,30);
        g2d.fill(circle);
        System.out.println("input: x y velX velY");
        Scanner sc = new Scanner(System.in);
        this.newX = sc.nextDouble();
        this.newY = sc.nextDouble();
        this.velX = sc.nextDouble();
        this.velY = sc.nextDouble();
        //moveObject(newX,newY, circle);
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
    public double getXCoord(){
        return this.x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void moveObject(double newX, double newY, Ellipse2D circle){
            if(x+velX < newX && y+velY < newY){
                this.x += velX;
                this.y += velY;
            }
            else if(x+velX < newX){
                this.x += velX;
                this.y = newY;
            }
            else if (y+velY < newY){
                this.x = newX;
                this.y += velY;
            }
            else{
                this.x = newX;
                this.y = newY;
            }
            setX(x);
            setY(y);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            repaint((int)x,(int)y,30,30);
        }
    public void actionPerformed(ActionEvent e){
            x += velX;
            y += velY;
            setX(x);
            setY(y);
            setLocation((int)x,(int)y);
            repaint();
    }
}
