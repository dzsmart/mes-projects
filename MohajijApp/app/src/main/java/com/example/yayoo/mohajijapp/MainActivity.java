package com.example.yayoo.mohajijapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton;
    private EditText loginMail;
    private EditText loginPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("connexion Database");
        progressDialog.show();

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword("kiuk14@gmail.com", "1234560").addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,"connexion error", Toast.LENGTH_LONG).show();
                    finish();
                }
                else
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,"Logging succsssfuly", Toast.LENGTH_LONG).show();
            }
        });

        loginMail=(EditText) findViewById(R.id.login_mail);
        loginPassword=(EditText) findViewById(R.id.login_password);
        loginButton=(Button) findViewById(R.id.login_button);

        loginButton.setOnClickListener(this);

        FirebaseUser user=firebaseAuth.getCurrentUser();

        databaseReference= FirebaseDatabase.getInstance().getReference();


    }

    private void loginUser(){
        String email=loginMail.getText().toString().trim();
        String password=loginPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(email)){
            if (!TextUtils.isEmpty(password)){
                if (firebaseAuth.getCurrentUser()!=null){
                    searchUser();
                }
            }else Toast.makeText(this,"Please enter your password",Toast.LENGTH_LONG).show();
                return;
        }else Toast.makeText(this,"Please enter your email",Toast.LENGTH_LONG).show();
            return;
    }

    private void searchUser(){
        databaseReference.child("HaajInfo").addListenerForSingleValueEvent();
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }


    @Override
    public void onClick(View view){
        if (view==loginButton){
            loginUser();
        }
        if (view==loginPassword){
            //open profile activity
        }
    }
}
