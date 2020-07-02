
package com.sh.mgltrackandtrace.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WaybillDatum {

    @SerializedName("tracking_point")
    @Expose
    private String trackingPoint;
    @SerializedName("tracking_point_mm")
    @Expose
    private String tracking_point_mm;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("created_at")
    private String created_at;

    public String getTrackingPoint() {
        return trackingPoint;
    }

    public void setTrackingPoint(String trackingPoint) {
        this.trackingPoint = trackingPoint;
    }

    public String getTrackingPointMM() {
        return tracking_point_mm;
    }

    public void setTrackingPointMM(String tracking_point_mm) {
        this.tracking_point_mm = tracking_point_mm;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTracking_point_mm() {
        return tracking_point_mm;
    }

    public String getCreated_at() {
        return created_at;
    }
}
