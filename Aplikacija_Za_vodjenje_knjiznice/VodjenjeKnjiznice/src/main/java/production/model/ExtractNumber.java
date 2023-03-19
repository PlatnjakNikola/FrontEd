package production.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface ExtractNumber {

    /**
     * method that removes id form name
     * @param input name of author and id together
     * @return id of author
     */
    static int extract(String input) {
        Pattern pattern = Pattern.compile("^(\\d+)\\.");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }
}