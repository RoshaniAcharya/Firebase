package com.example.roshani.firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    EditText email,password;
    Button signin,login;
    FirebaseAuth lauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lauth=FirebaseAuth.getInstance();
        tv=findViewById(R.id.notuser);
        email=findViewById(R.id.email);
        password=findViewById(R.id.pass);
        signin=findViewById(R.id.btn2);
        login=findViewById(R.id.btn1);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this,StorageActivity.class);
                startActivity(i);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               lauth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                   @Override
                   public void onSuccess(AuthResult authResult) {
                       Toast.makeText(MainActivity.this,"login successful",Toast.LENGTH_SHORT).show();
                       Intent i=new Intent(MainActivity.this,Dashboard.class);
                       startActivity(i);
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(MainActivity.this,"login failed",Toast.LENGTH_SHORT).show();
                   }
               });
            }
        });
    }
}
