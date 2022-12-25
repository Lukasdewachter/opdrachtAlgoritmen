import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static java.lang.Thread.sleep;

class TestPane extends JPanel {
    public Graphics2D g2d;
    HashMap<Integer,Container> containers;
    HashMap<Integer,List<Slot>>heightMap;
    List<Container>printContainers;
    List<Assignment>assignments, realAssignments;
    List<Crane>cranes;
    int length,width,maxHeight,targetHeight,time=0,timeDelay;
    int containerX,containerY;
    private List<Slot>slots, notVisitedSlots;
    private Timer timer;
    private boolean c1 = false, c2 = false,containerAttached, heightMode, firstTime = true;
    private double[] restricted = {0,0};
    File outputFile;
    ActionListener al;
    public TestPane(List<Assignment>assignments,List<Crane>cranes, HashMap<Integer,Container> containers,List<Slot>slots, int length, int width,int containerX,int containerY,int maxHeight,int timeDelay) {
        this.assignments = assignments;
        this.outputFile =  new File("./output/output.txt");
        this.maxHeight = maxHeight;
        this.realAssignments = new ArrayList<>();
        this.slots = slots;
        this.timeDelay = timeDelay;
        this.heightMap = new HashMap<>();
        for(int i=0; i<maxHeight;i++){
            heightMap.put(i,new ArrayList<>());
        }
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
        for(Crane c : cranes){
            c.setRestricted(restricted);
        }
        this.containerX = containerX;
        this.containerY = containerY;
        al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (firstTime) {
                        sleep(2000);
                        firstTime = false;
                    } else {
                        sleep(timeDelay);
                    }
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                for (Crane crane : cranes) {
                    Container container = null;
                    Assignment assignment = null;
                    if(crane.isBlocked()){
                        Assignment otherAssignment = null;
                        for(Crane cr : cranes){
                            if(cr.getId() != crane.getId()){
                                otherAssignment = cr.getCurrentAssignment();
                                break;
                            }
                        }
                        if(otherAssignment != null){
                            crane.moveCraneOneStep(otherAssignment);
                            continue;
                        }
                        crane.moveCraneOneStep(null);
                        continue;
                    }
                    if (crane.getCurrentAssignment() == null || crane.getCurrentAssignment().getContainerId()==-1) {
                        if (!heightMode) {
                            if (realAssignments.isEmpty()) {
                                crane.setCompleted(true);
                                try {
                                    checkReady();
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                            for (Assignment as : realAssignments) {
                                if(as.getActive()){
                                    continue;
                                }
                                Container c = containers.get(as.getContainerId());
                                Slot sBegin = slots.get(c.getSlot().getId());
                                double containerXcoord = c.getX();
                                Slot sEnd = slots.get(as.getSlotId());
                                if (sBegin != null) {
                                    if(canTakeContainer(c,crane) && canPlaceContainer(c, sEnd.getId())) {
                                        if (sEnd.getXCoordinate() <= crane.getXMax() && sEnd.getXCoordinate() >= crane.getXMin() && containerXcoord <= crane.getXMax() && containerXcoord >= crane.getXMin()) {
                                            crane.setCurrentAssignment(as,time);
                                            assignment = as;
                                            as.setActive(true);
                                            break;
                                        } else if (containerXcoord <= crane.getXMax() && containerXcoord >= crane.getXMin()) {
                                            if (containerXcoord < restricted[0] || containerXcoord > restricted[1]) {
                                                Assignment newAssignment = findPlaceInArea(c, crane, restricted);
                                                crane.setCurrentAssignment(newAssignment,time);
                                                assignment = newAssignment;
                                                break;
                                            }
                                        }
                                    }else if(!sBegin.getTopContainer().equals(c) && containerXcoord <= crane.getXMax() && containerXcoord >= crane.getXMin()){
                                        Container blockingContainer = sBegin.getTopContainer();
                                        double maxArea = crane.getXMax();
                                        if(maxArea > length-1){
                                            maxArea = length-1;
                                        }
                                        double[] area = {crane.getXMin(),maxArea};
                                        Slot blockingSlot = blockingContainer.getSlot();
                                        Assignment ass = findPlaceInArea(blockingContainer,crane, area);
                                        crane.setCurrentAssignment(ass,time);
                                        assignment = ass;
                                        realAssignments.add(new Assignment(blockingSlot.getId(),blockingContainer.getId(),false));
                                        break;
                                    }
                                }
                            }
                            if(assignment == null){
                                crane.moveFromRestricted(time);
                                assignment = crane.getCurrentAssignment();
                            }
                        } else {
                            if(!notVisitedSlots.isEmpty()) {
                                for (int i = 0; i < notVisitedSlots.size(); i++) {
                                    Slot slot = notVisitedSlots.get(i);
                                    Container c = slot.getTopContainer();
                                    double[] area = new double[2];
                                    area[0] = crane.getXMin();
                                    area[1] = crane.getXMax();
                                    if(area[1] > length-1){
                                        area[1] = length-1;
                                    }
                                    Slot sBegin = slots.get(c.getSlot().getId());
                                    if (sBegin.getXCoordinate() >= area[0] && sBegin.getXCoordinate() <= area[1]) {
                                        Assignment newAssignment = findPlaceInAreaForHeight(c, crane, area);
                                        if(newAssignment == null){
                                            Assignment tempAssignment = null;
                                            Random r = new Random();
                                            while(true){
                                                Container newContainer = containers.get(r.nextInt(containers.size()-1));
                                                if(!newContainer.isTop()){
                                                    continue;
                                                }
                                                double newX = newContainer.getX();
                                                if(newX<= area[1] && newX>=area[0]&& canTakeContainer(newContainer,crane)){
                                                    tempAssignment = findPlaceInAreaForHeight(newContainer,crane,area);
                                                    if(tempAssignment != null){
                                                        assignment = tempAssignment;
                                                        crane.setCurrentAssignment(tempAssignment,time);
                                                        break;
                                                    }
                                                }
                                            }
                                        }else {
                                            crane.setCurrentAssignment(newAssignment,time);
                                            assignment = newAssignment;
                                            notVisitedSlots.remove(slot);
                                        }
                                        for(int j = assignment.getSlotId(); j<assignment.getSlotId() + containers.get(assignment.getContainerId()).getSize();j++){
                                            slots.get(j).setActive(true);
                                        }
                                        break;
                                    }
                                }
                            }else{
                                Slot s = null;
                                if(crane.getId() == 0){
                                    double xCoord = crane.getXMin();
                                    while(xCoord<0){
                                        xCoord +=1;
                                    }
                                    s = getSlotWithCoords((int)xCoord,0);
                                }else{
                                    double xCoord = crane.getXMax();
                                    while(xCoord>length-1){
                                        xCoord -=1;
                                    }
                                    s = getSlotWithCoords((int)xCoord,0);
                                }
                                Assignment ass = new Assignment(s.getId(),-1,false);
                                crane.setCurrentAssignment(ass,time);
                                assignment = ass;
                                crane.setCompleted(true);
                                try {
                                    checkReady();
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        }
                        if(crane.getCurrentAssignment() != null ){
                            crane.setCompleted(false);
                        }
                    }else {
                        container = crane.getContainer();
                        assignment = crane.getCurrentAssignment();
                    }
                    if (!crane.getHasContainer() && assignment != null) {
                        if (container == null && assignment.getContainerId() != -1) {
                            Container c = containers.get(assignment.getContainerId());
                            container = c;
                            crane.setContainer(false, container);
                            if (c == null) {
                                crane.setCompleted(true);
                                try {
                                    checkReady();
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        }
                        if(container != null) {
                            if(canTakeContainer(container,crane)) {
                                if(crane.getCurrentAssignment() != assignment){
                                    assignment = crane.getCurrentAssignment();
                                    container = containers.get(assignment.getContainerId());
                                }
                                if (canPlaceContainer(container, assignment.getSlotId())) {
                                    if (crane.moveCrane()) {
                                        Slot slot = container.getSlot();
                                        for (int i = slot.getId(); i < slot.getId() + container.getSize(); i++) {
                                            slots.get(i).removeContainer(container);
                                        }
                                        printContainers.remove(container);
                                        printContainers.add(container);
                                        crane.setHasContainer(true,time);
                                    }
                                } else {
                                    if (heightMode) {
                                        crane.moveCrane();
                                    }
                                }
                            }
                        }else{
                            if(assignment.getContainerId() == -1){
                                if(crane.moveCrane()){
                                    crane.setCompleted(true);
                                    try {
                                        checkReady();
                                    } catch (IOException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                }
                            }
                        }
                    }else if (assignment != null) {
                        if (crane.moveCrane()) {
                            Slot newSlot = slots.get(crane.getCurrentAssignment().getSlotId());
                            container.setSlot(newSlot);
                            for (int i = newSlot.getId(); i < newSlot.getId() + container.getSize(); i++) {
                                Slot s = slots.get(i);
                                s.addContainer(container);
                                s.setActive(false);

                            }
                            if(container.getId() == assignment.getContainerId() && newSlot.getId() == assignment.getSlotId()){
                                realAssignments.remove(assignment);

                            }else{
                                assignment.setActive(false);
                            }
                            crane.getCurrentAssignment().setActive(false);
                            crane.setContainer(false, null);
                            crane.setCurrentAssignment(null,time);
                            if (heightMode && notVisitedSlots.isEmpty()) {
                                crane.setCompleted(true);
                                try {
                                    checkReady();
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                            else{
                                if(realAssignments.isEmpty()&& !heightMode){
                                    crane.setCompleted(true);
                                    try {
                                        checkReady();
                                    } catch (IOException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                }
                            }
                        }
                    }else{
                        if(crane.getX() <= restricted[1] && crane.getX() >= restricted[0]){
                            crane.moveFromRestricted(time);
                        }
                    }
                }
                time++;
                repaint();
            }
        };
        timer = new Timer(10,al);
    }
    public Assignment findPlaceInAreaForHeight(Container container, Crane crane, double[] area){
        List<Slot>containerSlots = new ArrayList<>();
        for(int i=container.getSlot().getId(); i<container.getSlot().getId()+container.getSize(); i++){
            containerSlots.add(slots.get(i));
        }
        for(int i=0; i<targetHeight+1;i++){
            List<Slot>slotsAtHeight = heightMap.get(i);
            if(slotsAtHeight != null){
                for(Slot slot : slotsAtHeight){
                    if(slot.isActive()){
                        continue;
                    }
                    boolean sameSlots = false;
                    //check if these slots are different than the current occupied slots
                    for(int j=slot.getId(); j<slot.getId()+container.getSize(); j++){
                        if( j>=slots.size()){
                            sameSlots = true;
                        }
                        else if(containerSlots.contains(slots.get(j))){
                            sameSlots = true;
                        }
                    }
                    int xCoordinate = slot.getXCoordinate();
                    int slotId = slot.getId();
                    //is this slot feaible
                    if(xCoordinate >= area[0] && xCoordinate <= area[1] && canPlaceContainer(container,slot.getId()) && !sameSlots){
                        return new Assignment(slotId, container.getId(), false);
                    }
                }
            }
        }
        return null;
    }
    public Assignment findPlaceInArea(Container container, Crane crane, double[] area){
        int x=container.getSlot().getXCoordinate();
        int y=container.getSlot().getYCoordinate();
        List<Slot>containerSlots = new ArrayList<>();
        //check feasible
        for(int i=container.getSlot().getId(); i<container.getSlot().getId()+container.getSize(); i++){
            containerSlots.add(slots.get(i));
        }
        while(true) {
            Slot slot = getSlotWithCoords(x, y);
            if(slot != null) {
                boolean sameSlots = false;
                for(int i=slot.getId(); i<slot.getId()+container.getSize(); i++){
                    if(i >= slots.size()){
                        sameSlots = true;
                    }
                    //check if these slots are different than occupied slots
                    else if(containerSlots.contains(slots.get(i))){
                        sameSlots = true;
                    }
                }
                if (x >= area[0] && x <= area[1] && canPlaceContainer(container, slot.getId()) && !sameSlots) {
                    return new Assignment(slot.getId(), container.getId(), false);
                } else {
                    if (crane != null) {
                        //If the slot isn't good we move one spot in x or y direction to look for local available slots
                        if (crane.getXMax() < restricted[1] + 1) {
                            x++;
                            if (x > area[1]) {
                                x = (int) area[0];
                                y++;
                                if (y > width-1) {
                                    y = 0;
                                }
                            }
                        } else {
                            x--;
                            if (x < area[0]) {
                                x = (int) area[1];
                                y++;
                                if (y > width-1) {
                                    y = 0;
                                }
                            }
                        }
                    } else {
                        if (x < area[1]) {
                            x++;
                            if (x > area[1]) {
                                x = (int) area[0];
                                y++;
                                if (y > width-1) {
                                    y = 0;
                                }
                            }
                        } else {
                            x--;
                            if (x < area[0]) {
                                x = (int) area[1];
                                y++;
                                if (y > width-1) {
                                    y = 0;
                                }
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
        int lastContainerId=9999999;
        for(Slot slot : slots){
            if(slot.getStackSize() > targetHeight && slot.getTopContainer().getId() != lastContainerId){
                notVisitedSlots.add(slot);
                lastContainerId = slot.getTopContainer().getId();
            }else if(slot.getStackSize()<=targetHeight){
                int slotHeight = slot.getStackSize();
                List<Slot>slotList = heightMap.get(slotHeight);
                slotList.add(slot);
                heightMap.replace(slotHeight,slotList);
            }
        }
        Collections.sort(notVisitedSlots,Comparator.comparing(Slot::getTopContainerLength));
    }
    public boolean canPlaceContainer(Container container, int endSlotId){
        Slot s =  slots.get(endSlotId);
        int idSlot = s.getId();
        int yCoordinate = s.getYCoordinate();
        int heigth = s.getStackSize();
        //check all stacking constraints
        for(int i=idSlot; i<idSlot+container.getSize();i++){
            if(i >= slots.size()){
                return false;
            }
            Slot sl = slots.get(i);
            if(heightMode){
                if(heigth +1 > targetHeight){
                    return false;
                }
            }else {
                if (heigth + 1 > maxHeight) {
                    return false;
                }
            }
            if(sl.getStackSize() != heigth){
                return false;
            }
            if(sl.hasContainers()) {
                if (sl.getTopContainer().getSize() > container.getSize()) {
                    return false;
                }
            }
            if(sl.getYCoordinate() != yCoordinate){
                return false;
            }
            //if height needs to be lowered extra check needs to be done
            if(heightMode){
                if(sl.getStackSize()+1 > targetHeight){
                    return false;
                }
            }
        }
        return true;
    }
    public  boolean canTakeContainer(Container container, Crane crane){
        if(!heightMode) {
            Slot s = container.getSlot();
            int idSlot = s.getId();
            int yCoordinate = s.getYCoordinate();
            int heigth = s.getStackSize();
            //fealibility checks
            for (int i = idSlot; i < idSlot + container.getSize(); i++) {
                Slot sl = slots.get(i);
                if (s.getTopContainer() != null) {
                    if (s.getTopContainer().getId() != container.getId()) {
                        return false;
                    }
                }
                if (sl.getYCoordinate() != yCoordinate) {
                    return false;
                }
            }
            return true;
        }else{
            return true;
        }
    }
    public void checkReady() throws IOException {
        boolean ready = true;
        for(Crane c : cranes){
            if(!c.isCompleted()){
                ready = false;
            }
        }
        if(ready) {
            boolean correct = true;
            if(!heightMode) {
                List<Assignment> copyAssign = new ArrayList<>(List.copyOf(assignments));
                for (Assignment as : assignments) {
                    Container c = containers.get(as.getContainerId());
                    Slot cSlot = c.getSlot();
                    if (as.getSlotId() == cSlot.getId()) {
                        copyAssign.remove(as);
                    }else {
                        realAssignments.add(as);
                        correct = false;
                    }
                }
                if (realAssignments.isEmpty() && correct) {
                    System.out.println("Correct order");
                    timer.stop();
                }
            }else{
                for(Slot slot : slots){
                    if(slot.getStackSize()>targetHeight){
                        correct = false;
                    }
                }
                if(correct) {
                    System.out.println("Correct new height");
                    timer.stop();
                }
            }
            if(correct){
                FileWriter writer = new FileWriter("./output/output.txt");
                for(Crane cr : cranes){
                    writer.write("Crane: "+cr.getId()+"\n");
                    List<String>moves = cr.getMoves();
                    for(String s : moves ){
                        writer.write(s+"\n");
                    }
                }
                writer.close();
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.exit(0);
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
        g2d.drawString("Time: "+time+"    assignments left: "+realAssignments.size(),500,20);
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