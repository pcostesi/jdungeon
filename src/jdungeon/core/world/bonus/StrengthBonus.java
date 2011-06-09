package jdungeon.core.world.bonus;

import jdungeon.core.world.Player;

/**
 * The Class StrengthBonus.
 */
public class StrengthBonus extends Bonus {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1806332638633143325L;

	/**
	 * Instantiates a new strength bonus.
	 * 
	 * @param level
	 *            the level
	 */
	public StrengthBonus(int level) {
		super(level);
	}

	/*
	 * Does nothing for neighbouring players.
	 * 
	 * @see jdungeon.core.world.bonus.Bonus#areaBuff(jdungeon.core.world.Player)
	 */
	@Override
	protected void areaBuff(Player p) {
	}

	/**
	 * Grants an extra bonus that builds up the strength of a player.
	 * 
	 * @see jdungeon.core.world.bonus.Bonus#buff(jdungeon.core.world.Player)
	 */
	@Override
	protected void buff(Player p) {
		p.setStrengthBonus(getLevel() + p.getStrengthBonus());
		getWorld().remove(this);
	}

}
