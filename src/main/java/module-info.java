module com.example.cronometropsp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml.bind;


    opens com.example.cronometropsp to javafx.fxml;
    exports com.example.cronometropsp;
    exports com.example.cronometropsp.controller to javafx.fxml;
    opens com.example.cronometropsp.controller to javafx.fxml;
    opens com.example.cronometropsp.model to java.xml.bind;
    opens com.example.cronometropsp.conexion to java.xml.bind;
    exports com.example.cronometropsp.model;

}