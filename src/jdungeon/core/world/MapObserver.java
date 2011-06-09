package jdungeon.core.world;

/**
 * An asynchronous update interface for receiving notifications about Map
 * information as the Map is constructed.
 */
public interface MapObserver {

	/**
	 * This method is called when entity movement or update requests are
	 * informed by World
	 * 
	 * @param p
	 *            the point
	 * @param entities
	 *            the entities
	 */
	public void onMapChange(final Point p, final Entity[] entities);

	/**
	 * This method is called when the main Player dies.
	 */
	public void onPlayerDeath();

	/**
	 * This method is called when the main antagonist dies.
	 */
	public void onBossDeath();
}
