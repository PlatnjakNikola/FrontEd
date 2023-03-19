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
import production.exception.UserAlreadyExistException;
import production.model.User;

import java.io.IOException;

public class SignInController {
    //go to login screen
    public void showLoginScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("log_in.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("Log in!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }
    private static final Logger logger = LoggerFactory.getLogger(SignInController.class);
    @FXML
    private TextField nameTextField;

    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField repeatedPasswordTextField;


    /**
     * method that shows login screen
     */
    @FXML
    public void onLoginButtonClick(){
        StringBuilder errorMessages = new StringBuilder();


        String enteredName = nameTextField.getText();
        String enteredPassword = passwordTextField.getText();
        String enteredRepeatedPassword = repeatedPasswordTextField.getText();
        if(enteredName.isEmpty()){
            errorMessages.append("Name should not be empty!\n");
        }
        if(enteredPassword.isEmpty()){
            errorMessages.append("password should not be empty!\n");
        }
        if(enteredRepeatedPassword.isEmpty()){
            errorMessages.append("repeated password should not be empty!\n");
        }

        if(!errorMessages.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save action failed!");
            alert.setHeaderText("User data not saved!");
            alert.setContentText(errorMessages.toString());
            alert.showAndWait();
            return;
        }

        try{
            User newUser = new User(0, enteredName, enteredPassword);
            newUser.pronalazakUsernamea(enteredName);
            newUser.usporedbaPassworda(enteredRepeatedPassword);
            newUser.dodajUseraUDatoteku();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save action succeded!");
            alert.setHeaderText("Author data saved!");
            alert.showAndWait();

        }catch (NotSamePasswordException ex){
            logger.info(ex.getMessage(), ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save action failed!");
            alert.setHeaderText("User data not saved!");
            alert.setContentText("Password must be the same");
            alert.showAndWait();
            return;
        }catch(UserAlreadyExistException ex){
            logger.info(ex.getMessage(), ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save action failed!");
            alert.setHeaderText("User data not saved!");
            alert.setContentText("User Already exist in app");
            alert.showAndWait();
            return;
        }

        showLoginScreen();
    }

}
