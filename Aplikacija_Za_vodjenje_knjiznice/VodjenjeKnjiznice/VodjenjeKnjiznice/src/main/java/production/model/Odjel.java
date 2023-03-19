package production.model;

import production.enums.DramskaDjela;
import production.enums.EpskaDjela;
import production.enums.LirskaDjela;
import production.enums.OstalaDjela;

import java.io.Serializable;
import java.util.List;
public class Odjel extends Entitet implements ListOfOdjelBooks, Serializable {

    String naziv;
    List<String> knjige;
    public Odjel(long id, String naziv, String rod) {
        super(id);
        this.naziv = naziv;
        this.knjige = allBooks(rod);
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public List<String> getKnjige() {
        return knjige;
    }

    public void setKnjige(List<String> knjige) {
        this.knjige = knjige;
    }

    @Override
    public List<String> allBooks(String rod) {
        switch (rod){
            case "epika" -> {
                return EpskaDjela.svaEpskaDjela();
            }
            case "lirika" -> {
                return LirskaDjela.svaLirskaDjela();
            }
            case "drama" -> {
                return DramskaDjela.svaDramskaDjela();
            }
            default -> {
                return OstalaDjela.svaOstalaDjela();
            }
        }
    }

    public String getRod(){
        String genre = getKnjige().toString();
        if(genre.equals(EpskaDjela.svaEpskaDjela().toString())){
            return "epika";
        }
        else if(genre.equals(DramskaDjela.svaDramskaDjela().toString())){
            return "drama";
        }
        else if(genre.equals(LirskaDjela.svaLirskaDjela().toString())){
            return "lirika";
        }
        else{
            return "ostala";
        }
    }

    public String getAllDetails(){
        return naziv + ", " + getKnjige();
    }
}
