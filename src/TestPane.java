import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.List;
class TestPane extends JPanel {
    public Graphics2D g2d;
    private Color normal, correct;
    private Crane crane;
    private Crane crane2;
    List<Container> containers;
    List<Trajectory> trajectories1, trajectories2;
    List<Crane>cranes;
    Trajectory trajectory1, trajectory2;
    double x1 = 500, y1 = 50, v1 = 0, yDelta1 = 0, xEnd1 = 0, yEnd1 =0;
    double x2 = 900, y2 = 50, v2 = 0, yDelta2 = 0, xEnd2 = 0, yEnd2 =0;
    double xMax=0, xMax2=5;
    double xMin=4, xMin2=8;
    int count1 = 0,count2=0;
    int length,width,time=0;
    int containerX=0,containerY=0;
    private List<Slot>slots;
    private Timer timer;
    private Container container;
    private Boolean c1 = false, c2 = false,containerAttached;
    ActionListener al;
    public TestPane(List<Trajectory>t1,List<Trajectory>t2,List<Crane>cranes, List<Container> containers,List<Slot>slots, int length, int width ) {
        this.trajectories1 =t1;
        this.trajectories2 = t2;
        this.length = length;
        this.width=width;
        correct = new Color(46, 255, 0);
        normal = new Color(100,149,237);
        //trajectory1 = trajectories1.get(count1);
        //trajectory2 = trajectories2.get(count2);
        this.slots = slots;
        //loadTrajectory1();
        //loadTrajectory2();
        this.containers = containers;
        this.container=null;
        this.cranes = cranes;
        al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                time ++;
                if(!crane.getHasContainer()){
                    for(Container c : containers){
                        if(c.getId() == trajectory1.getContainerId()){
                            setContainer(c);
                        }
                    }
                    if(container!=null){
                        double xContainer = container.getX();
                        double yContainer = container.getY();
                        List<Slot>containerSlots = container.getSlots();
                        if(crane.moveCrane(xContainer,yContainer,v1,1)){
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                            for(Slot s : containerSlots){
                                if(s.getTopContainer().equals(container)) {
                                    s.removeContainer(container);
                                    containers.remove(containers.indexOf(container));
                                    containers.add(container);
                                    crane.setContainer(true,container);
                                }else{
                                    System.out.println("Can't pick this container");
                                    break;
                                }
                            }
                        }
                    }
                    else{
                        System.out.println("Error container null");
                    }
                }
                else if(crane.moveCrane(xEnd1, yEnd1, v1, 1)&&!crane.overlapCraneArea(crane2)) {
                    if (count1 < trajectories1.size()) {
                        List<Slot>containerSlots = container.getSlots();
                        Slot newSlot = getSlot((int)(xEnd1),(int)yEnd1);
                        Slot secondSlot=null;
                        if(container.getSize()>1){
                            secondSlot = getSlot((int)xEnd1,(int)yEnd1+1);
                            if(newSlot.getContainerStack().size() == secondSlot.getContainerStack().size() && newSlot.getTopContainerLength()<2 && secondSlot.getTopContainerLength()<2){
                                containerSlots.clear();
                                containerSlots.add(newSlot);
                                containerSlots.add(secondSlot);
                                newSlot.addContainer(container);
                                secondSlot.addContainer(container);
                            }
                            else{
                                System.out.println("container doesn't fit");
                            }
                        }else {
                            if (containerSlots.size() == 1 && newSlot.getTopContainerLength() == 2) {
                                System.out.println("Container doesn't fit");
                            } else {
                                containerSlots.remove(0);
                                containerSlots.add(newSlot);
                                newSlot.addContainer(container);
                            }
                        }
                        container.setSlots(containerSlots);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        count1++;
                        if(count1<trajectories1.size()) {
                            setContainer(null);
                            crane.setContainer(false,null);
                            trajectory1 = trajectories1.get(count1);
                            loadTrajectory1();
                        }
                    } else {
                        c1 = true;
                        c2=true;
                        setReady();
                    }
                }
                if(crane2.moveCrane(xEnd2, yEnd2, v2, 1)&&!crane2.overlapCraneArea(crane)) {
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
    public Slot getSlot(int x , int y){
        for(Slot slot : slots){
            if(slot.getxCoordinate()==x && slot.getyCoordinate()==y){
                return slot;
            }
        }
        return null;
    }
    public void setReady(){
        if(c1 && c2){
            timer.stop();
        }
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public void loadTrajectory1(){
        xEnd1 = trajectory1.getX();
        yEnd1 = trajectory1.getY();
        v1 = trajectory1.getV();
    }
    public void loadTrajectory2(){
        xEnd2 = trajectory2.getX();
        yEnd2 = trajectory2.getY();
        v2 = trajectory2.getV();
    }

    public void setContainerX(int containerX) {
        this.containerX = containerX;
    }

    public void setContainerY(int containerY) {
        this.containerY = containerY;
    }
    @Override
    protected void paintComponent(Graphics g) {
        g2d = (Graphics2D) g;
        super.paintComponent(g);
        // g2d.setColor(Color.RED);
        //Rectangle2D rect = new Rectangle2D.Double(500,50,200,200);
        //g2d.fill(rect);
        g.setColor(Color.black);
        g2d.drawString("Time: "+time,550,265);
        g2d.drawString("Current task 1: C:"+Integer.toString((int) xEnd1)+"   R: "+Integer.toString((int) yEnd1)+" " +
                "     Speed X:"+Integer.toString((int) v1)+"  Y: "+Integer.toString((int) yDelta1),100,10);
        g2d.drawString("Current task 2: C:"+Integer.toString((int) xEnd2)+"   R: "+Integer.toString((int) yEnd2)+" " +
                "     Speed X:"+Integer.toString((int) v2)+"  Y: "+Integer.toString((int) yDelta2),600,10);
        setContainerY(780/(width+1));
        if(containerY>100){
            setContainerY(50);
        }
        int y = 50;
        int x = 50;
        setContainerX(1450/(length+1));
        if(containerX>200){
            setContainerX(100);
        }
        for(int i=0 ; i<width+1; i++){
            g2d.drawLine(50, y,(containerX*length)+50, y);
            if(i!=width){
                g2d.drawString(Integer.toString(i),40,y+20);
            }
            y += containerY;
        }
        for(int i=0; i<length+1; i++){
            g2d.drawLine( x,50, x,(containerY*width)+50);
            if(i!=length){
                g2d.drawString(Integer.toString(i),x+20,40);
            }
            x += containerX;
        }
        System.out.println("containers:"+containers.size());
        for(Container c : containers){
            c.drawContainer(g2d,containerX,containerY);
        }
        for(Crane c : cranes){
            c.drawCrane(g2d,containerX,containerY,width);
        }
        for(Slot s : slots){
            s.drawSlot(g2d,containerX,containerY);
        }
        /*RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2d.setRenderingHints(rh);

        //crane2.drawCrane(g2d);
    */
    }
}