
package de.cirrus.jmaps.gsonclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OverviewPolyline {

    @SerializedName("points")
    @Expose
    private String points;

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public OverviewPolyline withPoints(String points) {
        this.points = points;
        return this;
    }

}
