package utils.observer;

import utils.events.Event;

public interface MyObserver<E extends Event> {
    void update(E e);
}