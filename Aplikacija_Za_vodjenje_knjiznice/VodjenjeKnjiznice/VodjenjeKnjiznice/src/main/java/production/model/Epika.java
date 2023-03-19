package production.model;

import production.enums.EpskaDjela;

import java.io.Serializable;
import java.time.LocalDate;

public class Epika extends Knjiga implements KnjizevniRod, Serializable {
    EpskaDjela epskoDjelo;
    public Epika(Long id, Authors autor, String imeKnjige, LocalDate datumIzdavanja, String opisKnjige,Odjel odjel, String epskoDjelo) {
        super(id, autor, imeKnjige, datumIzdavanja, opisKnjige, odjel);
        this.epskoDjelo = pretvoriEpskoDjelo(epskoDjelo);
    }
    //builder pattern

    public EpskaDjela pretvoriEpskoDjelo(String naziv) {
        return switch (naziv.toUpperCase()){
            case "EPOS" -> EpskaDjela.EPOS;
            case "EPSKA_PJESMA" -> EpskaDjela.EPSKA_PJESMA;
            case "ROMANTIKA" -> EpskaDjela.ROMANTIKA;
            case "BASNA" -> EpskaDjela.BASNA;
            case "PRICA" -> EpskaDjela.PRICA;
            case "LEGENDA" -> EpskaDjela.LEGENDA;
            default -> EpskaDjela.ROMAN;
        };
    }

    public EpskaDjela getEpskoDjelo() {
        return epskoDjelo;
    }

    public void setEpskoDjelo(EpskaDjela epskoDjelo) {
        this.epskoDjelo = epskoDjelo;
    }

    @Override
    public String getEntitet() {
        return epskoDjelo.toString();
    }

    @Override
    public String getAllDetails() {
        return getImeKnjige() + ", " + getAutor().getFullName() + ", " + getOpisKnjige() + ", " + getEpskoDjelo() + ", " + getDatumIzdavanja();
    }
}
