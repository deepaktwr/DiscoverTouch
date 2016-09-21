package proj.me.discovery.login;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.dd.CircularProgressButton;

import proj.me.discovery.ActDicovery;
import proj.me.discovery.R;
import proj.me.discovery.Utils;
import proj.me.discovery.services.LoginRequest;
import proj.me.discovery.services.LoginResponse;
import proj.me.discovery.services.RegisterRequest;
import proj.me.discovery.services.RegisterResponse;
import proj.me.discovery.services.RetrofitImpl;
import proj.me.discovery.views.CustomTextView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by root on 20/12/15.
 */
public class ActLaunch extends Activity implements ActivityCallback{

    private Animator buttonAnim;
    private CircularProgressButton loginButton;
    private RetrofitImpl retrofitImpl;

    private SharedPreferences preferences;
    private ParentFragment parentFragment;

    private LoginFrag loginFrag;
    private RegisterFrag registerFrag;

    private CustomTextView userText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.act_launch);
        preferences = getSharedPreferences("user", MODE_PRIVATE);
        if(preferences.getBoolean("isLogin", false)){
            final Intent i = new Intent(ActLaunch.this, ActDicovery.class);
            startActivity(i);
            finish();
        }
        loginFrag = new LoginFrag();
        registerFrag = new RegisterFrag();

        parentFragment = loginFrag;

        bindRetrofit();

        loginButton = (CircularProgressButton) findViewById(R.id.progress_button);
        userText = (CustomTextView)findViewById(R.id.user_text);

        buttonAnim = AnimatorInflater.loadAnimator(this, R.anim.down_up_button);
        buttonAnim.setStartDelay(750);
        buttonAnim.setTarget(loginButton);

        buttonAnim.start();

        loginButton.setIndeterminateProgressMode(true);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginButton.getProgress() == 0) {
                    if (!Utils.isInternetConnected(ActLaunch.this)) {
                        Utils.showToast(ActLaunch.this, "Network Unavailable");
                        return;
                    }
                    parentFragment.performAction();
                }/* else if (loginButton.getProgress() == 100) {
                    loginButton.setProgress(0);
                }*/ else if (loginButton.getProgress() == -1) {
                    loginButton.setProgress(0);
                }
            }
        });

        userText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userText.getText().toString().contains("New"))
                    callFragment(1);
                else
                    parentFragment.onBackNavigation();
            }
        });

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.container, loginFrag, "login");
        transaction.commit();
    }

    private void bindRetrofit(){

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("http://services-node12345js.rhcloud.com")
                .build();
        retrofitImpl = adapter.create(RetrofitImpl.class);

    }

    @Override
    public void startedTyping() {
        if(loginButton.getProgress() == -1)
            loginButton.setProgress(0);
    }

    @Override
    public void performLogin(LoginRequest loginRequest) {
        loginButton.setProgress(50);
        retrofitImpl.loginUser(loginRequest, new Callback<LoginResponse>() {
            @Override
            public void success(LoginResponse loginResponse, Response response) {
                Utils.logError("status" + loginResponse.getStatus());
                loginButton.setClickable(true);
                //userEdit.setVisibility(View.VISIBLE);
                loginButton.setProgress(100);
                Bundle bundle = new Bundle();
                bundle.putString("userId", loginResponse.getUser().getId());
                bundle.putString("userName", loginResponse.getUser().getFirstname() + loginResponse.getUser().getLastname());

                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isLogin", true);
                editor.putString("userName", loginResponse.getUser().getFirstname() + loginResponse.getUser().getLastname());
                editor.apply();

                final Intent i = new Intent(ActLaunch.this, ActDicovery.class);
                i.putExtras(bundle);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(i);
                        finish();
                    }
                }, 300);
            }
            @Override
            public void failure(RetrofitError error) {
                loginButton.setClickable(true);
                parentFragment.requestFailed(error.getLocalizedMessage());
                loginButton.setProgress(-1);
            }
        });
    }

    @Override
    public void performRegister(RegisterRequest registerRequest) {
        loginButton.setProgress(50);
        retrofitImpl.registerUser(registerRequest, new Callback<RegisterResponse>() {
            @Override
            public void success(RegisterResponse registerResponse, Response response) {
                Utils.logError("status" + registerResponse.getStatus());
                loginButton.setClickable(true);
                //userEdit.setVisibility(View.VISIBLE);
                loginButton.setProgress(100);
                parentFragment.onRegistrationSuccess();
            }

            @Override
            public void failure(RetrofitError error) {
                loginButton.setClickable(true);
                parentFragment.requestFailed(error.getLocalizedMessage());
                loginButton.setProgress(-1);
            }
        });
    }

    @Override
    public void onBackPressed() {
            parentFragment.onBackNavigation();
    }

    @Override
    public void callFragment(int id) {
        if(id == 0) {
            loginButton.setProgress(0);
            parentFragment = loginFrag;
            super.onBackPressed();
        }
        else{
            //call register fragment
            this.userText.setVisibility(View.INVISIBLE);
            parentFragment = registerFrag;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.container, registerFrag, "register");
            transaction.addToBackStack("login");
            transaction.commit();
        }
    }

    @Override
    public void animComplete(String userText, int visibility) {
        this.userText.setVisibility(visibility);
        this.userText.setText(userText);
    }
}
