package application;

import foundation.Map;
import foundation.MapElement;
import foundation.Position;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;

public class PathfinderCanvas extends Canvas {

	public static final int CELLSIZE = 87;

	private final Color LANDCOLOR = Color.SANDYBROWN;
	private final Color WATERCOLOR = Color.ROYALBLUE;
	private final Color STARTCOLOR = Color.DARKGREEN;
	private final Color FINISHCOLOR = Color.DARKRED;
	private final Color PATHFINDERCOLOR = Color.BLACK;
	private final Color EMPTYCOLOR = Color.LIGHTGRAY;

	private GraphicsContext gc = null;

	public PathfinderCanvas() {
		super(Map.MAPSIZE * CELLSIZE, Map.MAPSIZE * CELLSIZE);
		// create and apply effect
		InnerShadow is = new InnerShadow();
        is.setOffsetX(2.0f);
        is.setOffsetY(2.0f);
		this.setEffect(is);
		// Get the graphics context for the canvas. 
		gc = getGraphicsContext2D();
		// initialize picture
		for (int row = 0; row < Map.MAPSIZE; ++row)
			for (int column = 0; column < Map.MAPSIZE; ++column) {
				draw(MapElement.EMPTY, new Position(column, row), false);
			}

	}        

	public void clear() {
		gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
		// re-initialize picture
		for (int row = 0; row < Map.MAPSIZE; ++row)
			for (int column = 0; column < Map.MAPSIZE; ++column) {
				draw(MapElement.EMPTY, new Position(column, row), false);
			}
	}

	public void show(Map m) {

		for (int row = 0; row < Map.MAPSIZE; ++row)
			for (int column = 0; column < Map.MAPSIZE; ++column) {
				show(m, new Position(column, row), false);
			}
	}

	public void show(Map m, Position p, boolean pathfinder) {
		draw(m.getAt(p), p, pathfinder);	
	}
	
	private void draw(MapElement me, Position p, boolean pathfinder) {
		switch (me) {
		case LAND: case START: case FINISH:
			gc.setFill(LANDCOLOR);
			gc.fillRect(p.getColumn() * CELLSIZE, p.getRow() * CELLSIZE, CELLSIZE, CELLSIZE);
			if (pathfinder) {
				gc.setFill(PATHFINDERCOLOR);
				gc.fillOval(p.getColumn() * CELLSIZE + 1, p.getRow() * CELLSIZE + 1, CELLSIZE-2, CELLSIZE-2);			
			}
			if (me == MapElement.START) {
				gc.setFill(STARTCOLOR);
				gc.fillOval(p.getColumn() * CELLSIZE + 1, p.getRow() * CELLSIZE + 1, CELLSIZE-2, CELLSIZE-2);			
			}
			if (me == MapElement.FINISH) {
				gc.setFill(FINISHCOLOR);
				gc.fillOval(p.getColumn() * CELLSIZE + 1, p.getRow() * CELLSIZE + 1, CELLSIZE-2, CELLSIZE-2);			
			}
			break;
		case WATER: 
			gc.setFill(WATERCOLOR);
			gc.fillRect(p.getColumn() * CELLSIZE, p.getRow() * CELLSIZE, CELLSIZE, CELLSIZE);
			break;
		case EMPTY:
			gc.setFill(EMPTYCOLOR);
			gc.fillRect(p.getColumn() * CELLSIZE, p.getRow() * CELLSIZE, CELLSIZE, CELLSIZE);
			break;
		}
		
	}
}
