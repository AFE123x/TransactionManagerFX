module com.example.rubankfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.example.rubankfx to javafx.fxml;
    exports com.example.rubankfx;
}