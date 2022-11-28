public class Assignment {
    private int[] slotId;
    private int containerId;
    public Assignment(int[]slotId, int containerId){
        this.slotId=slotId;
        this.containerId=containerId;
    }

    public int getContainerId() {
        return containerId;
    }

    public int[] getSlotId() {
        return slotId;
    }
}
