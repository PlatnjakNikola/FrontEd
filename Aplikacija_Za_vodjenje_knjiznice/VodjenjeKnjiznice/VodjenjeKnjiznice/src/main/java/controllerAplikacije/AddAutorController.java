package controllerAplikacije;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production.database.Database;
import production.model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import static StartAplication.HelloApplication.listaSerijaliziranihObjekata;

/**
 * class for controling add author screen
 */
public class AddAutorController implements SerializationAndDeserialization{
    private static final Logger logger = LoggerFactory.getLogger(AddAutorController.class);
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField surnameTextField;

    @FXML
    private DatePicker dateOfBirthDatePicker;


    private Boolean update = Boolean.FALSE;

    private Authors authorBefore;


    /**
     * method that runs when screen opens
     */
    @FXML
    public void initialize(){
        System.out.println("Initialize has been executed!");
        if(trenutniUser.getUsername().equals("admin")){
            authorBefore = SearchAutorController.newAuthor;
            setAll(SearchAutorController.newAuthor);
        }
    }

    /**
     * method that sets author if he is chosen for update
     * @param authors is object that is going to be edited
     */
    public void setAll(Authors authors){
        if(authors != null){
            update = Boolean.TRUE;
            idAuthora = authors.getId();
            nameTextField.setText(authors.getName());
            surnameTextField.setText(authors.getSurname());
            dateOfBirthDatePicker.setValue(authors.getDateOfBirth());

            SearchAutorController.newAuthor = null;
        }else{
            nameTextField.clear();
            surnameTextField.clear();
            dateOfBirthDatePicker.setValue(null);
        }
    }
    private static long idAuthora;
    private final User trenutniUser =  LogInController.trenutniKorisnik;


    /**
     * method that saves or update author into database when pressed specific button on screen
     */
    @FXML
    public void onSaveButtonClick(){
        if(!trenutniUser.getUsername().equals("admin")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("CAN'T DO THAT");
            alert.setHeaderText("WRONG PRIVELAGES");
            alert.setContentText("THIS USER CAN'T DO MANIPULATION OF DATA");
            alert.showAndWait();
            return;
        }
        Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to add author? ", ButtonType.YES, ButtonType.NO);
        alert2.showAndWait();

        if (alert2.getResult() != ButtonType.YES) {
            return; 
        }
        StringBuilder errorMessages = new StringBuilder();


        String enteredName = nameTextField.getText();
        if(enteredName.isEmpty()){
            errorMessages.append("Name should not be empty!\n");
        }

        String enteredSurname = surnameTextField.getText();
        if(enteredSurname.isEmpty()){
            errorMessages.append("Surname should not be empty!\n");
        }

        LocalDate enteredDateOfBirth = dateOfBirthDatePicker.getValue();
        if(enteredDateOfBirth == null){
            errorMessages.append("Date of birth should not be empty");
        }

        if(!errorMessages.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save action failed!");
            alert.setHeaderText("Book data not saved!");
            alert.setContentText(errorMessages.toString());
            alert.showAndWait();
            return;
        }


        Authors updatedAuthor = new Authors.Builder(idAuthora)
                .withIme(enteredName)
                .withSurname(enteredSurname)
                .withDateOfBirth(enteredDateOfBirth)
                .build();

        if(update){
            try{
                Database.updateAuthorToDatabase(updatedAuthor);
                listaSerijaliziranihObjekata.add(new SerializedObject(authorBefore, updatedAuthor));
                serialize(listaSerijaliziranihObjekata);
            } catch(SQLException | IOException ex){
                logger.info(ex.getMessage(), ex);
                System.out.println("pogreska kod spajanja na bazu");
            }
            update = Boolean.FALSE;
        }
        else{
            try{
                Database.insertNewAuthorToDatabase(updatedAuthor);
                listaSerijaliziranihObjekata.add(new SerializedObject(authorBefore, updatedAuthor));
                serialize(listaSerijaliziranihObjekata);
            } catch(SQLException | IOException ex){
                logger.info(ex.getMessage(), ex);
                System.out.println("pogreska kod spajanja na bazu");
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Save action succeded!");
        alert.setHeaderText("Author data saved!");
        alert.showAndWait();
    }



}
