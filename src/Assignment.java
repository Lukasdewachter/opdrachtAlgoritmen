public class Assignment {
    private int slotId;
    private int containerId;

    private Boolean active;
    public Assignment(int slotId, int containerId, Boolean active){
        this.slotId=slotId;
        this.containerId=containerId;
        this.active = active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }

    public int getContainerId() {
        return containerId;
    }

    public int getSlotId() {
        return slotId;
    }
}
