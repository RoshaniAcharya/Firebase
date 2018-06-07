package com.example.roshani.firebase;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class StorageActivity extends AppCompatActivity {
    private static final int INT_CONST = 7;
    Button btn1,btn2;
    TextView tv;
    ListView lv;

    Uri dataUri;
    StorageReference sr;
    DatabaseReference dr;
    ProgressDialog dialog;
    int permissioncheck;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        permissioncheck= ContextCompat.checkSelfPermission(StorageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissioncheck!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},17);
        }
        btn1=findViewById(R.id.btnChoose);
        dr= FirebaseDatabase.getInstance().getReference().child("url");
        sr= FirebaseStorage.getInstance().getReference().child("files");
        btn2=findViewById(R.id.btnUpload);
        tv=findViewById(R.id.filename);
        lv=findViewById(R.id.list1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("*/*");
                startActivityForResult(i,INT_CONST);


            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dataUri!=null){
                    dialog=new ProgressDialog(StorageActivity.this);
                    dialog.setMessage("Uploading");
                    dialog.setCancelable(false);
                    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    dialog.show();
                    sr.putFile(dataUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            String downloadUrl=taskSnapshot.getDownloadUrl().toString();
                            dr.push().setValue(downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(StorageActivity.this,"successsss",Toast.LENGTH_SHORT).show();
                                    onStart();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(StorageActivity.this,"failed to success",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(StorageActivity.this,"failure",Toast.LENGTH_SHORT).show();

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            int Percentage=(int)(taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount()*100);
                            dialog.setProgress(Percentage);



                        }
                    });
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==INT_CONST && resultCode==RESULT_OK){
            dataUri=data.getData();
            tv.setText(dataUri.toString());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0]==PackageManager.PERMISSION_GRANTED){

        }else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},17);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    String url=ds.getValue().toString();
                }
                lv.setAdapter(new TheAdapter(StorageActivity.this,lst));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
