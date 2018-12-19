/**
 * This file contains an interface with partial implementation, an
 * abstract class, that will be extended by the observable database
 * objects.
 * @author RADIUS
 * @version 1.0
 */
package ch.epfl.sweng.radius.database;

import java.util.Queue;
import java.util.Timer;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * An interface with partial implementation, an abstract class,
 * that will be extended by the observable database objects.
 * In other words, Observable side in a simple Observer pattern implementation.
 */
public abstract class DBObservable{

    //Properties
    private final Queue<DBUserObserver> userObservers = new ConcurrentLinkedQueue<>();
    private final Queue<DBLocationObserver> locationObservers = new ConcurrentLinkedQueue<>();

    /**
     * Adds an observer that is only interested in user related changes to the list
     * of user observers.
     * @param observer The user observer to be added.
     */
    public void addUserObserver(DBUserObserver observer) {
        this.userObservers.add(observer);
    }

    /**
     * Notifies all observers in the list of user observers by calling their particular
     * onUserChange methods.
     * @param id The id of the database object
     */
    public void notifyUserObservers(String id) {
        for (DBUserObserver observer: this.userObservers) {
            observer.onUserChange(id);
        }
    }

    /**
     * Removes an observer from the list of user observers.
     * @param observer The user observer to be removed
     */
    public void removeUserObserver(DBUserObserver observer){
        this.userObservers.remove(observer);
    }

    /**
     * Adds an observer that is only interested in location related changes to the list
     * of location observers.
     * @param observer The location observer to be added.
     */
    public void addLocationObserver(DBLocationObserver observer) {
        this.locationObservers.add(observer);
    }

    /**
     * Notifies all observers in the list of location observers by calling their particular
     * onLocationChange methods.
     * @param id The id of the database object
     */
    public void notifyLocationObservers(String id) {
        for (DBLocationObserver observer: this.locationObservers) {
            observer.onLocationChange(id);
        }
    }

    /**
     * Removes an observer from the list of location observers.
     * @param observer The location observer to be removed
     */
    public void removeLocationObserver(DBLocationObserver observer){
        this.locationObservers.remove(observer);
    }

    /**
     * Removes all observers of the observable database object by
     * clearing, emptying the lists of user observers and location observers.
     */
    public void removeAllObservers(){
        this.userObservers.clear();
        this.locationObservers.clear();
        OthersInfo.getInstance().timer.cancel();
        OthersInfo.getInstance().timer =  new Timer(true);
        OthersInfo.getInstance().clearInstance();
    }

}
