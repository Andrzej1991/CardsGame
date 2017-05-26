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

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.editTextEmail)
    EditText editTextEmail;
    @BindView(R.id.editTextPassword)
    EditText editTextPassword;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            //profile activity here
        }
    }

    @OnClick(R.id.buttonSignIn)
    public void signIn() {
        userLogin();
    }

    private void userLogin() {
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

        progressDialog.setMessage("Loggin...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();


                        if (task.isSuccessful()) {
                            //start the game activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Utils.showToast(getApplicationContext(), "Something went wrong.. try again");
                        }

                    }
                });
    }

    @OnClick(R.id.textViewRegister)
    public void startRegisterActivity() {
        finish();
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
