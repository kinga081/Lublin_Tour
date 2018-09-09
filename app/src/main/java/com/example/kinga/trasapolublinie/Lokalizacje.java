package com.example.kinga.trasapolublinie;

/**
 * Created by kinga on 09.09.2018.
 */

public class Lokalizacje {

    String LocalId;
    String nazwa;
    String opis;
    String wspolrzedne;
    String kategoria;

    public Lokalizacje(){

    }

    public Lokalizacje(String localId, String nazwa, String opis, String wspolrzedne, String kategoria) {
        LocalId = localId;
        this.nazwa = nazwa;
        this.opis = opis;
        this.wspolrzedne = wspolrzedne;
        this.kategoria = kategoria;
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

    public String getWspolrzedne() {
        return wspolrzedne;
    }

    public String getKategoria() {
        return kategoria;
    }
}
