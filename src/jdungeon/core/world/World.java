package jdungeon.core.world;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jdungeon.core.world.bonus.Bonus;
import jdungeon.core.world.bonus.ExplorerBonus;
import jdungeon.core.world.creatures.Monster;
import jdungeon.core.world.filter.BoardFilter;
import jdungeon.core.world.filter.Entry;
import jdungeon.core.world.filter.Filter;

/**
 * The Class World.
 */
public final class World implements Serializable, Cloneable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -829410250059029974L;

	/** The board. */
	private Board board;

	/** The player. */
	private Player player;

	/** The boss. */
	private Monster boss;

	/** The observers. */
	private transient Set<MapObserver> observers = new HashSet<MapObserver>();

	/** The hurt entities. */
	private Set<Vulnerable> hurtEntities = new HashSet<Vulnerable>();

	/** The observer for the player. */
	private VulnerableObserver playerObserver;
	
	/** The observer for the boos. */
	private VulnerableObserver bossObserver;

	/**
	 * Instantiates a new world.
	 * 
	 * @param name
	 *            the name
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public World(String name, int x, int y) {
		board = new Board(name, x, y);

		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				ExplorerBonus bonus = new ExplorerBonus();
				bonus.setWorld(this);
				board.add(bonus, i, j);
			}
		}
		hurtEntities = new HashSet<Vulnerable>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone() throws CloneNotSupportedException {
		World w = (World) super.clone();

		w.board = new Board(board.getName(), board.getSizeX(), board.getSizeY());
		for (Entry e : board) {
			Entity entity = e.getEntity();
			Point point = e.getPoint();
			if (entity != null && point != null) {
				entity = (Entity) entity.clone();
				point = (Point) point.clone();
				w.board.add(entity, point);
				entity.setWorld(w);
			}
		}

		BoardFilter filter = board.filter();
		filter.where(Vulnerable.class).filter(new Filter() {
			public boolean apply(Point p, Entity e) {
				Vulnerable v = (Vulnerable) e;
				return (e != null && v.getHP() > 0 && v.getHP() < v
						.getMaxHealth());
			}
		});
		w.hurtEntities = new HashSet<Vulnerable>();
		Collections.addAll(w.hurtEntities, filter.toArray(new Vulnerable[0]));

		return w;
	}

	/**
	 * Adds the hurt entity.
	 * 
	 * @param v
	 *            the v
	 */
	void addHurtEntity(Vulnerable v) {
		hurtEntities.add(v);
	}

	/**
	 * Removes the hurt entity.
	 * 
	 * @param v
	 *            the v
	 */
	void removeHurtEntity(Vulnerable v) {
		hurtEntities.remove(v);
	}

	/**
	 * Gets the hurt entities.
	 * 
	 * @return the hurt entities
	 */
	public Vulnerable[] getHurtEntities() {
		return hurtEntities.toArray(new Vulnerable[0]);
	}

	/**
	 * Subscribe to map events.
	 * 
	 * @param observer
	 *            the observer
	 */

	public void subscribe(MapObserver observer) {
		if (observers == null) {
			observers = new HashSet<MapObserver>();
		}
		observers.add(observer);
	}

	/**
	 * Unsubscribe to map events.
	 * 
	 * @param observer
	 *            the observer
	 */
	public void unsubscribe(VulnerableObserver observer) {
		if (observers == null) {
			observers = new HashSet<MapObserver>();
		}
		observers.remove(observer);
	}

	/**
	 * Adds the entity to the map at the given coordinate pair.
	 * 
	 * @param e
	 *            the e
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void add(Entity e, int x, int y) {
		this.add(e, new Point(x, y));
	}

	/**
	 * Adds the entities to the map at the given coordinate pair.
	 * 
	 * @param entities
	 *            the entities
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void add(Entity entities[], int x, int y) {
		for (Entity e : entities) {
			this.add(e, new Point(x, y));
		}
	}

	/**
	 * Adds the entity to the map at the given point and binds the world
	 * 
	 * @param e
	 *            the entity
	 * @param p
	 *            the point
	 */
	public void add(Entity e, Point p) {
		board.add(e, p);
		e.setWorld(this);
		notifyChange(p);
	}

	/**
	 * Adds the entities
	 * 
	 * @param e
	 *            the entities
	 * @param p
	 *            the point
	 */
	public void add(Entity e[], Point p) {
		for (Entity entity : e) {
			board.add(entity, p);
		}
		notifyChange(p);
	}

	/**
	 * Move an entity to a new point.
	 * 
	 * @param e
	 *            the entity
	 * @param to
	 *            the new point
	 * @return true, if the entity moved
	 */
	protected boolean move(Entity e, Point to) {
		boolean moved;
		Point now = board.getEntityPos(e);

		moved = board.move(e, (Point) to.clone());
		notifyChange(now);
		notifyChange(to);

		return moved;
	}

	/**
	 * Move an entity by a given delta.
	 * 
	 * @param e
	 *            the entity
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return true, if the entity moved
	 */
	protected boolean moveDelta(Entity e, int x, int y) {
		boolean moved;
		Point now = board.getEntityPos(e);
		Point to = (Point) now.clone();
		to.translate(x, y);
		moved = board.move(e, to);
		notifyChange(now);
		notifyChange(to);

		return moved;
	}

	/**
	 * Removes the entity.
	 * 
	 * @param e
	 *            the entity
	 */
	public void remove(Entity e) {
		Point p = board.getEntityPos(e);
		if (e.equals(getPlayer())) {
			((Vulnerable) e).unsubscribe(playerObserver);
		} else if (e.equals(getBoss())) {
			((Vulnerable) e).unsubscribe(bossObserver);
		}
		e.setWorld(null);
		board.remove(e);
		notifyChange(p);
	}

	/**
	 * Removes the entities from the map.
	 * 
	 * @param e
	 *            the entities
	 */
	public void remove(Entity e[]) {
		for (Entity entity : e) {
			this.remove(entity);
		}

	}

	/**
	 * Clear view within radius 1 for the entity e.
	 * 
	 * @param e
	 *            the entity
	 */
	protected void clearView(Entity e) {
		Point p = board.getEntityPos(e);
		Point[] area = area(p, 1);
		BoardFilter filter = board.filter();
		filter.where(ExplorerBonus.class).in(area);
		for (Entry entry : filter) {
			remove(entry.getEntity());
		}
	}

	/**
	 * Gets the player.
	 * 
	 * @return the player
	 */
	public Player getPlayer() {
		if (player == null) {
			Entry e;
			BoardFilter filter = board.filter();
			filter.where(Player.class);
			e = filter.get();
			player = (Player) e.getEntity();
			if (e == null){
				return player;
			}
			player.subscribe(new VulnerableObserver() {
				private static final long serialVersionUID = -1202243787619556443L;

				public void onDeath() {
					for (MapObserver observer : observers) {
						observer.onPlayerDeath();
					}
				}
			});
		}
		return player;
	}

	/**
	 * Gets the boss.
	 * 
	 * @return the boss
	 */
	public Monster getBoss() {
		if (boss == null) {
			Entry e;
			BoardFilter filter = board.filter();
			filter.where(Monster.class).filter(new Filter() {
				public boolean apply(Point p, Entity e) {
					return ((Monster) e).getLevel() == 3;
				}
			});
			e = filter.get();
			if (e == null){
				return boss;
			}
			boss = (Monster) e.getEntity();
			
			boss.subscribe(new VulnerableObserver() {
				private static final long serialVersionUID = 4825200888973408861L;

				public void onDeath() {
					for (MapObserver observer : observers) {
						observer.onBossDeath();
					}
				}
			});
		}
		return boss;
	}

	/**
	 * This sync event is responsible of refreshing the state of a map.
	 */
	public void syncEvent() {
		BoardFilter filter = board.filter();
		filter.noPointsContaining(ExplorerBonus.class);
		for (Entry e : filter) {
			notifyChange(e.getPoint());
		}
	}

	/**
	 * Replace an entity with another.
	 * 
	 * @param oldEntity
	 *            the old entity
	 * @param newEntity
	 *            the new entity
	 */
	public void replace(Entity oldEntity, Entity newEntity) {
		Point p = board.getEntityPos(oldEntity);
		board.replace(oldEntity, newEntity);
		notifyChange(p);
	}

	/**
	 * Raise a map update event for the point p to each observer.
	 * 
	 * @param p
	 *            the point
	 */
	void notifyChange(Point p) {
		Set<Entity> entities = board.in(p);
		for (MapObserver observer : observers) {
			observer.onMapChange(p, entities.toArray(new Entity[0]));
		}
	}

	/**
	 * Helper function. Gets an area defined by the point and radius.
	 * 
	 * @param p
	 *            the point
	 * @param radius
	 *            the radius
	 * @return the points in the area
	 */
	private Point[] area(Point p, int radius) {
		int px = (int) p.getX();
		int py = (int) p.getY();
		int diameter = (2 * radius + 1);
		Point[] result = new Point[diameter * diameter];
		int iter = 0;

		for (int y = py - radius; y <= py + radius; y++) {
			for (int x = px - radius; x <= px + radius; x++) {
				result[iter++] = new Point(x, y);
			}
		}
		return result;
	}

	/**
	 * Raise a map notification for all the points in the area defined by the
	 * point and radius.
	 * 
	 * @param p
	 *            the p
	 * @param radius
	 *            the radius
	 */
	void notifyChange(Point p, int radius) {
		for (Point point : area(p, radius)) {
			notifyChange(point);
		}
	}

	/**
	 * Notify a map change where the Entity e is located to listeners.
	 * 
	 * @param e
	 *            the entity
	 */
	void notifyChange(Entity e) {
		Point p = board.getEntityPos(e);
		notifyChange(p);
	}

	/**
	 * Notify map status within a radius to listeners.
	 * 
	 * @param e
	 *            the entity
	 * @param radius
	 *            the radius of notification
	 */
	void notifyChange(Entity e, int radius) {
		Point p = board.getEntityPos(e);
		notifyChange(p, radius);
	}

	/**
	 * Gets the entity in point.
	 * 
	 * @param <T>
	 *            the generic type extending Entity
	 * @param p
	 *            the point
	 * @param t
	 *            the array of type T
	 * @return the entity in point
	 */
	public <T extends Entity> T[] getEntityInPoint(Point p, T[] t) {
		BoardFilter filter = board.filter();
		filter.noPointsContaining(Bonus.class).in(p);
		return filter.toArray(t);
	}

}
