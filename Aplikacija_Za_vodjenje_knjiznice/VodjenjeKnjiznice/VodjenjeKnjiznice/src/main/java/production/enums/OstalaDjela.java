package production.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public enum OstalaDjela {
    CASOPIS,
    MANGA,
    STRIP,
    FILM,
    GLAZBA;

    /**
     * method that connects all enums to list
     * @return list of enums
     */
    public static List<String> svaOstalaDjela(){
        return List.of(CASOPIS.toString(), MANGA.toString(), STRIP.toString(), FILM.toString(), GLAZBA.toString());
    }
}
