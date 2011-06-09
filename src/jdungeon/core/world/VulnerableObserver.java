package jdungeon.core.world;

import java.io.Serializable;

/**
 * An asynchronous update interface for receiving notifications about Vulnerable
 * information as the Vulnerable is constructed.
 */
interface VulnerableObserver extends Serializable {

	/**
	 * This method is called when the Vulnerable ceases to be, well, alive.
	 * After this event is signaled, calling the entity becomes unsafe.
	 */
	public void onDeath();

}
