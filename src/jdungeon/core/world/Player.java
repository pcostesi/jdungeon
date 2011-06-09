package jdungeon.core.world;


// TODO: Auto-generated Javadoc
/**
 * The Class Player.
 */
public final class Player extends Vulnerable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5077508262843544636L;
	
	/** The experience. */
	private int experience;
	
	/** The name. */
	private String name;

	/**
	 * Instantiates a new player.
	 */
	public Player() {
		super(1, 5, 10);
		experience = 0;
	}
	
	/* Sets the world and cleans up the neighbouring ExplorerBonuses
	 * @see jdungeon.core.world.bonus.ExplorerBonus
	 * @see jdungeon.core.world.Entity#setWorld(jdungeon.core.world.World)
	 */
	public void setWorld(World w){
		super.setWorld(w);
		if (w != null){
			w.clearView(this);
		}
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name){
		this.name = name;
	}

	/**
	 * Gets the experience for next level.
	 *
	 * @return the experience for next level
	 */
	public int getExperienceForNextLevel() {
		return 5 * getLevel();
	}
	
	/* (non-Javadoc)
	 * @see jdungeon.core.world.Vulnerable#getExperience()
	 */
	public int getExperience() {
		return experience;
	}
	
	/**
	 * Move left.
	 */
	public void moveLeft() {
		move(-1, 0);
	}

	/**
	 * Move right.
	 */
	public void moveRight() {
		move(1, 0);
	}

	/**
	 * Move up.
	 */
	public void moveUp() {
		move(0, -1);
	}

	/**
	 * Move down.
	 */
	public void moveDown() {
		move(0, 1);
	}

	/**
	 * Move.
	 *
	 * @param x the x
	 * @param y the y
	 */
	private void move(int x, int y) {
		if (getHP() <= 0) {
			return;
		}

		World w = getWorld();
		if (w.moveDelta(this, x, y)) {
			w.notifyChange(this, 1);
		}
	}

	/* Awards experience to this player.
	 * @see jdungeon.core.world.Vulnerable#awardExperience(int)
	 */
	public void awardExperience(int exp) {
		experience = experience + exp;
		int level = getLevel();
		if (experience >= getExperienceForNextLevel()) {
			setLevel(++level);
			experience = 0;
			setMaxHealth(level * 10);
			setStrength(getRawStrength() + 5);
		}
	}

}
