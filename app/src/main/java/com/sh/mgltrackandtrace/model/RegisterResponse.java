
package com.sh.mgltrackandtrace.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterResponse {

    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("message")
    @Expose
    private MessageList message;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public MessageList getMessage() {
        return message;
    }

    public void setMessage(MessageList message) {
        this.message = message;
    }

}
