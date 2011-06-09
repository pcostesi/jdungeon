package jdungeon.core.world.scenery;

import jdungeon.core.world.Entity;

/**
 * The Class Wall.
 */
public class Wall extends Entity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -799314292856163866L;

	/** The singleton. */
	private static Wall singleton;

	/**
	 * Instantiates a new wall.
	 */
	private Wall() {

	}

	/**
	 * Gets the single instance of Wall.
	 * 
	 * @return single instance of Wall
	 */
	public static Wall getInstance() {
		if (singleton == null) {
			singleton = new Wall();
		}
		return singleton;
	}

	/**
	 * Turns the wall into a collidable object
	 * 
	 * @see jdungeon.core.world.Entity#hitBy(jdungeon.core.world.Entity)
	 */
	@Override
	public boolean hitBy(Entity e) {
		return false;
	}

}
