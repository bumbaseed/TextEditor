module com.example.texteditorv2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.fxmisc.richtext;


    opens com.example.texteditorv2 to javafx.fxml;
    exports com.example.texteditorv2;
}