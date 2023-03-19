package production.enums;

import java.util.List;

public enum DramskaDjela {
    TRAGEDIJA,
    KOMEDIJA,
    DRAMA,
    TRAGIKOMEDIJA,
    MELODRAMA;

    /**
     * method that connects all enums to list
     * @return list of enums
     */
    public static  List<String> svaDramskaDjela(){
        return List.of(TRAGEDIJA.toString(), KOMEDIJA.toString(), DRAMA.toString(), TRAGIKOMEDIJA.toString(), MELODRAMA.toString());
    }
}
