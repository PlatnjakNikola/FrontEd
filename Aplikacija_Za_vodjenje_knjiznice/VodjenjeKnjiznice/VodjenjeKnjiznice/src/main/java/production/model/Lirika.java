package production.model;

import production.enums.LirskaDjela;

import java.io.Serializable;
import java.time.LocalDate;

public class Lirika extends Knjiga implements KnjizevniRod, Serializable {
    LirskaDjela lirskoDjelo;

    public Lirika(Long id, Authors autor, String imeKnjige, LocalDate datumIzdavanja, String opisKnjige, Odjel odjel, String lirskoDjelo) {
        super(id, autor, imeKnjige, datumIzdavanja, opisKnjige, odjel);
        this.lirskoDjelo= pretvoriLirskoDjelo(lirskoDjelo);
    }

    public LirskaDjela pretvoriLirskoDjelo(String naziv) {
        return switch (naziv.toUpperCase()){
            case "ODA" -> LirskaDjela.ODA;
            case "HIMNA" -> LirskaDjela.HIMNA;
            case "ELEGIJA" -> LirskaDjela.ELEGIJA;
            case "PJESMA" -> LirskaDjela.PJESMA;
            default -> LirskaDjela.SATIRA;
        };
    }

    public LirskaDjela getLirskoDjelo() {
        return lirskoDjelo;
    }

    public void setLirskoDjelo(LirskaDjela lirskoDjelo) {
        this.lirskoDjelo = lirskoDjelo;
    }

    @Override
    public String getEntitet() {
        return lirskoDjelo.toString();
    }

    @Override
    public String getAllDetails() {
        return getImeKnjige() + ", " + getAutor().getFullName() + ", " + getOpisKnjige() + ", " + getLirskoDjelo() + ", " + getDatumIzdavanja();
    }
}
