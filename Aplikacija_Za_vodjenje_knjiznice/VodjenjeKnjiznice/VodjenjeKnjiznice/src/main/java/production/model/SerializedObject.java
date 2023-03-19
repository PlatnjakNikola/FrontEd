package production.model;

import java.io.*;
import java.time.LocalDate;

public class SerializedObject <T> implements Serializable {
    T objectBefore;
    T objectAfter;
    LocalDate vrijemePromjena;

    public SerializedObject(T objectBefore, T objectAfter) {
        this.objectBefore = objectBefore;
        this.objectAfter = objectAfter;
        this.vrijemePromjena = LocalDate.now();
    }

    public T getObjectBefore() {
        return objectBefore;
    }

    public T getObjectAfter() {
        return objectAfter;
    }

    public LocalDate getDate(){
        return vrijemePromjena;
    }


}
