package jdungeon.core.world.filter;

import jdungeon.core.world.Entity;
import jdungeon.core.world.Point;

// TODO: Auto-generated Javadoc
/**
 * The Class Entry.
 */
public class Entry {

	/** The p. */
	private Point p;
	
	/** The entity. */
	private Entity entity;

	/**
	 * Instantiates a new entry.
	 *
	 * @param p the p
	 * @param entity the entity
	 */
	public Entry(Point p, Entity entity) {
		this.p = p;
		this.entity = entity;
	}

	/**
	 * Instantiates a new entry.
	 *
	 * @param p the p
	 */
	public Entry(Point p) {
		this.p = p;
	}

	/**
	 * Gets the point.
	 *
	 * @return the point
	 */
	public Point getPoint() {
		return (Point) p.clone();
	}

	/**
	 * Gets the entity.
	 *
	 * @return the entity
	 */
	public Entity getEntity() {
		return entity;
	}
	
	/**
	 * Gets the entity.
	 *
	 * @param <T> the generic type
	 * @param t the t
	 * @return the entity
	 */
	@SuppressWarnings("unchecked")
	public <T extends Entity> T getEntity(Class<T> t) {
		if (!t.isInstance(entity)){
			throw new RuntimeException();
		}
		return (T) entity;
	}
}
