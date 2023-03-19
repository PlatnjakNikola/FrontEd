package production.model;

import java.io.*;
import java.util.List;

public interface SerializationAndDeserialization {
    String FILE_NAME = "dat/changes.dat";

    default void serialize(List<SerializedObject> lista) throws IOException {
        try(ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(FILE_NAME ))){
            out.writeObject(lista);
        }
    }
    default List<SerializedObject> deSerialize() throws IOException, ClassNotFoundException {
        try(ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(FILE_NAME ))){
            return (List<SerializedObject>)in.readObject();
        }
    }
}
