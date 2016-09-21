package proj.me.discovery.services;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 20/12/15.
 */
public class RegisterResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The message
     */
    public String getMessage() {
        return message;
    }
    /**
     *
     * @param message
     * The message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
