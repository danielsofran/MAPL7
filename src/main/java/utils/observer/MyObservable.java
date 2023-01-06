package utils.observer;

import utils.events.ChangeEventType;
import utils.events.Event;

public interface MyObservable <E extends Event> {
    void addObserver(MyObserver<E> e);
    void removeObserver(MyObserver<E> e);
    void notifyObservers(E t);
}