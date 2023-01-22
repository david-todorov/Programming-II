package application;

// Pathfinder
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class Pathfinder extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	public void start(Stage myStage) {

		// Setup the stage
		myStage.setTitle("Pathfinder");

		// Setup the graphical elements
		PathfinderCanvas observationCanvas = new PathfinderCanvas();
		PathfinderControllerPane pfPane = new PathfinderControllerPane(observationCanvas);

		// Create the HBox. 
		HBox rootNode = new HBox(10);

		rootNode.setPadding(new Insets(10, 10, 10, 10));

		rootNode.getChildren().addAll(pfPane, observationCanvas);

		// Create a scene. 
		Scene myScene = new Scene(rootNode, rootNode.getMinWidth(), rootNode.getMinHeight());

		// Set the scene on the stage.  
		myStage.setScene(myScene);
		myStage.setResizable(false);

		// Show the stage and its scene.
		myStage.show();

	}
	
}
