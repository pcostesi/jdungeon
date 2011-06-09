package jdungeon.parser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import jdungeon.core.world.Entity;
import jdungeon.core.world.Point;
import jdungeon.core.world.World;

public abstract class WorldAssembler {

	static private World newWorld;
	static private HashMap<String, File> gameWorldsList;
	static private Set<Point> occupiedPoints;

	/**
	 * Wrapper function for worldCreator, uses the input String to obtain the
	 * File to call worldCreator, from gameWorldList Must have invoked:
	 * getWorldList()
	 * 
	 * @throws CorruptFileException
	 *             if the file is invalid
	 * @throws FileNotFoundException
	 *             if an IOError occurs
	 * @see jdungeon.parser.getWorldList
	 */
	// Makes testing easier
	static public World createNewWorld(String gameWorldName)
			throws FileNotFoundException, CorruptFileException {
		File gameWorldFile = gameWorldsList.get(gameWorldName);
		return worldCreator(gameWorldFile);
	}

	/**
	 * Parses the map file, creating all the map entities, the map itself and
	 * validating the data received.
	 * 
	 * @param gameWorldFile
	 *            a String containing the World name.
	 * 
	 * @throws CorruptFileException
	 *             if the file is invalid
	 * @throws FileNotFoundException
	 *             if an IOError occurs
	 * 
	 * @see jdungeon.parser.getWorldList
	 */

	static public World worldCreator(File gameWorldFile)
			throws FileNotFoundException, CorruptFileException {
		occupiedPoints = new HashSet<Point>();
		GameReader.newGameReader(gameWorldFile);
		int gameData[] = GameReader.getGameData(2, ",");
		if (gameData == null) {
			throw new CorruptFileException();
		}
		String worldName = GameReader.getGameData();
		if (worldName == null) {
			throw new CorruptFileException();
		}
		newWorld = new World(worldName, gameData[0], gameData[1]);
		Entity newEntity;
		Point entityPosition;
		gameData = GameReader.getGameData(6, ",");
		while (gameData != null) {
			newEntity = GameFactory.createEntity(gameData);
			entityPosition = GameFactory.getEntityPosition(gameData);
			newWorld.add(newEntity, GameFactory.getEntityPosition(gameData));
			if (occupiedPoints.contains(entityPosition)) {
				throw new CorruptFileException();
			}
			occupiedPoints.add(entityPosition);
			newEntity.setWorld(newWorld);
			gameData = GameReader.getGameData(6, ",");
		}
		reset();
		return newWorld;
	}

	static private void reset() {
		GameFactory.reset();
		occupiedPoints = new HashSet<Point>();
		GameReader.close();
	}

	/**
	 * Wrapper function for worldList, gives worldList the directory from where
	 * it has to obtain the worlds name.
	 * 
	 * @return Set<String>
	 * @throws NoMapsInFolderException
	 *             if there are no maps or no valid maps in the resources folder
	 */
	// makes testing worldList easier
	static public Set<String> getWorldList() throws NoMapsInFolderException {
		File worldDirectory = new File("boards");
		return worldList(worldDirectory);
	}

	/**
	 * Parses all .board files in the resources folder, obtaining the Worlds
	 * names.It stores a link between the file and the World name returns a
	 * Set<Strings> with the names of the Worlds. If there are 2 maps with the
	 * same name, it will only register the first one
	 * 
	 * @return Set<String>
	 * 
	 * @throws NoMapsInFolderException
	 *             if there are no maps or no valid maps in the resources folder
	 */
	static public Set<String> worldList(File worldDir)
			throws NoMapsInFolderException {
		if (gameWorldsList == null) {
			File worldFiles[] = worldDir.listFiles(new WorldFileFilter());
			gameWorldsList = new HashMap<String, File>();
			for (File worldFile : worldFiles) {
				try {
					if (worldFile.isDirectory() == false) {
						GameReader.newGameReader(worldFile);
						GameReader.getGameData();
						String worldName = GameReader.getGameData();
						gameWorldsList.put(worldName, worldFile);
					}
				} catch (FileNotFoundException e) {
				} catch (CorruptFileException e) {
					e.printStackTrace();
				}
			}
		}
		if (gameWorldsList.isEmpty()) {
			throw new NoMapsInFolderException();
		}
		return gameWorldsList.keySet();
	}

	static class WorldFileFilter implements FilenameFilter {
		public boolean accept(File file, String name) {
			return (name.endsWith(".board"));
		}
	}

	static public void saveGame(String name, File directory, World gameWorld)
			throws FailureException {
		ObjectOutputStream save = null;
		try {
			File saveFile = new File(directory.getAbsolutePath() + File.separator + name
					+ ".wtf");
			if (saveFile.isFile()) {
				saveFile.delete();
			}
			save = new ObjectOutputStream(new BufferedOutputStream(
					new FileOutputStream(saveFile)));
			save.writeObject(gameWorld);
			save.close();
		} catch (IOException e) {
			e.printStackTrace();
			try {
				save.close();
			} catch (IOException e1) {
			}
			throw new FailureException();
		}
	}

	static public World loadGame(File filePath) throws FailureException {
		World gameWorld;
		try {
			ObjectInputStream file = new ObjectInputStream(
					new BufferedInputStream(new FileInputStream(filePath)));
			gameWorld = (World) file.readObject();
			file.close();
		} catch (Exception e) {
			throw new FailureException();
		}

		return gameWorld;

	}
}
