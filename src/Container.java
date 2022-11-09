import java.util.List;
public class Container {
    private int id;
    private int length;
    private List<Slot> slots;
    public Container(int id, int length, List<Slot> slots){
        this.id = id;
        this.length = length;
        this.slots=slots;
    }
}