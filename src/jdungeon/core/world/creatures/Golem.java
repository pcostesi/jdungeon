package jdungeon.core.world.creatures;

/**
 * The Class Golem.
 */
public class Golem extends Monster {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1268891956515692713L;
	
	/** The Constant S. */
	private static final double S = 1;
	
	/** The Constant F. */
	private static final double F = 0.7;

	/**
	 * Instantiates a new golem.
	 *
	 * @param level the level
	 */
	public Golem(int level) {
		super(level, F, S);
	}

}
