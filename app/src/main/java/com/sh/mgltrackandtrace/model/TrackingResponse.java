
package com.sh.mgltrackandtrace.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrackingResponse {

    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("waybill_data")
    @Expose
    private List<WaybillDatum> waybillData = null;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<WaybillDatum> getWaybillData() {
        return waybillData;
    }

    public void setWaybillData(List<WaybillDatum> waybillData) {
        this.waybillData = waybillData;
    }

}
