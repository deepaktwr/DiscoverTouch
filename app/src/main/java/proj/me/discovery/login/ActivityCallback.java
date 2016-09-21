package proj.me.discovery.login;

import proj.me.discovery.services.LoginRequest;
import proj.me.discovery.services.RegisterRequest;

/**
 * Created by root on 23/12/15.
 */
public interface ActivityCallback {
    void startedTyping();
    void performLogin(LoginRequest loginRequest);
    void performRegister(RegisterRequest registerRequest);

    void callFragment(int id);

    void animComplete(String userText, int visibility);
}
