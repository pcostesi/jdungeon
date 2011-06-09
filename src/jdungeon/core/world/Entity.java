package jdungeon.core.world;

import java.io.Serializable;

/**
 * The Class Entity.
 */
public abstract class Entity implements Serializable, Cloneable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8404560648461171917L;

	/** The world. */
	private World w;

	/**
	 * Sets the world.
	 * 
	 * @param w
	 *            the new world
	 */
	public void setWorld(World w) {
		this.w = w;
	}

	/**
	 * Gets the world.
	 * 
	 * @return the world
	 */
	public World getWorld() {
		return w;
	}

	/**
	 * This handler gets called when hit by an entity.
	 * 
	 * @param e
	 *            the entity
	 * @return true, if does not collide
	 */
	public boolean hitBy(Entity e) {
		return true;
	}

	/**
	 * This handler gets called when this object hits another entity.
	 * 
	 * @param e
	 *            the entity
	 * @return true, if does not collide
	 */
	public boolean hitsTo(Entity e) {
		return true;
	}

	/**
	 * This handler gets called when an entity is near this object.
	 * 
	 * @param e
	 *            the entity
	 */
	public void near(Entity e) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
