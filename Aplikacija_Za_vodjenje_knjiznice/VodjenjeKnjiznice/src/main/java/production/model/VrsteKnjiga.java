package production.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VrsteKnjiga <T extends Knjiga> implements Serializable {
    private final List<T> listaKnjiga;

    public VrsteKnjiga() {
        this.listaKnjiga = new ArrayList<>();
    }

    public void dodajKnjigu(T knjiga){
        this.listaKnjiga.add(knjiga);
    }
    public void setKnjige(List<T> knjige){
        this.listaKnjiga.addAll(knjige);
    }
    public void izbrisiKnjigu(int index){
        listaKnjiga.remove(listaKnjiga.get(index));
    }
    public T dohvatiKnjigu(int index){
        do{
            try{
                return listaKnjiga.get(index);
            }catch (IndexOutOfBoundsException ex){
                //logger
                System.out.println("Ne postoji ustanova na tom indexu");
            }
        }while(true);
    }

    public T dohvatiKnjiguPoImenu(String ime){
        for(T k: listaKnjiga){
            if(k.getImeKnjige().equals(ime)){
                return k;
            }
        }
        return null;
    }

    public List<T> getListaKnjiga() {
        return listaKnjiga;
    }

    //dodaj knjigu
    //izbrisi knjigu
    //dohvati knjigu
    //dohvati listu knjiga
}
