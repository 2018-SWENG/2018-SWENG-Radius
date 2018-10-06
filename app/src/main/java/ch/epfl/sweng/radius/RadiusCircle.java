package ch.epfl.sweng.radius;

import android.location.Location;

import com.google.android.gms.maps.model.Circle;

public interface RadiusCircle {

    //methods
    public double getRadius();

    public double getLatitude();

    public double getLongtitude();

    public void setRadius(double radius);

    public void setLatitude(double latitude);

    public void setLongtitude(double longtitude);

    public boolean contains(double p2latitude, double p2longtitude);

    public double findDistance(double p2latitude, double p2longtitude);
}
