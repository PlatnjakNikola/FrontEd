package production.enums;

import java.util.List;

public enum LirskaDjela {
    ODA,
    HIMNA,
    ELEGIJA,
    PJESMA,
    SATIRA;

    /**
     * method that connects all enums to list
     * @return list of enums
     */
    public static List<String> svaLirskaDjela(){
        return List.of(ODA.toString(), HIMNA.toString(), ELEGIJA.toString(), PJESMA.toString(), SATIRA.toString());
    }
}
