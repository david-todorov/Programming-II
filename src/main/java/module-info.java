module pathfinder.pathfinder {
    requires javafx.controls;
    requires javafx.fxml;


    opens application to javafx.fxml;
    exports application;
    exports foundation;
    exports ai;

}