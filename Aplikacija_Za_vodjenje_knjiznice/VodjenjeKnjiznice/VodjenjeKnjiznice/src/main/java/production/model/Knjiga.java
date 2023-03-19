package production.model;

import production.enums.LirskaDjela;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class Knjiga extends Entitet implements Serializable {

    private Authors autor;
    private String imeKnjige;
    private LocalDate datumIzdavanja;
    private String opisKnjige;
    private Odjel odjel;

    public Knjiga(long id, Authors autor, String imeKnjige, LocalDate datumIzdavanja, String opisKnjige, Odjel odjel) {
        super(id);
        this.autor = autor;
        this.imeKnjige = imeKnjige;
        this.datumIzdavanja = datumIzdavanja;
        this.opisKnjige = opisKnjige;
        this.odjel = odjel;
    }

    public Authors getAutor() {
        return autor;
    }

    public void setAutor(Authors autor) {
        this.autor = autor;
    }

    public String getImeKnjige() {
        return imeKnjige;
    }

    public void setImeKnjige(String imeKnjige) {
        this.imeKnjige = imeKnjige;
    }

    public LocalDate getDatumIzdavanja() {
        return datumIzdavanja;
    }

    public void setDatumIzdavanja(LocalDate datumIzdavanja) {
        this.datumIzdavanja = datumIzdavanja;
    }

    public String getOpisKnjige() {
        return opisKnjige;
    }

    public void setOpisKnjige(String opisKnjige) {
        this.opisKnjige = opisKnjige;
    }

    public Odjel getOdjel() {
        return odjel;
    }

    public void setOdjel(Odjel odjel) {
        this.odjel = odjel;
    }

    public abstract String getAllDetails();


}
