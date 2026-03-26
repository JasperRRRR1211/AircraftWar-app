package edu.hitsz.observer;

import java.util.ArrayList;
import java.util.List;

public class BombPublisher {
    private final List<BombObserver> observers = new ArrayList<>();

    public void addObserver(BombObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(BombObserver observer) {
        observers.remove(observer);
    }

    public void notifyAllObservers() {
        for (BombObserver observer : observers) {
            observer.update();
        }
    }

    public List<BombObserver> notifyAndCollectDestroyed() {
        List<BombObserver> destroyed = new ArrayList<>();
        for (BombObserver observer : observers) {
            observer.update();
            destroyed.add(observer);
        }
        return destroyed;
    }
}
