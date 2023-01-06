package utils.events;

public class ChangedEvent<E> implements Event {
    private E data, oldData;
    private ChangeEventType type;

    public ChangedEvent(ChangeEventType type, E data){
        this.type = type;
        this.data = data;
    }

    public ChangedEvent(ChangeEventType type, E data, E oldData){
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public E getData() {
        return data;
    }

    public E getOldData() {
        return oldData;
    }

    public ChangeEventType getType() {
        return type;
    }
}