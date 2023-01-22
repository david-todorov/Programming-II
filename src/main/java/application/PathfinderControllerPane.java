package application;

import java.io.File;

import ai.AI;
import foundation.Direction;
import foundation.Map;
import foundation.MapElement;
import foundation.Position;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class PathfinderControllerPane extends VBox {

	private final static int BUTTONWIDTH = 150;
	
	private Map map = null;
	private int steps = 0;
	private long time = 0;
	private Timeline timeline = null;
	private Position pathfinderPosition = null;
	private AI ai = null;

	// GUI elements
	private Label lblSteps = new Label();
	private Label lblStepMax = new Label();
	private Label lblTime = new Label();
	private Label lblTimeMax = new Label();
	private Button btnLoad = new Button("Load");
	private Button btnGo = new Button("Go");
	private Button btnStop = new Button("Stop");

	// Canvas
	private PathfinderCanvas observationCanvas = null;

	public PathfinderControllerPane(PathfinderCanvas observationCanvas) {
		super(10);
		this.observationCanvas = observationCanvas;

		// Set graphical properties

		this.setPadding(new Insets(0, 10, 20, 10));

		// Set up the animation
		timeline = new Timeline(new KeyFrame(Duration.millis(333), ae -> playStep()));
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Set the "Load" button 
		btnLoad.setMinWidth(BUTTONWIDTH);
		btnLoad.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Load Map");
				fileChooser.setInitialDirectory(new File(System.getProperty("user.dir"))); 
				fileChooser.getExtensionFilters().addAll(
						new FileChooser.ExtensionFilter("MAP", "*.map"),
						new FileChooser.ExtensionFilter("XML", "*.xml"),
						new FileChooser.ExtensionFilter("All Files", "*.*")
						);
				File file = fileChooser.showOpenDialog(btnLoad.getScene().getWindow());
				if (file != null) {
					map = new Map(file.getAbsolutePath());
					observationCanvas.show(map);
					btnGo.setDisable(false);
					lblSteps.setText("");
					lblStepMax.setText("StepMax\t" + map.getStepMax());
					lblTime.setText("");
					lblTimeMax.setText("TimeMax:\t" + map.getTimeMax() + ".0 s");
				}
			}
		});

		// Set the "Go" button 
		btnGo.setMinWidth(BUTTONWIDTH);
		btnGo.setDisable(true);
		btnGo.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				// Set up the counters
				steps = 0;
				time = 0;
				// Set up the GUI
				observationCanvas.show(map);
				btnStop.setDisable(false);
				btnLoad.setDisable(true);
				btnGo.setDisable(true);
				lblSteps.setText("Steps:\t" + steps);
				lblSteps.setText("Time:\t" + ((time / 100) / 10.0) + " s");
				// Set up the pathfinder
				pathfinderPosition = new Position(map.getStart());
				// Start up AI
				long begin = System.currentTimeMillis();
				ai = new AI(new Map(map));
				long end = System.currentTimeMillis();
				time += end - begin;
				// Play animation
				timeline.playFromStart();
			}
		});

		// Set the "Stop" button 
		btnStop.setMinWidth(BUTTONWIDTH);
		btnStop.setDisable(true);
		btnStop.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				btnStop.setDisable(true);
				btnLoad.setDisable(false);
				btnGo.setDisable(false);
				timeline.stop();
			}
		});

		// Add them to the box. 
		getChildren().addAll(btnLoad, btnGo, btnStop, new Separator(), 
				             lblSteps, lblStepMax, new Separator(), lblTime, lblTimeMax);

	}


	private void playStep() {

		// step ahead
		++steps;
		lblSteps.setText("Steps:\t" + steps);
		
		// Move the pathfinder
		if (pathfinderPosition != null) {
			// Call up AI and accept move
			long begin = System.currentTimeMillis();
			Direction dir = ai.move(pathfinderPosition);
			long end = System.currentTimeMillis();
			time += end - begin;
			lblTime.setText("Time:\t" + ((time / 100) / 10.0) + " s");
			// Legal move?
			Position newpos = pathfinderPosition.direct(dir);
			MapElement me = map.getAt(newpos);
			if (me != MapElement.WATER && me != MapElement.START) {
				// Display move --- in finish???
				observationCanvas.show(map, pathfinderPosition, false);
				pathfinderPosition = newpos;
				if (pathfinderPosition.equals(map.getFinish())) {
					pathfinderPosition = null;
				} else {
					observationCanvas.show(map, pathfinderPosition, true);
				}
			}
		}

		// abort when above step or time limit
		if (steps > map.getStepMax()) {
			pathfinderPosition = null;
			Alert a = new Alert(AlertType.ERROR, "Maximum number of steps exceeded ...", ButtonType.OK);
			a.show();
		}
		if (time / 1000.0 > map.getTimeMax() * 1.0) {
			pathfinderPosition = null;
			Alert a = new Alert(AlertType.ERROR, "Maximum time exceeded ...", ButtonType.OK);
			a.show();
		}


		// End of run ??
		if (pathfinderPosition == null) {
			btnStop.setDisable(true);
			btnLoad.setDisable(false);
			btnGo.setDisable(false);
			timeline.stop();                	
		}

	}

}
