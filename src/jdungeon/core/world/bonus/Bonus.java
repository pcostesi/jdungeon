package jdungeon.core.world.bonus;

import jdungeon.core.world.Entity;
import jdungeon.core.world.Player;

// TODO: Auto-generated Javadoc
/**
 * The Class Bonus.
 */
public abstract class Bonus extends Entity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5185613195995567126L;

	/** The level. */
	private int level;

	/**
	 * Instantiates a new bonus.
	 * 
	 * @param level
	 *            the level
	 */
	public Bonus(int level) {
		this.level = level;
	}

	/**
	 * Buffs the Player
	 * 
	 * @see jdungeon.core.world.Entity#hitBy(jdungeon.core.world.Entity)
	 */
	@Override
	public boolean hitBy(Entity e) {
		if (e instanceof Player) {
			this.buff((Player) e);
		}
		return true;
	}

	/**
	 * Buffs the player near this bonus
	 * 
	 * @see jdungeon.core.world.Entity#near(jdungeon.core.world.Entity)
	 */
	@Override
	public void near(Entity e) {
		if (e instanceof Player) {
			this.areaBuff((Player) e);
		}
	}

	/**
	 * Gets the level.
	 * 
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Buff (when a player steps on this bonus).
	 * 
	 * @param p
	 *            the point
	 */
	protected abstract void buff(Player p);

	/**
	 * Area buff (called when a player gets near).
	 * 
	 * @param p
	 *            the point
	 */
	protected abstract void areaBuff(Player p);

}
