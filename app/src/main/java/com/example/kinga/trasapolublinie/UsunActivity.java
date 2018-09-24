package com.example.kinga.trasapolublinie;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class UsunActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private Spinner spiner;
    private Spinner spiner2;
    private ArrayList<String> name;
    private ArrayAdapter<String> adapter;
    private String genere;
    private DatabaseReference baza;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usun);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spiner = (Spinner)findViewById(R.id.spinner);
        spiner2 = (Spinner)findViewById(R.id.spinner2);


        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


    }


    public void Usun(View view){
        usunPunkt();
    }

    public void usunPunkt(){
        String generuj = spiner2.getSelectedItem().toString();

        baza = FirebaseDatabase.getInstance().getReference("lokalizacje").child(genere).child(generuj);
        baza.removeValue();
        Toast.makeText(this,"Usunięto",Toast.LENGTH_SHORT).show();

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void Wybierz() {
        name=new ArrayList<>();
        name.add("Wybierz co usunąć");
        genere = spiner.getSelectedItem().toString();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("lokalizacje").child(genere);
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot s : dataSnapshot.getChildren()){
                    Lokalizacje user = s.getValue(Lokalizacje.class);
                    name.add(user.getNazwa());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,name);
        spiner2.setAdapter(adapter);

    }

}
