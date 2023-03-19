module main {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;
    exports controllerAplikacije;
    opens controllerAplikacije to javafx.fxml;
    exports StartAplication;
    opens StartAplication to javafx.fxml;
}
