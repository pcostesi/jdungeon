package jdungeon.core.world.creatures;

import jdungeon.core.world.Vulnerable;

/**
 * The Class Monster.
 */
public abstract class Monster extends Vulnerable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8982164023200526814L;

	/**
	 * Instantiates a new monster.
	 * 
	 * @param level
	 *            the level
	 * @param f
	 *            the f
	 * @param s
	 *            the s
	 */
	public Monster(int level, double f, double s) {
		super(level, computeStrength(f, level), computeHealth(s, level));
	}

	/**
	 * Compute health.
	 * 
	 * @param s
	 *            the s
	 * @param level
	 *            the level
	 * @return the int
	 */
	private static int computeHealth(double s, int level) {
		double base = Math.pow(level + 3, 2) - 10;
		return (int) Math.floor(base * s);
	}

	/**
	 * Compute strength.
	 * 
	 * @param f
	 *            the f
	 * @param level
	 *            the level
	 * @return the int
	 */
	private static int computeStrength(double f, int level) {
		double base = (level + 5) * level * 0.5;
		return (int) Math.floor(base * f);
	}

	/**
	 * Monsters have no experience. This does nothing.
	 * 
	 * @see jdungeon.core.world.Vulnerable#awardExperience(int)
	 */
	public void awardExperience(int exp) {
	}

	/**
	 * Monsters have no experience.
	 * 
	 * @return 0
	 * @see jdungeon.core.world.Vulnerable#getExperience()
	 */
	public int getExperience() {
		return 0;
	}

	/**
	 * This method awards experience to a vulnerable when killed.
	 * 
	 * @see jdungeon.core.world.Vulnerable#hurtBy(jdungeon.core.world.Vulnerable)
	 */
	public int hurtBy(Vulnerable v) {
		int life = super.hurtBy(v);
		if (life == 0) {
			v.awardExperience(this.getLevel());
		}
		return life;

	}
}
