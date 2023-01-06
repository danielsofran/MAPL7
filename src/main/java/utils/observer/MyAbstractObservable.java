package utils.observer;

import utils.events.Event;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class MyAbstractObservable<E extends Event> {
    private final List<MyObserver<E>> observers = new LinkedList<>();
    public void addObserver(MyObserver<E> e){
        observers.add(e);
    }
    
    @SafeVarargs
    public final void addAll(MyObserver<E>... observers){
        this.observers.addAll(Arrays.asList(observers));
    }
    public void removeObserver(MyObserver<E> e){
        observers.remove(e);
    }
    public void removeAll(){
        observers.clear();
    }
    public void notifyObservers(E t){
        observers.forEach(x->x.update(t));
    }
}
