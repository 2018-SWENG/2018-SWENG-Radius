package ch.epfl.sweng.radius.database;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class DBObservable{
    private final Queue<DBUserObserver> userObservers = new ConcurrentLinkedQueue<>();
    private final Queue<DBLocationObserver> locationObservers = new ConcurrentLinkedQueue<>();

    public void addUserObserver(DBUserObserver observer){
        this.userObservers.add(observer);
    }


    public void notifyUserObservers(String id){
        for (DBUserObserver observer: this.userObservers) {
            observer.onUserChange(id);
        }
    }

    public void addLocationObserver(DBLocationObserver observer){
        this.locationObservers.add(observer);
    }

    public void removeLocationObserver(DBLocationObserver observer){
        this.locationObservers.remove(observer);
    }
    public void notifyLocationObservers(String id){
        for (DBLocationObserver observer: this.locationObservers) {
            observer.onLocationChange(id);
        }
    }
}
