module RTCA {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.fxml;
    requires mongo.java.driver;
    requires spring.security.crypto;
    opens Controllers;
    exports GUI;
}