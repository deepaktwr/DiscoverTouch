package proj.me.discovery.services;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 23/12/15.
 */
public class RegisterRequest {
    @SerializedName("username")
    private String userName;
    @SerializedName("firstname")
    private String userFirstName;
    @SerializedName("lastname")
    private String userLastName;

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
    /**
     *
     * @return
     * The userFirstName
     */
    public String getUserFirstName() {
        return userFirstName;
    }
    /**
     *
     * @param userFirstName
     * The firstname
     */
    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }
    /**
     *
     * @return
     * The userLastName
     */
    public String getUserLastName() {
        return userLastName;
    }
    /**
     *
     * @param userLastName
     * The lastname
     */
    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }
}
