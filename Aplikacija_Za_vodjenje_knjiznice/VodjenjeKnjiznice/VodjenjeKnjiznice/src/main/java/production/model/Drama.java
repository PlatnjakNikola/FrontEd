package production.model;


import production.enums.DramskaDjela;

import java.io.Serializable;
import java.time.LocalDate;

public class Drama extends Knjiga implements KnjizevniRod, Serializable {

    DramskaDjela dramskoDjelo;

    public Drama(Long id, Authors autor, String imeKnjige, LocalDate datumIzdavanja, String opisKnjige,Odjel odjel, String dramskoDjelo) {
        super(id, autor, imeKnjige, datumIzdavanja, opisKnjige, odjel);
        this.dramskoDjelo = pretvoriDramskoDjelo(dramskoDjelo);
    }
    //builder pattern


    public DramskaDjela pretvoriDramskoDjelo(String naziv) {
        return switch (naziv.toLowerCase()){
            case "tragedija" -> DramskaDjela.TRAGEDIJA;
            case "komedija" -> DramskaDjela.KOMEDIJA;
            case "drama" -> DramskaDjela.DRAMA;
            case "tragikomedija" -> DramskaDjela.TRAGIKOMEDIJA;
            default -> DramskaDjela.MELODRAMA;
        };
    }

    public DramskaDjela getDramskoDjelo() {
        return dramskoDjelo;
    }

    public void setDramskoDjelo(DramskaDjela dramskoDjelo) {
        this.dramskoDjelo = dramskoDjelo;
    }

    @Override
    public String getEntitet() {
        return dramskoDjelo.toString();
    }

    @Override
    public String getAllDetails() {
        return getImeKnjige() + ", " + getAutor().getFullName() + ", " + getOpisKnjige() + ", " + getDramskoDjelo() + ", " + getDatumIzdavanja();
    }
}
