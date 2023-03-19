package controllerAplikacije;

import StartAplication.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production.exception.NotSamePasswordException;
import production.exception.UnknownUserException;
import production.model.User;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static production.model.SerializationAndDeserialization.FILE_NAME;

public class LogInController {

    /**
     * method that show sign in screen
     */
    @FXML
    public void showSignInScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/controllerAplikacije/sign_in.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("Sign in!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }
    private static final Logger logger = LoggerFactory.getLogger(SignInController.class);
    @FXML
    private TextField nameTextField;

    @FXML
    private TextField passwordTextField;

    public static User trenutniKorisnik;

    /**
     * method that check if user is valid and let them into app if they are valid
     */
    @FXML
    public void onLoginButtonClick(){
        StringBuilder errorMessages = new StringBuilder();


        String enteredName = nameTextField.getText();
        String enteredPassword = passwordTextField.getText();

        if(enteredName.isEmpty()){
            errorMessages.append("Name should not be empty!\n");
        }
        if(enteredPassword.isEmpty()){
            errorMessages.append("password should not be empty!\n");
        }

        if(!errorMessages.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login action failed!");
            alert.setHeaderText("Errors");
            alert.setContentText(errorMessages.toString());
            alert.showAndWait();
            return;
        }

        try(ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(FILE_NAME )))
        {
            out.flush();
            trenutniKorisnik = new User(0, enteredName, enteredPassword);
            trenutniKorisnik.dohvatiUsera();

        }
        catch (UnknownUserException ex){
            logger.info(ex.getMessage(), ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Find action failed!");
            alert.setHeaderText("User data unknown!");
            alert.setContentText("User doesn't exist in app");
            alert.showAndWait();
            return;
        }catch (NotSamePasswordException ex){
            logger.info(ex.getMessage(), ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Find action failed!");
            alert.setHeaderText("User data not found!");
            alert.setContentText("WRONG PASSWORD!!!");
            alert.showAndWait();
            return;
        } catch (IOException e) {
            logger.info(e.getMessage(), e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Action failed!");
            alert.setContentText("NO serialization file");
            alert.showAndWait();
            return;
        }

        new MenuController().showHelloScreen();
    }

}
