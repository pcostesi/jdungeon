package jdungeon.core.world;

import java.util.HashSet;
import java.util.Set;

import jdungeon.core.world.scenery.Splat;
import jdungeon.exception.InvalidDeltaException;

/**
 * The Class Vulnerable.
 */
public abstract class Vulnerable extends Entity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1316649287162060617L;

	/** The HP. */
	private int HP;

	/** The max hp. */
	private int maxHP;

	/** The level. */
	private int level;

	/** The strength. */
	private int strength;

	/** The strength bonus. */
	private int strengthBonus;

	/** The observers. */
	private Set<VulnerableObserver> observers;

	/**
	 * Instantiates a new vulnerable.
	 * 
	 * @param level
	 *            the level
	 * @param strength
	 *            the strength
	 * @param health
	 *            the health
	 */
	public Vulnerable(int level, int strength, int health) {
		this.level = level;
		this.strength = strength;
		this.maxHP = health;
		this.HP = this.maxHP;
		this.strengthBonus = 0;
		observers = new HashSet<VulnerableObserver>();
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
	 * Gets the HP.
	 * 
	 * @return the HP
	 */
	public int getHP() {
		return HP;
	}

	/**
	 * Dispatches a hit to a Vulnerable entity e.
	 * 
	 * @return false if both are Vulnerable.
	 * 
	 * @see jdungeon.core.world.Entity#hitBy(jdungeon.core.world.Entity)
	 */
	@Override
	public boolean hitBy(Entity e) {
		if (e instanceof Vulnerable) {
			((Vulnerable) e).hurtBy(this);
		}
		return false;
	}

	/**
	 * Dispatches the damage to this entity.
	 * 
	 * @return false if this entity is alive and the hitting entity is an
	 *         instance of Vulnerable
	 * 
	 * @see jdungeon.core.world.Entity#hitsTo(jdungeon.core.world.Entity)
	 */
	@Override
	public boolean hitsTo(Entity e) {
		int life = getHP();
		if (life > 0 && e instanceof Vulnerable) {
			life = ((Vulnerable) e).hurtBy(this);
			return false;
		}
		return true;
	}

	/**
	 * Heal this entity.
	 * 
	 * @param healthDelta
	 *            the health delta
	 * @return the new health
	 */
	public int heal(int healthDelta) {
		if (healthDelta <= 0) {
			throw new InvalidDeltaException();
		}
		HP += healthDelta;
		if (HP >= maxHP) {
			HP = maxHP;
			getWorld().removeHurtEntity(this);
		}
		return getHP();
	}
	
	/**
	 * Get hurt by a Vulnerable.
	 * 
	 * @param v
	 *            the vulnerable inflicting the damage
	 * @return the new health
	 */
	public int hurtBy(Vulnerable v) {
		int healthDelta = v.getStrength();
		if (healthDelta < 0) {
			throw new InvalidDeltaException();
		}

		HP -= healthDelta;

		if (HP <= 0) {
			getWorld().removeHurtEntity(this);
			HP = 0;
			this.die();
		} else if (HP >= maxHP) {
			HP = maxHP;
			getWorld().removeHurtEntity(this);
		} else {
			getWorld().addHurtEntity(this);
		}
		return getHP();
	}

	/**
	 * Gets the strength (bonus + raw).
	 * 
	 * @return the strength
	 */
	public int getStrength() {
		return strength + getStrengthBonus();
	}

	/**
	 * Gets the raw strength.
	 * 
	 * @return the raw strength
	 */
	public int getRawStrength() {
		return strength;
	}

	/**
	 * Gets the strength bonus.
	 * 
	 * @return the strength bonus
	 */
	public int getStrengthBonus() {
		return strengthBonus;
	}

	/**
	 * Sets the strength.
	 * 
	 * @param delta
	 *            the delta
	 * @return the strength
	 */
	public int setStrength(int delta) {
		strength = delta;
		return strength;
	}

	/**
	 * Sets the strength bonus.
	 * 
	 * @param i
	 *            the strength bonus points
	 * @return the total bonus points
	 */
	public int setStrengthBonus(int i) {
		return strengthBonus = i;
	}

	/**
	 * Sets the max health.
	 * 
	 * @param h
	 *            the max health
	 * @return the current health
	 */
	public int setMaxHealth(int h) {
		maxHP = h;
		return getHP();
	}

	/**
	 * Gets the max health.
	 * 
	 * @return the max health
	 */
	public int getMaxHealth() {
		return maxHP;
	}

	/**
	 * Sets the level.
	 * 
	 * @param level
	 *            the level
	 * @return the level
	 */
	public int setLevel(int level) {
		this.level = level > 3 ? 3 : level;
		return level;
	}

	/**
	 * Award experience.
	 * 
	 * @param exp
	 *            the experience
	 */
	public abstract void awardExperience(int exp);

	/**
	 * Gets the experience.
	 * 
	 * @return the experience
	 */
	public abstract int getExperience();

	/**
	 * Subscribe to events, such as death.
	 * 
	 * @param observer
	 *            the observer
	 */
	public void subscribe(VulnerableObserver observer) {
		observers.add(observer);
	}

	/**
	 * Unsubscribe to events.
	 * 
	 * @param observer
	 *            the observer
	 */
	public void unsubscribe(VulnerableObserver observer) {
		observers.remove(observer);
	}

	/**
	 * Die. 
	 * Like when real people die. 
	 * How sad. 
	 * That pretty much sums it up.
	 */
	protected void die() {
		World world = getWorld();
		world.replace(this, Splat.getInstance());
		for (VulnerableObserver observer : observers) {
			observer.onDeath();
		}
	}
}
