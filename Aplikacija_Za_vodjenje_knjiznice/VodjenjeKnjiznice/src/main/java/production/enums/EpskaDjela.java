package production.enums;

import java.util.List;

public enum EpskaDjela {
    EPOS,
    EPSKA_PJESMA,
    ROMANTIKA,
    BASNA,
    PRICA,
    LEGENDA,
    ROMAN;
    /**
     * method that connects all enums to list
     * @return list of enums
     */
    public static  List<String> svaEpskaDjela(){
        return List.of(EPOS.toString(), EPSKA_PJESMA.toString(), ROMANTIKA.toString(), BASNA.toString(), PRICA.toString(), LEGENDA.toString(), ROMAN.toString());
    }
}
