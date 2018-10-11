package ch.epfl.sweng.radius;

public interface RadiusCircle {

    //methods
    double getRadius();

    double getLatitude();

    double getLongitude();

    void setRadius(double radius);

    void setLatitude(double latitude);

    void setLongitude(double longtitude);

    boolean contains(double p2latitude, double p2longtitude);

    double findDistance(double p2latitude, double p2longtitude);
}
