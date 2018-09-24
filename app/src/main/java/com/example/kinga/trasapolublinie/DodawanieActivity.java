package com.example.kinga.trasapolublinie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class DodawanieActivity extends AppCompatActivity {


    private EditText nazwa;
    private EditText opis;
    private EditText dl;
    private Spinner spiner;

    DatabaseReference bazaDanych;
    private EditText sz;
    private RadioGroup r1;
    private RadioButton radioLas1;
    private RadioButton darmowy;
    private RadioGroup grupa;
    private RadioButton platny;
    private boolean cena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodawanie);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        nazwa = (EditText)findViewById(R.id.nazwa);
        opis = (EditText)findViewById(R.id.opis);
        dl = (EditText)findViewById(R.id.dlugosc);
        sz = (EditText)findViewById(R.id.szerokosc);

        spiner = (Spinner)findViewById(R.id.spinner);

        bazaDanych = FirebaseDatabase.getInstance().getReference("lokalizacje");

    }

    public void Dodaj(View view){
            dodajLokalizacje();
    }

    private void dodajLokalizacje(){

        grupa = (RadioGroup)findViewById(R.id.r1);
        int zaz1 = grupa.getCheckedRadioButtonId();

        platny = (RadioButton)findViewById(R.id.platny);//platny == true
        darmowy = (RadioButton)findViewById(zaz1);

        platny.toggle();

        if((platny.equals(darmowy))){
            cena = true;
        }else cena = false;


        String name = nazwa.getText().toString().trim();
        String opi = opis.getText().toString().trim();
        String dlu = dl.getText().toString().trim();
        String szer = sz.getText().toString().trim();

        String genere = spiner.getSelectedItem().toString();


        if(!TextUtils.isEmpty(name)
                &&!TextUtils.isEmpty(opi)
                &&!TextUtils.isEmpty(dlu)
                &&!TextUtils.isEmpty(szer)
                &&!genere.equals("Wybierz kategorię"))

        {
            String id = bazaDanych.push().getKey();
            Lokalizacje lokalizacje = new Lokalizacje(id,name,opi,dlu,szer,cena);
            bazaDanych.child(genere).child(name).setValue(lokalizacje);

            Toast.makeText(this,"Dodano",Toast.LENGTH_SHORT).show();

            nazwa.getText().clear();
            opis.getText().clear();
            dl.getText().clear();
            sz.getText().clear();


        }else{
            Toast.makeText(this,"Wypełnij wszystkie pola",Toast.LENGTH_LONG).show();
        }


    }
}
