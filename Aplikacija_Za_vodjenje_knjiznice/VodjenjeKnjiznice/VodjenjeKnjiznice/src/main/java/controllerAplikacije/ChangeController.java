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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production.model.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ChangeController implements SerializationAndDeserialization {

    private static final Logger logger = LoggerFactory.getLogger(ChangeController.class);
    @FXML
    private TableView<SerializedObject> tableTableView;

    @FXML
    private TableColumn<SerializedObject, String> TableChangedColumn;
    @FXML
    private TableColumn<SerializedObject, String> BeforeTableColumn;
    @FXML
    private TableColumn<SerializedObject, String> AfterTableColumn;
    @FXML
    private TableColumn<SerializedObject, String> DateChanger;

    private List<SerializedObject> newList;

    /**
     * method that runs when screen opens
     */
    @FXML
    public void initialize(){
        try {
            newList = deSerialize();

            ObservableList<SerializedObject> observableList = FXCollections.observableList(newList);
            tableTableView.setItems(observableList);

            TableChangedColumn.setCellValueFactory(cellData -> switch (testDatabaseQuery(cellData.getValue())) {
                case "delete" -> new SimpleStringProperty("DELETE");
                case "insert" -> new SimpleStringProperty("INSERT");
                default -> new SimpleStringProperty("UPDATE");
            });

            BeforeTableColumn.setCellValueFactory(cellData -> {
                if(testIfNull(cellData.getValue().getObjectBefore())) return new SimpleStringProperty("");
                switch (testInstance(cellData.getValue().getObjectBefore())) {
                    case "autor" -> {
                        return new SimpleStringProperty(
                                    ((Authors)cellData.getValue().getObjectBefore()).getAllDetails());
                    }
                    case "odjel" -> {
                            return new SimpleStringProperty(
                                    ((Odjel)cellData.getValue().getObjectBefore()).getAllDetails());
                    }
                    default -> {
                        return new SimpleStringProperty(((Knjiga)cellData.getValue().getObjectBefore()).getAllDetails());
                    }
                }
            });

            AfterTableColumn.setCellValueFactory(cellData -> {
                if(testIfNull(cellData.getValue().getObjectAfter())) return new SimpleStringProperty("");
                switch (testInstance(cellData.getValue().getObjectAfter())) {
                    case "autor" -> {
                        return new SimpleStringProperty(
                                ((Authors)cellData.getValue().getObjectAfter()).getAllDetails());
                    }
                    case "odjel" -> {
                        return new SimpleStringProperty(
                                ((Odjel)cellData.getValue().getObjectAfter()).getAllDetails());
                    }
                    default -> {
                        return new SimpleStringProperty(((Knjiga)cellData.getValue().getObjectAfter()).getAllDetails());
                    }
                }
            });
            DateChanger.setCellValueFactory(cellData ->{
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String formattedDate = formatter.format(cellData.getValue().getDate());
                return new SimpleStringProperty(formattedDate);
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
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Something wrong with file changes.dat");
        }

    }

    private String testDatabaseQuery(SerializedObject object){
        if(object.getObjectAfter() == null)
            return "delete";
        else if (object.getObjectBefore() == null)
            return "insert";
        return "update";
    }
    private <T> String testInstance(T object){
        if(object instanceof Authors){
            return "autor";
        } else if (object instanceof Odjel) {
            return  "odjel";
        }
        return "book";
    }
    private boolean testIfNull(Object object){
        return object == null;
    }

}
