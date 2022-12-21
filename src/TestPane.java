import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

import static java.lang.Thread.sleep;

class TestPane extends JPanel {
    public Graphics2D g2d;
    private Color normal, correct;
    private Crane crane2;
    HashMap<Integer,Container> containers;
    List<Container>printContainers;
    List<Assignment>assignments, realAssignments;
    List<Crane>cranes;
    int length,width,targetHeight,time=0;
    int containerX,containerY;
    private List<Slot>slots, notVisitedSlots;
    private Timer timer;
    private boolean c1 = false, c2 = false,containerAttached, heightMode, firstTime = true;
    private double[] restricted = {0,0};
    ActionListener al;
    public TestPane(List<Assignment>assignments,List<Crane>cranes, HashMap<Integer,Container> containers,List<Slot>slots, int length, int width,int containerX,int containerY) {
        this.assignments = assignments;
        this.realAssignments = new ArrayList<>();
        this.slots = slots;
        if(assignments.isEmpty()){
            this.heightMode = true;
            this.targetHeight = 0;
            this.notVisitedSlots = new LinkedList<>();
        }else {
            this.heightMode = false;
            for (Assignment as : this.assignments) {
                Container c = containers.get(as.getContainerId());
                if (c.getSlot().getId() != as.getSlotId()) {
                    realAssignments.add(as);
                }
            }
        }
        this.containers = containers;
        this.printContainers = new ArrayList<>();
        printContainers.addAll(containers.values());
        this.length = length;
        restricted[1] = length;
        this.width=width;
        this.cranes = cranes;
        for(Crane c : cranes){
            if(c.getXMin() > restricted[0]){
                restricted[0] = c.getXMin();
            }
            if(c.getXMax() < restricted[1]){
                restricted[1] = c.getXMax();
            }
        }
        this.containerX = containerX;
        this.containerY = containerY;
        al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (firstTime) {
                        sleep(5000);
                        firstTime = false;
                    } else {
                        sleep(500);
                    }
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                for (Crane crane : cranes) {
                    Container container = null;
                    Assignment assignment = null;
                    if (crane.getCurrentAssignment() == null) {
                        if (!heightMode) {
                            if (realAssignments.isEmpty()) {
                                crane.setCompleted(true);
                                checkReady();
                            }
                            for (Assignment as : realAssignments) {
                                Container c = containers.get(as.getContainerId());
                                Slot sBegin = slots.get(c.getSlot().getId());
                                Slot sEnd = slots.get(as.getSlotId());
                                if (sBegin != null) {
                                    if (sEnd.getXCoordinate() <= crane.getXMax() && sEnd.getXCoordinate() >= crane.getXMin() && sBegin.getXCoordinate() <= crane.getXMax() && sBegin.getXCoordinate() >= crane.getXMin()) {
                                        crane.setCurrentAssignment(as);
                                        assignment = as;
                                        realAssignments.remove(as);
                                        break;
                                    } else if (sBegin.getXCoordinate() < crane.getXMax() && sBegin.getXCoordinate() > crane.getXMin()) {
                                        if (sBegin.getXCoordinate() < restricted[0] || sBegin.getXCoordinate() > restricted[1]) {
                                            Assignment newAssignment = findPlaceInArea(c, crane, restricted);
                                            crane.setCurrentAssignment(newAssignment);
                                            assignment = newAssignment;
                                            break;
                                        }
                                    }
                                }
                            }
                        } else {
                            while (!notVisitedSlots.isEmpty()) {
                                Random r = new Random();
                                Slot slot = notVisitedSlots.get(r.nextInt(notVisitedSlots.size()));
                                Container c = slot.getTopContainer();
                                //todo : uit while loop geraken als de slot niet past / geen slots meer zijn , kan via copy list en dan deleten fsoww
                                double[] area = new double[2];
                                area[0] = crane.getXMin();
                                area[1] = crane.getXMax();
                                Assignment newAssignment = findPlaceInArea(c, crane, area);
                                Slot sBegin = slots.get(c.getSlot().getId());
                                Slot sEnd = slots.get(newAssignment.getSlotId());
                                if (sBegin.getXCoordinate() > area[0] && sBegin.getXCoordinate() < area[1]) {
                                    crane.setCurrentAssignment(newAssignment);
                                    assignment = newAssignment;
                                    notVisitedSlots.remove(slot);
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
                            Container c = containers.get(assignment.getContainerId());
                            Slot containerSlot = c.getSlot();
                            Slot endSlot = slots.get(assignment.getSlotId());
                            container = c;
                            crane.setContainer(false, container);
                            if (c == null) {
                                //todo: mogelijks dit fixen voor stopcondities.
                                crane.setCompleted(true);
                                checkReady();
                            }
                        }
                        if (canTakeContainer(container) && canPlaceContainer(container, assignment.getSlotId())) {
                            if (crane.moveCrane()) {
                                Slot slot = container.getSlot();
                                for (int i = slot.getId(); i < slot.getId() + container.getSize(); i++) {
                                    slots.get(i).removeContainer(container);
                                }
                                //todo: hier nog iets fixen want containers gaan niet juist in volgorde.
                                printContainers.remove(container);
                                printContainers.add(container);
                                crane.setHasContainer(true);
                            }
                        } else {
                            if (container == null) {
                                System.out.println("Error container null");
                            }
                        }
                    } else if (crane.moveCrane() && assignment != null) {
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
                time++;
                repaint();
            }
        };
        timer = new Timer(10,al);
    }

    public Assignment findPlaceInArea(Container container, Crane crane, double[] area){
        int x=container.getSlot().getXCoordinate();
        int y=container.getSlot().getYCoordinate();
        while(true) {
            Slot slot = getSlotWithCoords(x, y);
            if (slot != null) {
                if (x > area[0] && x < area[1] && canPlaceContainer(container, slot.getId())) {
                    return new Assignment(slot.getId(), container.getId(), false);
                } else {
                    if (crane.getXMax() < area[1]) {
                        x++;
                        if (x > area[1]) {
                            x = (int) area[0];
                            y++;
                            if (y > width) {
                                y = 0;
                            }
                        }
                    } else {
                        x--;
                        if (x < area[0]) {
                            x = (int) area[1];
                            y++;
                            if (y > width) {
                                y = 0;
                            }
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
    public void makeTargetHeight(int targetHeight){
        this.targetHeight = targetHeight;
        for(Slot slot : slots){
            if(slot.getStackSize() > targetHeight){
                notVisitedSlots.add(slot);
            }
        }
    }
    public boolean canPlaceContainer(Container container, int endSlotId){
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
            if(heightMode){
                if(sl.getStackSize()+1 > targetHeight){
                    return false;
                }
            }
        }
        return true;
    }
    public  boolean canTakeContainer(Container container){
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
        boolean ready = true;
        for(Crane c : cranes){
            if(!c.isCompleted()){
                ready = false;
            }
        }
        if(ready) {
            timer.stop();
            List<Assignment> copyAssign = new ArrayList<>(List.copyOf(assignments));
            for (Assignment as : assignments) {
                Container c = containers.get(as.getContainerId());
                Slot cSlot = c.getSlot();
                if (as.getSlotId() == cSlot.getId()) {
                    copyAssign.remove(as);
                    break;
                }
            }
            if (copyAssign.isEmpty()) {
                System.out.println("deze ordening klopt!");
            }
        }
    }
    public void setContainerX(int containerX) {
        this.containerX = containerX;
    }

    public void setContainerY(int containerY) {
        this.containerY = containerY;
    }
    public void startTimer(){
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g2d = (Graphics2D) g;
        super.paintComponent(g);
        g.setColor(Color.black);
        g2d.drawString("Time: "+time,500,20);
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
        for(Container c : printContainers){
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