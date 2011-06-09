package jdungeon.core.world.creatures;

/**
 * The Class Dragon.
 */
public class Dragon extends Monster {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1880618701656135171L;
	
	/** The Constant F. */
	private final static double F = 1;
	
	/** The Constant S. */
	private final static double S = 1.35;

	/**
	 * Instantiates a new dragon.
	 *
	 * @param level the level
	 */
	public Dragon(int level) {
		super(level, F, S);
	}
}
