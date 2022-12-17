import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static java.lang.Thread.sleep;

class TestPane extends JPanel {
    public Graphics2D g2d;
    private Color normal, correct;
    private Crane crane;
    private Crane crane2;
    List<Container> containers;
    List<Assignment>assignments;
    List<Crane>cranes;
    Assignment assignment;
    double xEnd1 = 0, yEnd1 =0;
    double xEnd2 = 0, yEnd2 =0;
    double xMax=0, xMax2=5;
    double xMin=4, xMin2=8;
    int count1 = 0,count2=0;
    int length,width,time=0;
    int containerX,containerY;
    private List<Slot>slots;
    private Timer timer;
    private Container container;
    private Boolean c1 = false, c2 = false,containerAttached;
    ActionListener al;
    public TestPane(List<Assignment>assignments,List<Crane>cranes, List<Container> containers,List<Slot>slots, int length, int width,int containerX,int containerY) {
        this.assignments = assignments;
        this.length = length;
        this.width=width;
        correct = new Color(46, 255, 0);
        normal = new Color(100,149,237);
        this.assignment = assignments.get(0);
        this.slots = slots;
        this.containers = containers;
        this.container=null;
        this.cranes = cranes;
        this.crane = cranes.get(0);
        //if(cranes.size()>1)this.crane2=cranes.get(1);
        this.containerX = containerX;
        this.containerY = containerY;
        al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sleep(1000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                time ++;
                if(!crane.getHasContainer()){
                    while(true){
                        Container c  = containers.get(assignment.getContainerId());
                        Slot containerSlot = c.getSlot();
                        Slot endSlot = slots.get(assignment.getSlotId());
                        if(containerSlot.getId() == endSlot.getId()){
                            assignments.remove(0);
                            if(assignments.isEmpty())break;
                            setAssignment(assignments.get(0));
                        }else{
                            setContainer(c);
                            setEndCoordinates(assignment);
                            break;
                        }
                    }
                    if(container!=null && canTakeContainer(container) && canPlaceContainer(container,assignment.getSlotId())){
                        double xContainer = container.getX();
                        double yContainer = container.getY();
                        if(crane.moveCrane(xContainer,yContainer)) {
                            try {
                                sleep(1000);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                            container.getSlot().removeContainer(container);
                            containers.remove(containers.indexOf(container));
                            containers.add(container);
                            crane.setContainer(true, container);
                        }
                    }
                    else{
                        System.out.println("Error container null");
                    }
                }
                else if(crane.moveCrane(xEnd1, yEnd1)) {
                        Slot newSlot = slots.get(assignment.getSlotId());
                        container.setSlot(newSlot);
                        newSlot.addContainer(container);
                        assignments.remove(0);
                        if (!assignments.isEmpty()) {
                            setAssignment(assignments.get(0));
                        }else{
                            setContainer(null);
                            crane.setContainer(false, null);
                            c1 = true;
                            c2=true;
                            setReady();
                        }
                        try {
                            sleep(1000);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                }
                /*if(crane2.moveCrane(xEnd2, yEnd2, v2, 1)) {
                    if (count2 < 4) {
                        try {
                            sleep(1200);
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
                }*/
                repaint();
            }
        };
        timer = new Timer(10,al);
        timer.start();
    }
    public Boolean canPlaceContainer(Container container, int endSlotId){
        Slot s =  slots.get(endSlotId);
        int idSlot = s.getId();
        int yCoordinate = s.getYCoordinate();
        int heigth = s.getStackSize();
        for(int i=idSlot; i<idSlot+container.getSize();i++){
            Slot sl = slots.get(i);
            if(sl.getStackSize() != heigth){
                System.out.println("Slot "+sl.getId()+" Heeft niet juiste hoogte");
                return false;
            }
            if(sl.getTopContainer().getSize()>1){
                System.out.println("Bovenste Container is te groot");
                return false;
            }
            if(sl.getYCoordinate() != yCoordinate){
                System.out.println("Slot "+sl.getId()+" Heeft niet juiste y coordinaat");
            }
        }
        return true;
    }
    public  Boolean canTakeContainer(Container container){
        Slot s =  container.getSlot();
        int idSlot = s.getId();
        int yCoordinate = s.getYCoordinate();
        int heigth = s.getStackSize();
        for(int i=idSlot; i<idSlot+container.getSize();i++){
            Slot sl = slots.get(i);
            if(s.getTopContainer().getId() != container.getId()){
                System.out.println("Slot "+sl.getId()+" Heeft een andere container bovenaan");
                return false;
            }
            if(sl.getYCoordinate() != yCoordinate){
                System.out.println("Slot "+sl.getId()+" Heeft niet juiste y coordinaat");
            }
        }
        return true;
    }
    public void setEndCoordinates(Assignment as){
        double xEnd = slots.get(as.getSlotId()).getXCoordinate();
        double yEnd = slots.get(as.getSlotId()).getYCoordinate();
        if(container.getSize()==2){
            xEnd+=0.5;
        }
        if(container.getSize()==3){
            xEnd+=1;
        }
        this.xEnd1 = xEnd;
        this.yEnd1 = yEnd;
    }
    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public void setReady(){
        if(c1 && c2){
            timer.stop();
        }
    }

    public void setContainer(Container container) {
        this.container = container;
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
        g2d.drawString("Time: "+time,500,20);
        //g2d.drawString("Current task 1: C:"+Integer.toString((int) xEnd1)+"   R: "+Integer.toString((int) yEnd1)+" " +
        //        "     Speed X:"+Integer.toString((int) v1)+"  Y: "+Integer.toString((int) yDelta1),100,10);
        //g2d.drawString("Current task 2: C:"+Integer.toString((int) xEnd2)+"   R: "+Integer.toString((int) yEnd2)+" " +
        //       "     Speed X:"+Integer.toString((int) v2)+"  Y: "+Integer.toString((int) yDelta2),600,10);
        if(containerY>100){
            setContainerY(50);
        }
        int lineY = 50;
        int lineX = 50;

        if(containerX>200){
            setContainerX(100);
        }
        for(int i=0 ; i<width+1; i++){
            g2d.drawLine(50, lineY,((containerX)*length)+50, lineY);
            if(i!=width){
                g2d.drawString(Integer.toString(i),40,lineY+10);
            }
            lineY += containerY;
        }
        for(int i=0; i<length+1; i++){
            g2d.drawLine( lineX,50, lineX,((containerY)*width)+50);
            if(i!=length){
                g2d.drawString(Integer.toString(i),lineX+20,40);
            }
            lineX += containerX;
        }
        for(Container c : containers){
            c.drawContainer(g2d);
        }
        for(Crane c : cranes){
            c.drawCrane(g2d);
        }
        for(Slot s : slots){
            s.drawSlot(g2d);
        }
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2d.setRenderingHints(rh);
/*
        //crane2.drawCrane(g2d);
    */
    }
}