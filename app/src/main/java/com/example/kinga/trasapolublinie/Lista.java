package com.example.kinga.trasapolublinie;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kinga on 10.09.2018.
 */

public class Lista extends ArrayAdapter<Lokalizacje> {

    private List<Lokalizacje> lokalizacjeList;
    private TextView name;
    private TextView genere;
    private Activity contex;

    public Lista(Activity contex, List<Lokalizacje> lokalizacjeList){
        super(contex,R.layout.activity_lisa,lokalizacjeList);
        this.contex = contex;
        this.lokalizacjeList = lokalizacjeList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = contex.getLayoutInflater();
        View listViev = inflater.inflate(R.layout.activity_lisa,null,true);

        name = (TextView) listViev.findViewById(R.id.name);
        genere = (TextView) listViev.findViewById(R.id.genere);

        Lokalizacje lokalizacje = lokalizacjeList.get(position);
        name.setText(lokalizacje.getNazwa());
        genere.setText(lokalizacje.getOpis());

        return listViev;
    }
}
