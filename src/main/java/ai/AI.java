
package ai;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import foundation.Map;
import foundation.MapElement;
import foundation.Direction;
import foundation.Position;

public class AI {

	private class Node {
		private Position position;
		private MapElement content;
		private Node parentNode;

		public Node(Position position, MapElement content, Node parentNode) {
			this.position = position;
			this.content = content;
			this.parentNode = parentNode;

		}

	}

	static final Direction[] DIRECTIONVALUES = Direction.values();
	static final Queue<Direction> PATH_DIRECTIONS = new LinkedList<>();
	private Map map = null;

	public AI(Map map) {

		this.map = map;
		findShortestPath(map);

	}

	private void findShortestPath(Map map) {
		Queue<Node> tempQueue = new LinkedList<>();
		Stack<Position> pathPositions = new Stack<>();
		LinkedList<Position> checkedPositions = new LinkedList<>();

		Node startindpointNode = new Node(map.getStart(), MapElement.START, null);
		tempQueue.add(startindpointNode);
		checkedPositions.add(startindpointNode.position);

		while (tempQueue.isEmpty() == false) {
			Node popedNode = tempQueue.poll();
			if (popedNode.content == MapElement.FINISH) {

				for (Node finishNode = popedNode; finishNode.position != this.map
						.getStart(); finishNode = finishNode.parentNode) {

					pathPositions.add(finishNode.position);
				}

			}

			for (int i = 0; i < this.DIRECTIONVALUES.length; i++) {
				Direction direction = this.DIRECTIONVALUES[i];
				Position positionOfNeighbour = popedNode.position.direct(direction);

				if (positionOfNeighbour.equals(popedNode.position) == false) {
					if (map.getAt(positionOfNeighbour) != MapElement.WATER) {
						if (checkedPositions.contains(positionOfNeighbour) == false) {
							MapElement contentElement = this.map.getAt(positionOfNeighbour);
							Node neighbourNode = new Node(positionOfNeighbour, contentElement, popedNode);
							checkedPositions.add(positionOfNeighbour);
							tempQueue.add(neighbourNode);
						}
					}
				}
			}

		}
		Position currentPosition = this.map.getStart();

		while (pathPositions.isEmpty() == false) {

			Position nextPosition = pathPositions.pop();

			for (int i = 0; i < this.DIRECTIONVALUES.length; i++) {
				Direction direction = this.DIRECTIONVALUES[i];
				Position searchingPosition = currentPosition.direct(direction);
				if (searchingPosition.equals(nextPosition)) {
					this.PATH_DIRECTIONS.add(direction);
					currentPosition = nextPosition;
					break;
				}

			}
		}

	}

	public Direction move(Position current) {

		Direction dir = null;
		Position newpos = null;

		do {
			dir = this.PATH_DIRECTIONS.poll();
			newpos = current.direct(dir);
		} while (newpos.equals(current) || map.getAt(newpos) == MapElement.WATER
				|| map.getAt(newpos) == MapElement.START);
		return dir;
	}

}
