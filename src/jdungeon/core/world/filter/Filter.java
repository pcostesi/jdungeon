package jdungeon.core.world.filter;

import jdungeon.core.world.Entity;
import jdungeon.core.world.Point;

// TODO: Auto-generated Javadoc
/**
 * The Interface Filter.
 */
public interface Filter {

	/**
	 * Apply.
	 *
	 * @param p the p
	 * @param e the e
	 * @return true, if successful
	 */
	public boolean apply(final Point p, final Entity e);
	
}
