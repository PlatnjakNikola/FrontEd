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
import production.enums.DramskaDjela;
import production.enums.EpskaDjela;
import production.enums.LirskaDjela;
import production.enums.OstalaDjela;
import production.model.Odjel;
import production.model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SearchOdjelController {
    
    @FXML
    private TextField nameTextField;

    @FXML
    private TableView<Odjel> odjelTableView;
    @FXML
    private TableColumn<Odjel, String> nameOfOdjelColumn;
    @FXML
    private TableColumn<Odjel, String> idOfOdjelColumn;
    @FXML
    private TableColumn<Odjel, String> typeOfBooksColumn;
    @FXML
    private ChoiceBox<String> genresChoiceBox;

    private static final ObservableList<String> observableGenresList = FXCollections.observableArrayList("epika", "lirika", "drama", "all");
    
    @FXML
    private ContextMenu contextMenu;

    static Odjel newOdjel;

    private final User trenutniUser =  LogInController.trenutniKorisnik;

    private static List<Odjel> odjelsList = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(SearchOdjelController.class);

    /**
     * method that runs when screen opens
     */
    @FXML
    public void initialize(){
        try{
            System.out.println("Search odjel initialize has been executed!");

            genresChoiceBox.setValue("all");
            genresChoiceBox.setItems(observableGenresList);

            odjelsList = Database.getAllOdjelFromDatabase();

            ObservableList<Odjel> bookObservableList = FXCollections.observableList(odjelsList);

            odjelTableView.setItems(bookObservableList);

            nameOfOdjelColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getNaziv()));
            idOfOdjelColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
            typeOfBooksColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getKnjige())));

            odjelTableView.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
                if(t.getButton() == MouseButton.SECONDARY) {
                    contextMenu.show(odjelTableView, t.getScreenX(), t.getScreenY());
                    newOdjel = odjelTableView.getSelectionModel().getSelectedItem();
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
     * function that shows screen where Department can be edited
     */
    @FXML
    public void pressedEditedItem(){
        if(trenutniUser.getUsername().equals("admin")){
            new MenuController().showAddOdjelScreen();
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
     * function that search departments on specific parameters
     */
    @FXML
    protected void onSearchButtonClick() {
        String enteredName = nameTextField.getText();
        String enteredGenres = genresChoiceBox.getValue();

        List<Odjel> filteredList = odjelsList;

        if(!enteredName.isEmpty()){
            filteredList = filteredList.stream()
                    .filter(s -> s.getNaziv().toLowerCase().contains(enteredName.toLowerCase(Locale.ROOT)))
                    .collect(Collectors.toList());

        }
        if(!enteredGenres.equals("all")){
            switch (enteredGenres){
                case "epika" -> {
                    filteredList = filteredList.stream().filter(odjel -> odjel.getKnjige().equals(EpskaDjela.svaEpskaDjela())).toList();
                }
                case "lirika" -> {
                    filteredList = filteredList.stream().filter(odjel -> odjel.getKnjige().equals(LirskaDjela.svaLirskaDjela())).toList();
                }
                case "drama" -> {
                    filteredList = filteredList.stream().filter(odjel ->odjel.getKnjige().equals(DramskaDjela.svaDramskaDjela())).toList();
                }
                default -> {
                    filteredList = filteredList.stream().filter(odjel -> odjel.getKnjige().equals(OstalaDjela.svaOstalaDjela())).toList();
                }
            }

        }
        odjelTableView.setItems(FXCollections.observableList(filteredList));
    }

}
