package com.example.kinga.trasapolublinie;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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
    private String pktId;
    private Spinner spiner;
    private EditText nazwa;
    private ListView list;
    private List<Lokalizacje> lokalizacjeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usun);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("lokalizacje");

        nazwa = (EditText)findViewById(R.id.nazwa);
        spiner = (Spinner)findViewById(R.id.spinner);

        list = (ListView)findViewById(R.id.list);
        lokalizacjeList = new ArrayList<>();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lokalizacjeList.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Lokalizacje lokalizacje = data.getValue(Lokalizacje.class);
                    lokalizacjeList.add(lokalizacje);
                }
                Lista adapter = new Lista(UsunActivity.this,lokalizacjeList);
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void Usun(View view){
        usunPunkt(pktId);
    }

    public void usunPunkt(String pktId){
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("lokalizacje").child(pktId);
        mDatabaseReference.removeValue();
        Toast.makeText(this,"Usunięto",Toast.LENGTH_SHORT).show();
    }
}
