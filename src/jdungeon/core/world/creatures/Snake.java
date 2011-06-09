package jdungeon.core.world.creatures;

/**
 * The Class Snake.
 */
public class Snake extends Monster {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1581806365731222834L;
	
	/** The Constant S. */
	private static final double S = 1;
	
	/** The Constant F. */
	private static final double F = 1;

	/**
	 * Instantiates a new snake.
	 *
	 * @param level the level
	 */
	public Snake(int level) {
		super(level, F, S);
	}

}
