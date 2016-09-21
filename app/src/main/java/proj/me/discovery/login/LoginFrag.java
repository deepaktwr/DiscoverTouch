package proj.me.discovery.login;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import proj.me.discovery.R;
import proj.me.discovery.Utils;
import proj.me.discovery.services.LoginRequest;
import proj.me.discovery.views.CustomEditText;

/**
 * Created by root on 23/12/15.
 */
public class LoginFrag extends ParentFragment{

    private CustomEditText userEdit;
    private static boolean isAnimDone;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_frag, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userEdit = (CustomEditText)view.findViewById(R.id.user_login);

        if(isAnimDone){
            userEdit.setAlpha(1f);
            userEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s != null && s.length() > 0)
                        activityCallback.startedTyping();
                }
            });
            return;
        }

        Animator editAnim = AnimatorInflater.loadAnimator(context, R.anim.down_to_up);
        editAnim.setStartDelay(100);
        editAnim.setTarget(userEdit);


        editAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                activityCallback.animComplete("New User?", View.VISIBLE);
                isAnimDone = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        editAnim.start();
    }
    @Override
    public void performAction() {
        if(TextUtils.isEmpty(userEdit.getText().toString())){
            Utils.showToast(context, "Please provide a user name");
            return;
        }
        userEdit.setVisibility(View.INVISIBLE);
        activityCallback.animComplete("New User?", View.INVISIBLE);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName(userEdit.getText().toString());
        activityCallback.performLogin(loginRequest);
    }

    @Override
    public void requestFailed(String errorMessage) {
        activityCallback.animComplete("New User?", View.VISIBLE);
        userEdit.setText(null);
        userEdit.setVisibility(View.VISIBLE);
        Utils.showToast(context, errorMessage);
    }
}
