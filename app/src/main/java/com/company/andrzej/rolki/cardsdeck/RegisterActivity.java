package com.company.andrzej.rolki.cardsdeck;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.editTextEmail)
    EditText editTextEmail;
    @BindView(R.id.editTextPassword)
    EditText editTextPassword;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.buttonRegister)
    public void registerNewUser() {
        registerUser();
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim().toLowerCase();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            //email is empty
            //stopping function execution further
            Toast.makeText(getApplicationContext(), "Please enter email..", Toast.LENGTH_SHORT).show();
            return;
        } else if (!Utils.isEmailValid(email)) {
            Toast.makeText(getApplicationContext(), "Please enter the valid email..", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            //password is empty
            //stopping function execution further
            Toast.makeText(getApplicationContext(), "Please enter password..", Toast.LENGTH_SHORT).show();
            return;
        } else if (!Utils.isPasswordValid(password)) {
            Toast.makeText(getApplicationContext(), "Yout password is to short...", Toast.LENGTH_SHORT).show();
            return;
        }
        //if validations is ok
        //email and password
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    //user is successfully registered and logged in
                    //we will start the profile activity here
                    //right now lets display a toast only
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    Toast.makeText(getApplicationContext(), "Registered Succesfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Could not register.. please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @OnClick(R.id.textViewSignin)
    public void openLoginOptions() {
        finish();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}
