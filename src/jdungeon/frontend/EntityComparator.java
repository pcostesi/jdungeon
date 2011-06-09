package jdungeon.frontend;

import java.util.Comparator;

import jdungeon.core.world.Entity;
import jdungeon.core.world.Vulnerable;

public class EntityComparator implements Comparator<Entity> {

	@Override
	public int compare(Entity elem1, Entity elem2) {
		if (elem1 instanceof Vulnerable) {
			return 1;
		}
		return -1;
	}

}
