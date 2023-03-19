package controllerAplikacije;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production.database.Database;
import production.enums.DramskaDjela;
import production.enums.EpskaDjela;
import production.enums.LirskaDjela;
import production.enums.OstalaDjela;
import production.model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static StartAplication.HelloApplication.listaSerijaliziranihObjekata;


public class AddBookController implements SerializationAndDeserialization {
    private static final Logger logger = LoggerFactory.getLogger(AddBookController.class);

    @FXML
    private TextField nameOfBookTextField;

    @FXML
    private TextArea summaryTextArea;

    @FXML
    private ChoiceBox<String>  vrstaDjelaChoiceBox;

    @FXML
    private ChoiceBox<String> odjelChoiceBox;
    @FXML
    private ChoiceBox<String> authorChoiceBox;
    @FXML
    private DatePicker publicitationDatePicker;


    private static final ObservableList<String> vrstaDjelaObservableList  =
            FXCollections.observableArrayList(
                    Stream.of(OstalaDjela.svaOstalaDjela(), LirskaDjela.svaLirskaDjela(), EpskaDjela.svaEpskaDjela(), DramskaDjela.svaDramskaDjela())
                            .flatMap(Collection::stream)
                            .collect(Collectors.toList()));

    private Boolean update = Boolean.FALSE;

    private final User trenutniUser =  LogInController.trenutniKorisnik;
    private Knjiga bookBefore;
    /**
     * method that runs when screen opens
     */
    @FXML
    public void initialize(){
        System.out.println("Initialize has been executed!");
        if(trenutniUser.getUsername().equals("admin")){
            bookBefore = SearchBookController.newKnjiga;
            setAll(SearchBookController.newKnjiga);
            try{
                vrstaDjelaChoiceBox.setItems(vrstaDjelaObservableList);

                ObservableList<String> vrstaOdjelaObservableList = FXCollections.observableArrayList(Database.getAllOdjelFromDatabase().stream().map(Odjel::getNaziv).collect(Collectors.toSet()));
                odjelChoiceBox.setItems(vrstaOdjelaObservableList);

                ObservableList<String> authorsObservableList = FXCollections.observableArrayList(Database.getAllAuthorsFromDatabase().stream().map(Authors::getFullNameWithId).collect(Collectors.toSet()));
                authorChoiceBox.setItems(authorsObservableList);

            }catch (SQLException |IOException ex) {
                System.out.println("pogreska kod spajanja na bazu");
            }
        }

    }

    private long idKnjige = 0;

    /**
     * method that sets book if it is chosen for update
     * @param knjiga is object that is going to be edited
     */
    public void setAll(Knjiga knjiga){
        if(knjiga != null){
            update = Boolean.TRUE;
            idKnjige = knjiga.getId();
            nameOfBookTextField.setText(knjiga.getImeKnjige());
            summaryTextArea.setText(knjiga.getOpisKnjige());
            authorChoiceBox.setValue(knjiga.getAutor().getFullNameWithId());
            odjelChoiceBox.setValue(knjiga.getOdjel().getNaziv());
            vrstaDjelaChoiceBox.setValue(((KnjizevniRod)knjiga).getEntitet());
            publicitationDatePicker.setValue(knjiga.getDatumIzdavanja());
            SearchBookController.newKnjiga = null;
        }else{
            nameOfBookTextField.clear();
            summaryTextArea.clear();
            authorChoiceBox.setValue(null);
            vrstaDjelaChoiceBox.setValue(null);
            odjelChoiceBox.setValue(null);
            publicitationDatePicker.setValue(null);
        }
    }

    /**
     * method that saves or update book into database when pressed specific button on screen
     */
    @FXML
    private void onSaveButtonClick(){
        if(!trenutniUser.getUsername().equals("admin")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("CAN'T DO THAT");
            alert.setHeaderText("WRONG PRIVELAGES");
            alert.setContentText("THIS USER CAN'T DO MANIPULATION OF DATA");
            alert.showAndWait();
            return;
        }
        Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION, "Are you shure you want to add book? ", ButtonType.YES, ButtonType.NO);
        alert2.showAndWait();

        if (alert2.getResult() != ButtonType.YES) {
            return;
        }
        StringBuilder errorMessages = new StringBuilder();


        String enteredName = nameOfBookTextField.getText();
        if(enteredName.isEmpty()){
            errorMessages.append("Name should not be empty!\n");
        }

        String enteredSummary = summaryTextArea.getText();
        if(enteredSummary.isEmpty()){
            errorMessages.append("Summary should not be empty!\n");
        }

