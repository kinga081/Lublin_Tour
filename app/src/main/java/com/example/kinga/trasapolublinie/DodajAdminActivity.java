package com.example.kinga.trasapolublinie;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DodajAdminActivity extends AppCompatActivity {

    private EditText add;
    private DatabaseReference bazaDanych;
    private String email;
    private DatabaseReference mDatabaseReference;
    String cos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_admin);

        add = (EditText)findViewById(R.id.add);

        bazaDanych = FirebaseDatabase.getInstance().getReference("Admin");


    }

    public void Dodaj(View view){
        dodajEmail();
    }

    public void Usun(View view){
        usunPunkt();
    }

    private void usunPunkt() {

        email = add.getText().toString().trim();
        bazaDanych.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()){
                    Admin user = s.getValue(Admin.class);

                    if(email.equals(user.getEmail())){
                         cos = user.getUid();
                        //mDatabaseReference.removeValue();
                        break;

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
       // mDatabaseReference = FirebaseDatabase.getInstance().getReference("Admin").child(cos);
        //mDatabaseReference.removeValue();
        Toast.makeText(this, "Usunięto"+cos, Toast.LENGTH_SHORT).show();
        add.getText().clear();
    }

    private void dodajEmail() {


        email = add.getText().toString().trim();

        if(!TextUtils.isEmpty(email)) {

            String id = bazaDanych.push().getKey();
            Admin admin = new Admin(email,id);
            bazaDanych.child(id).setValue(admin);

            Toast.makeText(this, "Dodano", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Wprowadź E-mail",Toast.LENGTH_LONG).show();
        }
        add.getText().clear();
    }
}
