
package de.cirrus.jmaps.gsonclasses;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainRouteClass {

    @SerializedName("geocoded_waypoints")
    @Expose
    private List<GeocodedWaypoint> geocodedWaypoints = null;
    @SerializedName("routes")
    @Expose
    private List<SubRoute> routes = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<GeocodedWaypoint> getGeocodedWaypoints() {
        return geocodedWaypoints;
    }

    public void setGeocodedWaypoints(List<GeocodedWaypoint> geocodedWaypoints) {
        this.geocodedWaypoints = geocodedWaypoints;
    }

    public MainRouteClass withGeocodedWaypoints(List<GeocodedWaypoint> geocodedWaypoints) {
        this.geocodedWaypoints = geocodedWaypoints;
        return this;
    }

    public List<SubRoute> getRoutes() {
        return routes;
    }

    public void setRoutes(List<SubRoute> routes) {
        this.routes = routes;
    }

    public MainRouteClass withRoutes(List<SubRoute> routes) {
        this.routes = routes;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MainRouteClass withStatus(String status) {
        this.status = status;
        return this;
    }

}
