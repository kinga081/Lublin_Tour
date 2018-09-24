package com.example.kinga.trasapolublinie;



public class Lokalizacje {

    private String LocalId;
    private String nazwa;
    private String opis;
    private String dlugosc;
    private String szerokosc;
    private boolean cena;


    public Lokalizacje(){}

    public Lokalizacje(String localId, String nazwa, String opis, String dlugosc, String szerokosc, boolean cena) {
        LocalId = localId;
        this.nazwa = nazwa;
        this.opis = opis;
        this.dlugosc = dlugosc;
        this.szerokosc = szerokosc;
        this.cena = cena;
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

    public String getDlugosc() {
        return dlugosc;
    }

    public String getSzerokosc() {
        return szerokosc;
    }

    public boolean isCena() {
        return cena;
    }
}