        String enteredAuthor= authorChoiceBox.getValue();
        if(enteredAuthor == null) {
            errorMessages.append("Author should not be empty");
        }
        String enteredOdjel= odjelChoiceBox.getValue();
        if(enteredOdjel == null) {
            errorMessages.append("Department should not be empty");
        }
        String enteredVrstaDjela= vrstaDjelaChoiceBox.getValue();
        if(enteredVrstaDjela == null) {
            errorMessages.append("Vrsta djela should not be empty");
        }

        LocalDate enteredPublicitationDate = publicitationDatePicker.getValue();
        if(enteredPublicitationDate == null){
            errorMessages.append("Publicitation Date should not be empty");
        }

        if(!testirajKompatibilnostOdjelaIVrsteDjela(enteredVrstaDjela, enteredOdjel)){
            errorMessages.append("Vrsta djela vam ne paše uz odjel.");
        }

        if(!errorMessages.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save action failed!");
            alert.setHeaderText("Book data not saved!");
            alert.setContentText(errorMessages.toString());
            alert.showAndWait();
            return;
        }

        Knjiga updatedBook = vratiKnjigu(enteredAuthor, enteredOdjel, enteredName, enteredSummary, enteredVrstaDjela,enteredPublicitationDate);

        if(update){
            try{
                Database.updateKnjizevnaDjelaToDatabase(updatedBook);
                listaSerijaliziranihObjekata.add(new SerializedObject(bookBefore, updatedBook));
                serialize(listaSerijaliziranihObjekata);

                update = Boolean.FALSE;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Save action succeded!");
                alert.setHeaderText("Book data updated!");
                alert.showAndWait();
            } catch(SQLException | IOException ex){
                logger.info(ex.getMessage(), ex);
                System.out.println("pogreska kod spajanja na bazu");
            }
        }
        else{
            try{
                assert updatedBook != null;
                Database.insertNewKnjizevnaDjelaToDatabase(updatedBook);
                listaSerijaliziranihObjekata.add(new SerializedObject(bookBefore, updatedBook));
                serialize(listaSerijaliziranihObjekata);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Save action succeded!");
                alert.setHeaderText("Book data saved!");
                alert.showAndWait();
            } catch(SQLException | IOException ex){
                logger.info(ex.getMessage(), ex);
                System.out.println("pogreska kod spajanja na bazu");
            }
        }
    }

    /**
     * function that test if type of book is compatible with department
     * @param vrstaDjela name of book type
     * @param odjelEntered name of department
     * @return true if type and department are compatible
     */
    private boolean testirajKompatibilnostOdjelaIVrsteDjela(String vrstaDjela, String odjelEntered){
        try {
            if(vrstaDjela ==null || odjelEntered == null){
                return false;
            }
            Odjel odjel = Database.getAllOdjelFromDatabase().stream()
                    .filter(p -> p.getNaziv().equalsIgnoreCase(odjelEntered))
                    .findFirst().get();

            return odjel.getKnjige().contains(vrstaDjela);

        } catch (SQLException | IOException e) {
            logger.info("Greska u spajanju na bazu");
        }
        return false;
    }

    /**
     * method that creates objedct of type Knjiga
     * @param enteredAuthor name of author
     * @param enteredOdjel name of Department
     * @param enteredName name of book
     * @param enteredSummary summary of book
     * @param enteredVrstaDjela is type
     * @param enteredPublicitationDate is ddate when book is published
     * @return object of type Knjiga
     */
    private Knjiga vratiKnjigu( String enteredAuthor, String enteredOdjel, String enteredName, String enteredSummary, String enteredVrstaDjela, LocalDate enteredPublicitationDate){

        long authorId = ExtractNumber.extract(enteredAuthor);
        try {
            Authors author = Database.getAuthorFromDatabase(authorId);
            Odjel odjel = Database.getAllOdjelFromDatabase().stream()
                    .filter(p -> p.getNaziv().equalsIgnoreCase(enteredOdjel))
                    .findFirst().get();


            return switch (odjel.getRod()) {
                case "epika" ->
                        new Epika(idKnjige, author, enteredName, enteredPublicitationDate, enteredSummary, odjel, enteredVrstaDjela);
                case "lirika" ->
                        new Lirika(idKnjige, author, enteredName, enteredPublicitationDate, enteredSummary, odjel, enteredVrstaDjela);
                case "drama" ->
                        new Drama(idKnjige, author, enteredName, enteredPublicitationDate, enteredSummary, odjel, enteredVrstaDjela);
                default ->
                        new OstalaKnjizevnaDjela(idKnjige, author, enteredName, enteredPublicitationDate, enteredSummary, odjel, enteredVrstaDjela);
            };
        }
        catch (SQLException  | IOException e) {
            logger.info(e.getMessage(), e);
        }
        return null;
    }


}
