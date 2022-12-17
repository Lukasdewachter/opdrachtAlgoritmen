public class Assignment {
    private int slotId;
    private int containerId;

    private Boolean completed;
    public Assignment(int slotId, int containerId, Boolean completed){
        this.slotId=slotId;
        this.containerId=containerId;
        this.completed = completed;
    }

    public int getContainerId() {
        return containerId;
    }

    public int getSlotId() {
        return slotId;
    }
}
