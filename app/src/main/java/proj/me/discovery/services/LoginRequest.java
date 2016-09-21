package proj.me.discovery.services;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 20/12/15.
 */
public class LoginRequest {
    @SerializedName("username")
    private String userName;

    /**
     *
     * @return
     * The userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     *
     * @param userName
     * The username
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
