package jdungeon.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

abstract public class GameReader {
	static BufferedReader game;

	/**
	 * Creates a BufferedReader with the input File and it stores it for further
	 * use
	 * 
	 * @throws FileNotFoundException
	 */
	static public void newGameReader(File gameFile)
			throws FileNotFoundException {
		game = new BufferedReader(new FileReader(gameFile));
	}

	/**
	 * Parses a valid line, splits it by "pattern" and checks that the amount of
	 * pieces "splited" equals dataSize. returns null if the BufferedReader is
	 * empty
	 * 
	 * @return int[] sized dataSize
	 * @throws CorruptFileException
	 *             if an error while parsing the numbers occurs, or if an
	 *             IOException occurs
	 */
	static public int[] getGameData(int dataSize, String pattern)
			throws CorruptFileException {
		String line;
		line = getGameData();
		if (line == null) {
			GameReader.close();
			return null;
		}
		String data[] = line.split(pattern);
		if (data.length != dataSize) {
			GameReader.close();
			throw new CorruptFileException();
		}
		int finalData[] = new int[dataSize];
		try {
			for (int i = 0; i < data.length; i++) {
				if (i == data.length - 1) {
					finalData[i] = Integer.parseInt(data[i].split("#")[0]
							.trim());
				} else
					finalData[i] = Integer.parseInt(data[i].trim());
			}
		} catch (NumberFormatException e) {
			GameReader.close();
			throw new CorruptFileException();
		}
		return finalData;

	}

	/**
	 * Parses the BufferedReader until it finds a valid line (non blank nor
	 * commented line), returning it as a String If the Reader ends before
	 * finding a valid line it closes the Reader and returns null.
	 * 
	 * @return String
	 * @throws CorruptFileException
	 *             if an IOError occurs
	 */
	static public String getGameData() throws CorruptFileException {
		String line;
		try {
			do {
				line = game.readLine();
				if (line == null) {
					GameReader.close();
					return null;
				}
				line = line.trim();
			} while ((line.length() == 0 || line.charAt(0) == '#' || line
					.equals("")));
		} catch (IOException e) {
			GameReader.close();
			throw new CorruptFileException();
		}
		return line;
	}

	/**
	 * Closes the BufferedReader
	 */
	static public void close() {
		try {
			game.close();
		} catch (IOException e) {
		}
	}
}
