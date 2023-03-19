package controllerAplikacije;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production.database.Database;
import production.model.*;

import java.io.IOException;
import java.sql.SQLException;

import static StartAplication.HelloApplication.listaSerijaliziranihObjekata;

public class DeleteBookController implements SerializationAndDeserialization {

    @FXML
    private ChoiceBox<String> bookChoiceBox;
    private final User trenutniUser =  LogInController.trenutniKorisnik;

    private static final Logger logger = LoggerFactory.getLogger(DeleteBookController.class);

    /**
     * method that runs when screen opens
     */
    @FXML
    public void initialize(){
        System.out.println("Initialize has been executed!");

        if(trenutniUser.getUsername().equals("admin")){
            try {
                ObservableList<String> bookObservableList= FXCollections.observableArrayList(Database.getAllBookFromDatabase().stream().map(Knjiga::getImeKnjige).toList());
                bookChoiceBox.setItems(bookObservableList);

            } catch (SQLException | IOException e) {
                logger.error("Database error", e);
                System.out.println("Pogreska u radu s bazom");
            }
        }
    }


    /**
     * function that deletes specific book from database when button is pressed
     */
    @FXML
    public void onDeleteButtonClick(){
        if(!trenutniUser.getUsername().equals("admin")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("CAN'T DO THAT");
            alert.setHeaderText("WRONG PRIVELAGES");
            alert.setContentText("THIS USER CAN'T DO MANIPULATION OF DATA");
            alert.showAndWait();
            return;
        }
        Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete book? ", ButtonType.YES, ButtonType.NO);
        alert2.showAndWait();

        if (alert2.getResult() != ButtonType.YES) {
            return;
        }


        String enteredBook= bookChoiceBox.getValue();
        if(enteredBook == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save action failed!");
            alert.setHeaderText("Book data not deleted!");
            alert.setContentText("Book pick should not be empty");
            alert.showAndWait();
            return;
        }


        try{
            listaSerijaliziranihObjekata.add(new SerializedObject(getBook(enteredBook), null));
            Database.deleteKnjizevnaDjelafromDatabase(getBook(enteredBook));
            serialize(listaSerijaliziranihObjekata);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Delete action succeded!");
            alert.setHeaderText("Book data deleted!");
            alert.showAndWait();
        } catch(SQLException | IOException ex){
            logger.info(ex.getMessage(), ex);
            System.out.println("pogreska kod spajanja na bazu");
        }

    }

    private Knjiga getBook(String bookName){
        try {
            VrsteKnjiga<Knjiga> knjiga = new VrsteKnjiga<>();
            knjiga.setKnjige(Database.getAllBookFromDatabase());
            return knjiga.dohvatiKnjiguPoImenu(bookName);
        } catch (SQLException | IOException e) {
            logger.error("error with database", e);
            System.out.println("Pogreska u bazi podataka");
        }
        return null;
    }
}
