package com.company.andrzej.rolki.cardsdeck;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.company.andrzej.rolki.cardsdeck.presenters.RegisterPresenter;
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

   private RegisterPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
       if(presenter == null){
           presenter = new RegisterPresenter();
       }
    }

    @OnClick(R.id.buttonRegister)
    public void registerNewUser() {
        String email = editTextEmail.getText().toString().trim().toLowerCase();
        String password = editTextPassword.getText().toString().trim();
        presenter.registerUser(this, email, password);
    }


    @OnClick(R.id.textViewSignin)
    public void openLoginOptions() {
        finish();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}
