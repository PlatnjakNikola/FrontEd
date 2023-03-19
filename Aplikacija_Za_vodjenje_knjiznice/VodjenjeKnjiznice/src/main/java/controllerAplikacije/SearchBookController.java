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
import production.model.Knjiga;
import production.model.Odjel;
import production.model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SearchBookController {

    private static final ObservableList<String> observableGenresList = FXCollections.observableArrayList();

    @FXML
    private ChoiceBox<String> genresChoiceBox;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField authorTextField;

    @FXML
    private TableView<Knjiga> bookTableView;

    @FXML
    private TableColumn<Knjiga, String> nameOfBookColumn;
    @FXML
    private TableColumn<Knjiga, String> authorColumn;
    @FXML
    private TableColumn<Knjiga, String> publicitationDateColumn;
    @FXML
    private TableColumn<Knjiga, String> sectionInLibraryColumn;
    @FXML
    private ContextMenu contextMenu;

    static Knjiga newKnjiga;

    private static List<Knjiga> bookList = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(SearchBookController.class);
    private final User trenutniUser =  LogInController.trenutniKorisnik;

    /**
     * method that runs when screen opens
     */
    @FXML
    public void initialize(){
        try{
            System.out.println("Search book initialize has been executed!");

            bookList = Database.getAllBookFromDatabase();


            genresChoiceBox.setValue("all");
            observableGenresList.clear();
            observableGenresList.add("all");
            observableGenresList.addAll(Database.getAllOdjelFromDatabase().stream().map(Odjel::getNaziv).collect(Collectors.toSet()));
            genresChoiceBox.setItems(observableGenresList);

            ObservableList<Knjiga> bookObservableList = FXCollections.observableList(bookList);
            bookTableView.setItems(bookObservableList);

            nameOfBookColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getImeKnjige()));
            authorColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getAutor().getFullName()));
            publicitationDateColumn.setCellValueFactory(cellData ->{
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String formattedDate = formatter.format(cellData.getValue().getDatumIzdavanja());
                return new SimpleStringProperty(formattedDate);
            });
            sectionInLibraryColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getOdjel().getNaziv()));

            bookTableView.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
                if(t.getButton() == MouseButton.SECONDARY) {
                    contextMenu.show(bookTableView, t.getScreenX(), t.getScreenY());
                    newKnjiga = bookTableView.getSelectionModel().getSelectedItem();
                }
            });

            Timeline clock = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    LocalDateTime localDateTime = LocalDateTime.now();
                    HelloApplication.getStage().setTitle("Sada je "+ localDateTime.getHour() +":" +localDateTime.getMinute() + "sati");
                }
            }), new KeyFrame(Duration.seconds(10)));
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
     * method that shows book edit screen
     */
    @FXML
    public void pressedEditedItem(){

        if(trenutniUser.getUsername().equals("admin")){
            new MenuController().showBookEditScreen();
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
     * function that searches book on specific parameters
     */
    @FXML
    protected void onSearchButtonClick() {
        String enteredName = nameTextField.getText();
        String enteredAuthor = authorTextField.getText();
        String enteredGenres = genresChoiceBox.getValue();

        List<Knjiga> filteredList = bookList;


        if(!enteredName.isEmpty()){
            filteredList = filteredList.stream()
                    .filter(s -> s.getImeKnjige().toLowerCase().contains(enteredName.toLowerCase(Locale.ROOT)))
                    .collect(Collectors.toList());
        }
        if(!enteredAuthor.isEmpty()){
            filteredList = filteredList.stream()
                    .filter(s -> s.getAutor().getFullName().toLowerCase().contains(enteredAuthor.toLowerCase()))
                    .collect(Collectors.toList());

        }
        if(!enteredGenres.equals("all")){
            filteredList = filteredList.stream()
                    .filter(s -> s.getOdjel().getNaziv().equals(enteredGenres)).collect(Collectors.toList());

        }

        bookTableView.setItems(FXCollections.observableList(filteredList));
    }
}
