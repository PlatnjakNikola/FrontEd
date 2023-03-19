package production.model;

import production.enums.OstalaDjela;

import java.io.Serializable;
import java.time.LocalDate;

public class OstalaKnjizevnaDjela extends Knjiga implements KnjizevniRod, Serializable {
    OstalaDjela djelo;

    public OstalaKnjizevnaDjela(Long id, Authors autor, String imeKnjige, LocalDate datumIzdavanja, String opisKnjige, Odjel odjel, String djelo) {
        super(id, autor, imeKnjige, datumIzdavanja, opisKnjige, odjel);
        this.djelo = pretvoriDjelo(djelo);
    }


    public OstalaDjela pretvoriDjelo(String naziv) {
        return switch (naziv.toUpperCase()){
            case "CASOPIS" -> OstalaDjela.CASOPIS;
            case "MANGA" -> OstalaDjela.MANGA;
            case "STRIP" -> OstalaDjela.STRIP;
            case "FILM" -> OstalaDjela.FILM;
            default -> OstalaDjela.GLAZBA;
        };
    }

    public OstalaDjela getDjelo() {
        return djelo;
    }

    public void setDjelo(OstalaDjela djelo) {
        this.djelo = djelo;
    }

    @Override
    public String getEntitet() {
        return djelo.toString();
    }

    @Override
    public String getAllDetails() {
        return getImeKnjige() + ", " + getAutor().getFullName() + ", " + getOpisKnjige() + ", " + getDjelo() + ", " + getDatumIzdavanja();
    }
}
