package com.example.kinga.trasapolublinie;

/**
 * Created by kinga on 20.09.2018.
 */

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class FirebaseHelper {

    DatabaseReference db;
    Boolean saved=null;

    public FirebaseHelper(DatabaseReference db) {
        this.db = db;
    }



    //READ
    public ArrayList<String> retrieve()
    {
        final ArrayList<String> spacecrafts=new ArrayList<>();

        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot,spacecrafts);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot,spacecrafts);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return spacecrafts;
    }

    private void fetchData(DataSnapshot snapshot,ArrayList<String> spacecrafts)
    {
        spacecrafts.clear();
        spacecrafts.add("dupa");
        for (DataSnapshot ds:snapshot.child("Bezpłatne").getChildren())
        {
            String name=ds.getValue(Lokalizacje.class).getNazwa();
            spacecrafts.add(name);
        }

    }
}
