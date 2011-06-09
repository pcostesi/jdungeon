package jdungeon.core.world.bonus;

import jdungeon.core.world.Player;
import jdungeon.core.world.Vulnerable;

/**
 * The Class ExplorerBonus.
 */
public class ExplorerBonus extends Bonus {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4035754717671401034L;

	/**
	 * Instantiates a new explorer bonus.
	 */
	public ExplorerBonus() {
		super(1);
	}

	/**
	 * Heals all hurt entities in the world.
	 * 
	 * @see jdungeon.core.world.bonus.Bonus#areaBuff(jdungeon.core.world.Player)
	 */
	@Override
	protected void areaBuff(Player p) {
		for (Vulnerable v : getWorld().getHurtEntities()) {
			v.heal(v.getLevel());
		}
		getWorld().remove(this);
	}

	/**
	 * Overrides the default behaviour to become useless.
	 * 
	 * @see jdungeon.core.world.bonus.Bonus#buff(jdungeon.core.world.Player)
	 */
	@Override
	protected void buff(Player p) {
	}

}
