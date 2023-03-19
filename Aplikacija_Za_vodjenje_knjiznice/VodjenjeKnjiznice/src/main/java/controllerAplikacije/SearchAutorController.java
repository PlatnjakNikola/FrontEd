package controllerAplikacije;

import StartAplication.HelloApplication;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production.database.Database;
import production.model.Authors;
import production.model.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SearchAutorController {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField surnameTextField;
    @FXML
    private DatePicker dateOfBirthPicker;

    @FXML
    private TableView<Authors> autorTableView;
    @FXML
    private TableColumn<Authors, String> nameOfAutorColumn;
    @FXML
    private TableColumn<Authors, String> surnameOfAutorColumn;
    @FXML
    private TableColumn<Authors, String> dateOfBirthColumn;

    @FXML
    private ContextMenu contextMenu;

    static Authors newAuthor;

    private static List<Authors> authorsList = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(SearchAutorController.class);
    private final User trenutniUser =  LogInController.trenutniKorisnik;

    /**
     * method that runs when screen opens
     */
    @FXML
    public void initialize(){
        try{
            System.out.println("Search Author initialize has been executed!");

            authorsList = Database.getAllAuthorsFromDatabase();

            ObservableList<Authors> bookObservableList = FXCollections.observableList(authorsList);
            autorTableView.setItems(bookObservableList);

            nameOfAutorColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getName()));
            surnameOfAutorColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getSurname()));
            dateOfBirthColumn.setCellValueFactory(cellData ->{
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String formattedDate = formatter.format(cellData.getValue().getDateOfBirth());
                return new SimpleStringProperty(formattedDate);
            });



            autorTableView.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
                if(t.getButton() == MouseButton.SECONDARY) {
                    contextMenu.show(autorTableView, t.getScreenX(), t.getScreenY());
                    newAuthor = autorTableView.getSelectionModel().getSelectedItem();
                }
            });


            Timeline clock = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    LocalDateTime localDateTime = LocalDateTime.now();
                    HelloApplication.getStage().setTitle("Sada je "+ localDateTime.getHour() +":" +localDateTime.getMinute() + "sati");
                }
            }), new KeyFrame(Duration.seconds(60)));
            clock.setCycleCount(Animation.INDEFINITE);
            clock.play();
        }
        catch(SQLException | IOException ex){
            logger.info(ex.getMessage(), ex);
            System.out.println("pogreska kod spajanja na bazu");
            ex.printStackTrace();
        }
    }


    /**
     * function that open edit screen if item is selected in search screen
     */
    @FXML
    public void pressedEditedItem(){
        if(trenutniUser.getUsername().equals("admin")){
            new MenuController().showAddAuthorScreen();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("CAN'T DO THAT");
            alert.setHeaderText("WRONG PRIVELAGES");
            alert.setContentText("THIS USER CAN'T DO MANIPULATION OF DATA");
            alert.showAndWait();

        }

    }


    /**
     * function that shows authors for entered parameters
     */
    @FXML
    protected void onSearchButtonClick() {
        String enteredName = nameTextField.getText();
        String enteredSurname = surnameTextField.getText();
        String chosenDate = getDate(dateOfBirthPicker.getValue());
        List<Authors> filteredList = authorsList;


        if(!enteredName.isEmpty()){
            filteredList = filteredList.stream()
                    .filter(s -> s.getName().toLowerCase().contains(enteredName.toLowerCase(Locale.ROOT)))
                    .collect(Collectors.toList());

        }
        if(!enteredSurname.isEmpty()){
            filteredList = filteredList.stream()
                    .filter(s -> s.getSurname().toLowerCase().contains(enteredSurname.toLowerCase()))
                    .collect(Collectors.toList());

        }
        if(chosenDate!=null){
            filteredList= filteredList.stream().filter(s->s.getDateOfBirth().toString().equals(chosenDate)).collect(Collectors.toList());

        }
        autorTableView.setItems(FXCollections.observableList(filteredList));
    }


    /**
     * function thath converts date to string with specific pattern
     * @param localDate is date that need to convert
     * @return converted date
     */
    private String getDate(LocalDate localDate){
        if(localDate!=null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return localDate.format(formatter);
        }
        return null;
    }

}
