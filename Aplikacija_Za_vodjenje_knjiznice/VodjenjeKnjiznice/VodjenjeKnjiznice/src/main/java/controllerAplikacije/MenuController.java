package controllerAplikacije;

import StartAplication.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class MenuController {

    /**
     * function that shows start screen
     */
    public void showHelloScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/controllerAplikacije/welcome.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 700.0, 600.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("Welcome!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }

    /**
     * function that shows add author screen
     */
    public void showAddAuthorScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/controllerAplikacije/AddAuthor.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 700.0, 476.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("New Author!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }
    /**
     * function that shows search author screen
     */
    public void showAuthorSearchScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/controllerAplikacije/search_author.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 694.0, 515.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("Author search!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }
    /**
     * function that shows add department screen
     */
    //odjel screen
    public void showAddOdjelScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/controllerAplikacije/AddOdjel.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 685.0, 376.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("New Department!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }

    /**
     * function that shows search department screen
     */
    public void showOdjelSearchScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/controllerAplikacije/search_odjel.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 941.0, 515.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("Department Search!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }

    /**
     * function that shows add book screen
     */
    public void showAddBookScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/controllerAplikacije/AddBook.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 913.0, 512.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("New Book!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }

    /**
     * function that shows search book screen
     */
    public void showBookSearchScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/controllerAplikacije/search_book.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 941.0, 515.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("Book search!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }

    /**
     * function that shows book edit screen
     */
    public void showBookEditScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/controllerAplikacije/AddBook.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 913.0, 512.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("Book Edit!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }

    /**
     * function that shows delete book screen
     */
    public void showDeleteBookScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/controllerAplikacije/deleteBook.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 700.0, 400.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("Book Edit!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }

    /**
     * function that shows all changes screen
     */
    public void showChangeSearchScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/controllerAplikacije/changes.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 1164.0, 515.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("Changes Search!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }




}


