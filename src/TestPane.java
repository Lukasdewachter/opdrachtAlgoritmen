import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Thread.sleep;

class TestPane extends JPanel {
    public Graphics2D g2d;
    private Color normal, correct;
    private Crane crane2;
    List<Container> containers;
    List<Assignment>assignments, realAssignments;
    List<Crane>cranes;
    int length,width,time=0;
    int containerX,containerY;
    private List<Slot>slots;
    private Timer timer;
    private Container container;
    private Boolean c1 = false, c2 = false,containerAttached;
    private double[] restricted = {0,0};
    ActionListener al;
    public TestPane(List<Assignment>assignments,List<Crane>cranes, List<Container> containers,List<Slot>slots, int length, int width,int containerX,int containerY) {
        this.assignments = assignments;
        this.containers = containers;
        this.realAssignments = new ArrayList<>();
        for(Assignment as : this.assignments){
            for(Container c : this.containers){
                if(c.getId() == as.getContainerId()){
                    if(c.getSlot().getId() != as.getSlotId()){
                        realAssignments.add(as);
                    }
                }
            }
        }
        this.length = length;
        restricted[1] = length;
        this.width=width;
        this.slots = slots;
        this.container=null;
        this.cranes = cranes;
        for(Crane c : cranes){
            if(c.getXMin() > restricted[0]){
                restricted[0] = c.getXMin();
            }
            if(c.getXMax() < restricted[1]){
                restricted[1] = c.getXMax();
            }
        }
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
                for(Crane crane : cranes) {
                    Container container = null;
                    Assignment assignment = null;//todo: container niet meer globaal maken
                    if(crane.getCurrentAssignment()==null){
                        if (realAssignments.isEmpty()) {
                            crane.setCompleted(true);
                            checkReady();
                        }
                        for(Assignment as : realAssignments) {
                            Slot sBegin = null;
                            for (Container c : containers) {
                                if (c.getId() == as.getContainerId()) {
                                    sBegin = slots.get(c.getSlot().getId());
                                    break;
                                }
                            }
                            Slot sEnd = slots.get(as.getSlotId());
                            if(sBegin != null) {
                                if (sEnd.getXCoordinate() < crane.getXMax() && sEnd.getXCoordinate() > crane.getXMin() && sBegin.getXCoordinate() < crane.getXMax() && sBegin.getXCoordinate() > crane.getXMin()) {
                                    crane.setCurrentAssignment(as);
                                    assignment = as;
                                    realAssignments.remove(as);
                                    break;
                                } else if (sBegin.getXCoordinate() < crane.getXMax() && sBegin.getXCoordinate() > crane.getXMin()) {
                                    if(sBegin.getXCoordinate() < restricted[0] || sBegin.getXCoordinate() > restricted[1]) {
                                        for (Container c : containers) {
                                            if (c.getId() == as.getContainerId()) {
                                                Assignment newAssignment = findPlaceInRestricted(c, crane);
                                                crane.setCurrentAssignment(newAssignment);
                                                assignment = newAssignment;
                                                break;
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }else {
                        container = crane.getContainer();
                        assignment = crane.getCurrentAssignment();
                    }
                    if (!crane.getHasContainer() && assignment != null) {
                        if (container == null) {
                            for (Container c : containers) {
                                if (c.getId() == assignment.getContainerId()) {
                                    Slot containerSlot = c.getSlot();
                                    Slot endSlot = slots.get(assignment.getSlotId());
                                    container = c;
                                    crane.setContainer(false,container);
                                    break;
                                }
                            }
                            if (container == null) {
                                crane.setCompleted(true);
                                checkReady();
                            }
                        }
                        if (container != null && canTakeContainer(container) && canPlaceContainer(container, assignment.getSlotId()) && assignment != null) {
                            if (crane.moveCrane()) {
                                try {
                                    sleep(1000);
                                } catch (InterruptedException ex) {
                                    throw new RuntimeException(ex);
                                }
                                Slot slot = container.getSlot();
                                for (int i = slot.getId(); i < slot.getId() + container.getSize(); i++) {
                                    slots.get(i).removeContainer(container);
                                }

                                containers.remove(containers.indexOf(container));
                                containers.add(container);
                                crane.setHasContainer(true);
                            }
                        } else {
                            if (container == null) {
                                System.out.println("Error container null");
                            }

                        }
                    } else if (crane.moveCrane() && assignment!=null) {
                        Slot newSlot = slots.get(crane.getCurrentAssignment().getSlotId());
                        container.setSlot(newSlot);
                        for (int i = newSlot.getId(); i < newSlot.getId() + container.getSize(); i++) {
                            slots.get(i).addContainer(container);
                        }
                        crane.getCurrentAssignment().setActive(false);
                        crane.setContainer(false, null);
                        crane.setCurrentAssignment(null);
                    }
                }
                time ++;
                repaint();
            }
        };
        timer = new Timer(10,al);
        timer.start();
    }

    public Assignment findPlaceInRestricted(Container container, Crane crane){
        int x=container.getSlot().getXCoordinate();
        int y=container.getSlot().getYCoordinate();
        while(true){
            Slot slot = getSlotWithCoords(x,y);
            if(x>restricted[0] && x<restricted[1] && canPlaceContainer(container,slot.getId())){
                return new Assignment(slot.getId(),container.getId(),false);
            }else {
                if(crane.getXMax() < restricted[1]){
                    x++;
                    if(x>restricted[1]){
                        x = (int)restricted[0];
                        y++;
                        if(y > width){
                            y=0;
                        }
                    }
                }else{
                    x--;
                    if(x<restricted[0]){
                        x = (int)restricted[1];
                        y++;
                        if(y > width){
                            y=0;
                        }
                    }
                }
            }
        }
    }
    public Slot getSlotWithCoords(int x, int y){
        for (Slot s : slots){
            if(s.getXCoordinate() == x && s.getYCoordinate()==y){
                return s;
            }
        }
        return null;
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
            if(sl.hasContainers()) {
                if (sl.getTopContainer().getSize() > container.getSize()) {
                    System.out.println("Bovenste Container is te groot");
                    return false;
                }
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
            if(s.getTopContainer() !=null) {
                if (s.getTopContainer().getId() != container.getId()) {
                    System.out.println("Slot " + sl.getId() + " Heeft een andere container bovenaan");
                    //TODO: fixen dat bovenste container verplaatst word later.
                    return false;
                }
            }
            if(sl.getYCoordinate() != yCoordinate){
                System.out.println("Slot "+sl.getId()+" Heeft niet juiste y coordinaat");
            }
        }
        return true;
    }
    public void checkReady(){
        Boolean ready = true;
        for(Crane c : cranes){
            if(!c.isCompleted()){
                ready = false;
            }
        }
        if(ready){
            timer.stop();
        }
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
    }
}