package com.example.kinga.trasapolublinie;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DodajAdminActivity extends AppCompatActivity {

    private EditText add;
    private DatabaseReference bazaDanych;
    private String email;
    private DatabaseReference mDatabaseReference;
    String cos;
    private Spinner spinner;
    private FirebaseHelper helper;
    private DatabaseReference bd;
    private ArrayList<String> name;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_admin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        add = (EditText) findViewById(R.id.add);
        spinner = (Spinner) findViewById(R.id.spinner);

        konto_admina();

        //if(spinner.equals(zspinner.getSelectedItem().toString())) {
        //  Toast.makeText(this, "dupa" + spinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
        //}
        // spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,helper.retrieve()));


    }

    public void konto_admina() {
       name=new ArrayList<>();
       name.add("Wybierz co usunąć");
       bazaDanych = FirebaseDatabase.getInstance().getReference("Admin");
       bazaDanych.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot s : dataSnapshot.getChildren()){
                    Admin user = s.getValue(Admin.class);
                    name.add(user.getEmail());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, name);
        spinner.setAdapter(adapter);

    }

    public void Dodaj(View view){
        dodajEmail();
    }

    public void Usun(View view){
        usunPunkt();
    }

    private void usunPunkt() {


        String genere = spinner.getSelectedItem().toString();


        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Admin").child(genere);
        mDatabaseReference.removeValue();
/*
        email = add.getText().toString().trim();
        bazaDanych.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()){
                    Admin user = s.getValue(Admin.class);

                    if(email.equals(user.getEmail())){
                       // bazaDanych.removeValue();
                         cos = user.getEmail();
                        //mDatabaseReference.removeValue();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });*/
       // mDatabaseReference = FirebaseDatabase.getInstance().getReference("Admin").child(cos);
        //mDatabaseReference.removeValue();


        Toast.makeText(this, "Usunięto konto: " +genere, Toast.LENGTH_SHORT).show();
        add.getText().clear();
        konto_admina();
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
