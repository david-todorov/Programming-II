package foundation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Map {

	public static final int MAPSIZE = 10;

	private MapElement[][] map = new MapElement[MAPSIZE][MAPSIZE];
	private Position start = null;
	private Position finish = null;
	private int stepMax = 0;
	private int timeMax = 0;

	public Map() {
		for (int i = 0; i < MAPSIZE; ++i)
			for(int j = 0; j < MAPSIZE; ++j) 
				map[i][j] = MapElement.EMPTY;
		start = null;
		finish = null;
		stepMax = 0;
		timeMax = 0;
	}

	public Map(Map m) {
		for (int i = 0; i < MAPSIZE; ++i)
			for(int j = 0; j < MAPSIZE; ++j) 
				map[i][j] = m.map[i][j];
		start = new Position(m.start); 
		finish = new Position(m.finish);
		stepMax = m.stepMax;
		timeMax = m.timeMax;
	}

	public Map(String fileName) {
		this();

		String line = "";
		String[] result = null;
		BufferedReader in = null;

		try {
			// open the file
			in = new BufferedReader(new FileReader(fileName));

			// read header
			line = in.readLine();
			result = line.split(" ");
			if (result.length != 1 || !result[0].toUpperCase().trim().equals("<MAP>"))
				throw new MapException("MapException: Error reading header of " + fileName);

			// read the maximum steps

			line = in.readLine();
			result = line.split(" ");
			if (result.length != 3 || !result[0].toUpperCase().trim().equals("<STEPMAX>") ||
					!result[2].toUpperCase().trim().equals("</STEPMAX>"))
				throw new MapException("MapException: Error reading maximum steps in " + fileName);
			try {
				stepMax = Integer.parseInt(result[1].toUpperCase().trim());
			} catch (NumberFormatException nfe) {
				throw new MapException("MapException: Error reading maximum steps in " + fileName);
			}

			// read the maximum time

			line = in.readLine();
			result = line.split(" ");
			if (result.length != 3 || !result[0].toUpperCase().trim().equals("<TIMEMAX>") ||
					!result[2].toUpperCase().trim().equals("</TIMEMAX>"))
				throw new MapException("MapException: Error reading maximum time in " + fileName);
			try {
				timeMax = Integer.parseInt(result[1].toUpperCase().trim());
			} catch (NumberFormatException nfe) {
				throw new MapException("MapException: Error reading maximum time in " + fileName);
			}


			// read the map
			for (int linecnt = 0; linecnt < MAPSIZE; ++linecnt) {
				// read the map line vector
				line = in.readLine();
				result = line.split(" ");
				if (result.length != 3 || 
						!result[0].toUpperCase().trim().equals("<MAPLINE>") ||
						!result[2].toUpperCase().trim().equals("</MAPLINE>") ||
						result[1].length() != MAPSIZE)
					throw new MapException("MapException: Error reading mapline " + linecnt + " in " + fileName);
				for (int i = 0; i < MAPSIZE; ++i) {
					char mapchar = result[1].charAt(i);
					switch (mapchar) {
					case '.':  map[i][linecnt] = MapElement.WATER; break;
					case '+':  map[i][linecnt] = MapElement.LAND; break;
					case 'S':  map[i][linecnt] = MapElement.START; 
					if (start != null)
						throw new MapException("MapException: Too many starting points in " + fileName);
					start = new Position(i, linecnt);
					break;
					case 'F':  map[i][linecnt] = MapElement.FINISH; 
					if (finish != null)
						throw new MapException("MapException: Too many finish points in " + fileName);
					finish = new Position(i, linecnt);
					break;
					default:
						throw new MapException("MapException: Erroneous character in mapline " + linecnt + " in " + fileName);
					}
				}
			}
			// read footer
			line = in.readLine();
			result = line.split(" ");
			if (result.length != 1 || !result[0].toUpperCase().trim().equals("</MAP>"))
				throw new MapException("MapException: Error reading footer of " + fileName);
			if (start == null)
				throw new MapException("MapException: No starting point found in " + fileName);
			if (finish == null)
				throw new MapException("MapException: No finish point found in " + fileName);
		} catch (IOException e) {
			throw new MapException("MapException: Error reading " + fileName);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {}
			} 
		}

	}

	public MapElement getAt(Position p) { 
		return map[p.getColumn()][p.getRow()];
	}

	public Position getStart() {		
		return start;
	}

	public Position getFinish() {		
		return finish;
	}

	public int getStepMax() {
		return stepMax;
	}

	public int getTimeMax() {
		return timeMax;
	}

}
