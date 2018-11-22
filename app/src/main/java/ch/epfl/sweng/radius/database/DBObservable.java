package ch.epfl.sweng.radius.database;

import java.util.HashSet;
import java.util.Set;

public abstract class DBObservable {
    private final Set<DBObserver> observers = new HashSet<>();

    public void addObserver(DBObserver observer){
        this.observers.add(observer);
    }

    public void removeObserver(DBObserver observer){
        this.observers.remove(observer);
    }
    public void notifyObservers(String id){
        for (DBObserver observer: this.observers) {
            observer.onDataChange(id);
        }
    }
}
