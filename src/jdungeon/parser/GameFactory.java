package jdungeon.parser;

import jdungeon.core.world.Entity;
import jdungeon.core.world.Player;
import jdungeon.core.world.Point;
import jdungeon.core.world.bonus.HealthBonus;
import jdungeon.core.world.bonus.StrengthBonus;
import jdungeon.core.world.creatures.*;
import jdungeon.core.world.scenery.Wall;
import Constants.ObjectType;

public abstract class GameFactory {

	static private boolean playerAppeared = false;
	static private boolean finalBossAppeared = false;

	/**
	 * Verifies that the input vector contains valid data, and instantiates the
	 * corresponding entity to that data.
	 * 
	 * @throws CorruptFileException
	 *             if the data is invalid
	 */
	static public Entity createEntity(int gameData[])
			throws CorruptFileException {
		Entity newEntity = null;
		ObjectType o = ObjectType.objFromInt(gameData[0]);
		switch (o) {
		case PLAYER:
			newEntity = parsePlayer(gameData);
			break;
		case WALL:
			newEntity = parseWall(gameData);
			break;
		case ENEMY:
			newEntity = parseEnemy(gameData);
			break;
		case HEALTHBONUS:
			newEntity = parseHealthBonus(gameData);
			break;
		case STRENGTHBONUS:
			newEntity = parseStrengthBonuse(gameData);
			break;
		default:
			newEntity = null;
		}
		if (newEntity == null) {
			throw new CorruptFileException();
		} else {
			return newEntity;
		}

	}

	static public Point getEntityPosition(int gameData[])
			throws CorruptFileException {
		return new Point(gameData[2], gameData[1]);
	}

	static public void reset() {
		playerAppeared = false;
		finalBossAppeared = false;
	}

	/**
	 * Validates the gameData, checking it's accord for a player, 
	 * and checks that there are no more than 2 players created.
	 * Returns a new Player if the data is valid
	 */
	static private Entity parsePlayer(int gameData[]) {
		if (playerAppeared || gameData[3] != 0 || gameData[4] != 0
				|| gameData[5] != 0) {
			return null;
		}
		return new Player();
	}

	static private Entity parseWall(int gameData[]) {
		if (gameData[3] != 0 || gameData[4] != 0 || gameData[5] != 0) {
			return null;
		}
		return Wall.getInstance();
	}

	static private Entity parseEnemy(int gameData[]) {
		if (gameData[5] != 0 || (gameData[4] == 3 && finalBossAppeared)
				|| gameData[4] > 3//
				|| gameData[4] < 1) {
			return null;
		}
		if (gameData[4] == 3) {
			finalBossAppeared = true;
		}
		switch (gameData[3]) {
		case 1:
			return new Golem(gameData[4]);
		case 2:
			return new Dragon(gameData[4]);
		case 3:
			return new Snake(gameData[4]);
		default:
			return null;
		}
	}

	static private Entity parseHealthBonus(int gameData[]) {
		if (gameData[3] != 0 || gameData[4] != 0 || gameData[5] <= 0) {
			return null;
		}
		return new HealthBonus(gameData[5]);
	}

	static private Entity parseStrengthBonuse(int gameData[]) {
		if (gameData[3] != 0 || gameData[4] != 0 || gameData[5] <= 0) {
			return null;
		}
		return new StrengthBonus(gameData[5]);
	}

}
