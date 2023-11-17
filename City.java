import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.acos;

public class City {

    private final String name;
    private final State state;
    private final double latitude;
    private final double longitude;

    private static final double RADIUS_OF_EARTH = 6335.439; // kilometers

    public City(String name, State state, double latitude, double longitude) {
        this.name = name;
        this.state = state;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String name() { return this.name; }
    public State state() { return this.state; }
    public double latitude() { return this.latitude; }
    public double longitude() { return this.longitude; }


    public double distance(City to) {
        double phi1 = Math.toRadians(this.latitude);
        double phi2 = Math.toRadians(to.latitude);
        double lambda1 = Math.toRadians(this.longitude);
        double lambda2 = Math.toRadians(to.longitude);
        double deltaLambda = lambda2 - lambda1;

        double centralAngle = acos(sin(phi1) * sin(phi2) +
                cos(phi1) * cos(phi2) * cos(deltaLambda));

        return RADIUS_OF_EARTH * centralAngle;
    }

    @Override
    public String toString() {
        return this.name + ", " + this.state.code();
    }

    public boolean equals(City other) {
        return this.name.equals(other.name) && this.state.equals(other.state);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof City && this.equals((City) other);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode() * this.state.hashCode();
    }
}
