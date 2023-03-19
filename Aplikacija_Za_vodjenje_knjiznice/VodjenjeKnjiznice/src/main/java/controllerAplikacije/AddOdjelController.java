package controllerAplikacije;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production.model.Odjel;
import production.model.SerializationAndDeserialization;
import production.model.SerializedObject;
import production.model.User;
import production.threads.AddDepartmentThread;
import production.threads.EditDepartmentThread;

import java.io.IOException;

import static StartAplication.HelloApplication.listaSerijaliziranihObjekata;


public class AddOdjelController implements SerializationAndDeserialization {
    @FXML
    private TextField nameTextField;
    private static final Logger logger = LoggerFactory.getLogger(AddOdjelController.class);

    @FXML
    private ChoiceBox<String> genresChoiceBox;
    private static final ObservableList<String> observableGenresList = FXCollections.observableArrayList("epika", "lirika", "drama", "all");
    private Boolean update = Boolean.FALSE;
    private final User trenutniUser =  LogInController.trenutniKorisnik;

    private Odjel oldOdjel;

    /**
     * method that runs when screen opens
     */
    @FXML
    public void initialize(){
        System.out.println("Initialize has been executed!");
        genresChoiceBox.setValue("all");
        genresChoiceBox.setItems(observableGenresList);

        if(trenutniUser.getUsername().equals("admin")){
            oldOdjel = SearchOdjelController.newOdjel;
            setAll(SearchOdjelController.newOdjel);
        }
    }


    /**
     * method that sets department if it is chosen for update
     * @param odjel is object that is going to be edited
     */
    public void setAll(Odjel odjel){
        if(odjel != null){
            update = Boolean.TRUE;
            nameTextField.setText(odjel.getNaziv());
            genresChoiceBox.setValue(odjel.getRod());
            SearchOdjelController.newOdjel = null;
        }else{
            nameTextField.clear();
        }
    }

    /**
     * method that saves or update department into database when pressed specific button on screen
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
        Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION, "Are you shure you want to add Department? ", ButtonType.YES, ButtonType.NO);
        alert2.showAndWait();

        if (alert2.getResult() != ButtonType.YES) {
            return;
        }
        StringBuilder errorMessages = new StringBuilder();

        String enteredName = nameTextField.getText();
        if(enteredName.isEmpty()){
            errorMessages.append("Name should not be empty!\n");
        }
        String enteredGenre = genresChoiceBox.getValue();
        if(enteredGenre.isEmpty() || enteredGenre.equals("all")){
            errorMessages.append("Genre should not be empty!\n");
        }

        if(!errorMessages.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save action failed!");
            alert.setHeaderText("Odjel data not saved!");
            alert.setContentText(errorMessages.toString());
            alert.showAndWait();
            return;
        }

        Odjel updatedOdjel = new Odjel(4, enteredName, enteredGenre);

        if(update){
            try{
                EditDepartmentThread thread = new EditDepartmentThread(updatedOdjel);
                Thread newRunner = new Thread(thread);
                newRunner.start();
                listaSerijaliziranihObjekata.add(new SerializedObject(oldOdjel, updatedOdjel));
                serialize(listaSerijaliziranihObjekata);

            } catch(IOException ex){
                logger.info(ex.getMessage(), ex);
                System.out.println("pogreska kod spajanja na bazu");
                ex.printStackTrace();
            }
            update = Boolean.FALSE;
        }
        else{
            try{
                AddDepartmentThread thread = new AddDepartmentThread(updatedOdjel);
                Thread newRunner = new Thread(thread);
                newRunner.start();
                listaSerijaliziranihObjekata.add(new SerializedObject(oldOdjel, updatedOdjel));
                serialize(listaSerijaliziranihObjekata);

            } catch(IOException ex){
                logger.info(ex.getMessage(), ex);
                System.out.println("pogreska kod spajanja na bazu");
                ex.printStackTrace();
            }
        }


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Save action succeded!");
        alert.setHeaderText("Author data saved!");
        alert.showAndWait();
    }


}
