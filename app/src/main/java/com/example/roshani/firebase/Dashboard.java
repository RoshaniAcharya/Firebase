package com.example.roshani.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {
    EditText rol,nam;
    Button save;
    DatabaseReference db;
    ListView lv;
    List<Module> list=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        rol=findViewById(R.id.rolld);
        nam=findViewById(R.id.named);
        lv=findViewById(R.id.list);
        save=findViewById(R.id.btn1);
        db= FirebaseDatabase.getInstance().getReference().child("user");
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Module m=new Module();
                m.setName(nam.getText().toString());
                m.setRoll(Integer.parseInt(rol.getText().toString()));
                db.push().setValue(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Dashboard.this,"successful",Toast.LENGTH_SHORT).show();
                        onStart();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Dashboard.this,"faillure",Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot dss:dataSnapshot.getChildren())
                {
                    Module m=new Module();
                    m.setRoll(Integer.parseInt(dss.child("roll").getValue().toString()));
                    m.setName(dss.child("name").getValue().toString());
                    list.add(m);
                }
                lv.setAdapter(new CustomAdapter(Dashboard.this,list));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
