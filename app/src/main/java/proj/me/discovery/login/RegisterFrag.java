package proj.me.discovery.login;

import android.animation.Animator;
import android.animation.AnimatorInflater;
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
import proj.me.discovery.services.RegisterRequest;
import proj.me.discovery.views.CustomEditText;

/**
 * Created by root on 23/12/15.
 */
public class RegisterFrag extends ParentFragment implements TextWatcher{

    private CustomEditText userName, userFirstName, userLastName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_frag, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userName = (CustomEditText)view.findViewById(R.id.user_name);
        userFirstName = (CustomEditText)view.findViewById(R.id.user_firstname);
        userLastName = (CustomEditText)view.findViewById(R.id.user_lastname);

        userName.addTextChangedListener(this);
        userFirstName.addTextChangedListener(this);
        userLastName.addTextChangedListener(this);


        performVisibleAnim();
    }


    @Override
    public void performAction() {
        if(TextUtils.isEmpty(userName.getText()) || TextUtils.isEmpty(userFirstName.getText())){
            Utils.showToast(context, "Please provide user name and first name");
            return;
        }
        //performInvisibleAnim();
        userName.setAlpha(0f);
        userFirstName.setAlpha(0f);
        userLastName.setVisibility(View.INVISIBLE);
        activityCallback.animComplete("Already Registered?", View.INVISIBLE);

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUserName(userName.getText().toString());
        registerRequest.setUserFirstName(userFirstName.getText().toString());
        registerRequest.setUserLastName(TextUtils.isEmpty(userLastName.getText()) ? "" : userLastName.getText().toString());
        activityCallback.performRegister(registerRequest);

    }

    @Override
    public void requestFailed(String errorMessage) {
        performVisibleAnim();
        Utils.showToast(context, errorMessage);
    }

    @Override
    void onBackNavigation() {
        performInvisibleAnim();
    }

    @Override
    void onRegistrationSuccess() {
        performInvisibleAnim();
    }

    private void performVisibleAnim(){
        userName.setText(null);
        userFirstName.setText(null);
        userLastName.setText(null);
        userLastName.setVisibility(View.VISIBLE);

        userName.setAlpha(0f);
        userFirstName.setAlpha(0f);

        Animator topAnim = AnimatorInflater.loadAnimator(context, R.anim.top_below_to_up);
        topAnim.setStartDelay(10);
        topAnim.setTarget(userName);

        Animator middleAnim = AnimatorInflater.loadAnimator(context, R.anim.middle_below_to_up);
        middleAnim.setStartDelay(20);
        middleAnim.setTarget(userFirstName);

        topAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                activityCallback.animComplete("Already Registered?", View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        topAnim.start();
        middleAnim.start();
    }
    private void performInvisibleAnim(){
        activityCallback.animComplete("New User?", View.INVISIBLE);
        userName.setText(null);
        userFirstName.setText(null);
        userLastName.setText(null);

        Animator topAnim = AnimatorInflater.loadAnimator(context, R.anim.top_up_to_below);
        topAnim.setStartDelay(10);
        topAnim.setTarget(userName);

        Animator middleAnim = AnimatorInflater.loadAnimator(context, R.anim.middle_up_to_below);
        middleAnim.setStartDelay(20);
        middleAnim.setTarget(userFirstName);

        topAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                activityCallback.animComplete("New User?", View.VISIBLE);
                userLastName.setVisibility(View.INVISIBLE);
                RegisterFrag.super.onBackNavigation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        topAnim.start();
        middleAnim.start();
    }

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
}
