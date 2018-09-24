package com.example.kinga.trasapolublinie;

import android.content.Intent;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AdministracjaActivity extends AppCompatActivity {

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
    private HashMap<String, String> meMap;
    private String value;
    private String key;
    private ArrayList<String> ble;
    private String genere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administracja);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        add = (EditText) findViewById(R.id.add);
        spinner = (Spinner) findViewById(R.id.spinner);

        konto_admina();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    Wybierz();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });
        //if(spinner.equals(zspinner.getSelectedItem().toString())) {
        //  Toast.makeText(this, "dupa" + spinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
        //}
        // spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,helper.retrieve()));


    }

    private void Wybierz() {
        genere = spinner.getSelectedItem().toString();
        value = meMap.get(genere);
    }

    public void konto_admina() {
       name=new ArrayList<>();
        ble=new ArrayList<>();
       meMap=new HashMap<String, String>();
       name.add("Wybierz co usunąć");
       bazaDanych = FirebaseDatabase.getInstance().getReference("Admin");
       bazaDanych.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot s : dataSnapshot.getChildren()){
                    Admin user = s.getValue(Admin.class);
                    name.add(user.getEmail());
                    meMap.put(user.getEmail(),user.getUid());
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

            mDatabaseReference = FirebaseDatabase.getInstance().getReference("Admin").child(value);
            mDatabaseReference.removeValue();

            Toast.makeText(this, "Usunięto konto: "+genere, Toast.LENGTH_SHORT).show();
            add.getText().clear();

            Intent intent = getIntent();
            finish();
            startActivity(intent);

       


    }

    private void dodajEmail() {

        email = add.getText().toString().trim();

        if(!TextUtils.isEmpty(email)) {

            String id = bazaDanych.push().getKey();
            Admin admin = new Admin(email,id);
            bazaDanych.child(id).setValue(admin);

            Toast.makeText(this, "Dodano", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Wprowadź E-mail",Toast.LENGTH_SHORT).show();
        }
        add.getText().clear();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
