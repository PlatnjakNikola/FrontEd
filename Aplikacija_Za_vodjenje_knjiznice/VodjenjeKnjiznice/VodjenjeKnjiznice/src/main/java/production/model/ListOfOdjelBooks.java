package production.model;

import java.util.List;

public interface ListOfOdjelBooks <T extends Knjiga> {
    List<T> allBooks(String rod);
}
