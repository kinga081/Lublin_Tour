package com.example.kinga.trasapolublinie;



public class Lokalizacje {

    String LocalId;
    String nazwa;
    String opis;
    String wspolrzedne;
    String kategoria;
    String dlugosc;
    String szerokosc;


    public Lokalizacje() {
    }

    public Lokalizacje(String localId, String nazwa, String opis,String dlugosc, String szerokosc) {
        LocalId = localId;
        this.nazwa = nazwa;
        this.opis = opis;
        this.dlugosc = dlugosc;
        this.szerokosc = szerokosc;
    }

    public String getDlugosc() {
        return dlugosc;
    }

    public String getSzerokosc() {
        return szerokosc;
    }

    public String getLocalId() {
        return LocalId;
    }

    public String getNazwa() {
        return nazwa;
    }

    public String getOpis() {
        return opis;
    }


    public String getKategoria() {
        return kategoria;
    }
}
