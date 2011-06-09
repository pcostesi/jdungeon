package jdungeon.core.world.scenery;

import jdungeon.core.world.Entity;

/**
 * The Class Splat.
 */
public class Splat extends Entity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3264325159080704221L;
	
	/** The singleton. */
	private static Splat singleton;

	/**
	 * Instantiates a new splat.
	 */
	private Splat() {

	}

	/**
	 * Gets the single instance of Splat.
	 *
	 * @return single instance of Splat
	 */
	public static Splat getInstance() {
		if (singleton == null) {
			singleton = new Splat();
		}
		return singleton;
	}

}
