package jdungeon.core.world.bonus;

import jdungeon.core.world.Player;

// TODO: Auto-generated Javadoc
/**
 * The Class HealthBonus.
 */
public class HealthBonus extends Bonus {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5122026020706813821L;

	/**
	 * Instantiates a new health bonus.
	 * 
	 * @param hp
	 *            the hp
	 */
	public HealthBonus(int hp) {
		super(hp);
	}

	/**
	 * Does nothing.
	 * 
	 * @see jdungeon.core.world.bonus.Bonus#areaBuff(jdungeon.core.world.Player)
	 */
	@Override
	protected void areaBuff(Player p) {
	}

	/*
	 * Heals and removes itself from the world
	 * 
	 * @see jdungeon.core.world.bonus.Bonus#buff(jdungeon.core.world.Player)
	 */
	@Override
	protected void buff(Player p) {
		p.heal(getLevel());
		getWorld().remove(this);
	}

}
