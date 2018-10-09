package ch.epfl.sweng.radius;

public interface RadiusCircle {

    //methods
    double getRadius();

    double getLatitude();

    double getLongtitude();

    void setRadius(double radius);

    void setLatitude(double latitude);

    void setLongtitude(double longtitude);

    boolean contains(double p2latitude, double p2longtitude);

    double findDistance(double p2latitude, double p2longtitude);
}
