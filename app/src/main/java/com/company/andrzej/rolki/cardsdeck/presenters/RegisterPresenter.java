package com.company.andrzej.rolki.cardsdeck.presenters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.company.andrzej.rolki.cardsdeck.MainActivity;
import com.company.andrzej.rolki.cardsdeck.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;

/**
 * Created by Katarzyna on 2017-05-26.
 */

public class RegisterPresenter {

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;


    public RegisterPresenter() {
    }

    public void registerUser(Activity activity, String email, String password) {
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(activity);
        if (TextUtils.isEmpty(email)) {
            //email is empty
            //stopping function execution further
            Toast.makeText(activity, "Please enter email..", Toast.LENGTH_SHORT).show();
            return;
        } else if (!Utils.isEmailValid(email)) {
            Toast.makeText(activity, "Please enter the valid email..", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            //password is empty
            //stopping function execution further
            Toast.makeText(activity, "Please enter password..", Toast.LENGTH_SHORT).show();
            return;
        } else if (!Utils.isPasswordValid(password)) {
            Toast.makeText(activity, "Yout password is to short...", Toast.LENGTH_SHORT).show();
            return;
        }
        //if validations is ok
        //email and password
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    //user is successfully registered and logged in
                    //we will start the profile activity here
                    //right now lets display a toast only
                    activity.startActivity(new Intent(activity, MainActivity.class));
                    Toast.makeText(activity, "Registered Succesfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Could not register.. please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
