package jdungeon.core.world;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import jdungeon.core.world.filter.BoardFilter;
import jdungeon.core.world.filter.Entry;

/**
 * The Class Board.
 */
public final class Board implements Serializable, Iterable<Entry> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6646332294864609599L;

	/** The Constant MAX_SIZE. */
	private static final int MAX_SIZE = 30;

	/** The Constant MIN_SIZE. */
	private static final int MIN_SIZE = 6;

	/** The name. */
	private String name;

	/** The cells. */
	private Set<Entity>[][] cells;

	/** The entities and points mapping. */
	private Map<Entity, Point> entities = new HashMap<Entity, Point>();

	/** The height. */
	private int sizeY;

	/** The width. */
	private int sizeX;

	/**
	 * Instantiates a new board.
	 * 
	 * @param name
	 *            the name
	 * @param x
	 *            the width
	 * @param y
	 *            the height
	 */
	public Board(String name, int x, int y) {

		this.setName(name);
		if (x < MIN_SIZE || x > MAX_SIZE || y < MIN_SIZE || y > MAX_SIZE) {
			throw new RuntimeException();
		}
		sizeX = x;
		sizeY = y;

		initMatrix();
	}

	/**
	 * Inits the matrix.
	 */
	@SuppressWarnings("unchecked")
	private void initMatrix() {
		int x = 0;
		int y = 0;

		cells = (Set<Entity>[][]) new Set[sizeY][sizeY];
		for (y = 0; y < sizeY; y++) {
			cells[y] = (Set<Entity>[]) new Set[sizeX];
			for (x = 0; x < sizeX; x++) {
				cells[y][x] = new HashSet<Entity>();
			}
		}

	}

	/**
	 * Gets the size y.
	 * 
	 * @return the size y
	 */
	public int getSizeY() {
		return sizeY;
	}

	/**
	 * Gets the size x.
	 * 
	 * @return the size x
	 */
	public int getSizeX() {
		return sizeX;
	}

	/**
	 * Replace an entity with another one.
	 * 
	 * @param oldEntity
	 *            the old entity
	 * @param newEntity
	 *            the new entity
	 */
	public void replace(Entity oldEntity, Entity newEntity) {
		Point p = getEntityPos(oldEntity);
		remove(oldEntity);
		add(newEntity, p);
	}

	/**
	 * Get the immediate neighbours of an entity.
	 * 
	 * @param e
	 *            the entity
	 * @return the set of neighbouring entities
	 */
	protected Set<Entity> neighbours(Entity e) {
		Point p = getEntityPos(e);
		if (e != null) {
			return neighbours(p);
		}
		return null;
	}

	/**
	 * Get the immediate neighbours of a point.
	 * 
	 * @param p
	 *            the point
	 * @return the sets of neighbouring entities
	 */
	protected Set<Entity> neighbours(Point p) {
		Set<Entity> result = new HashSet<Entity>(8);
		int poiX = (int) p.getX();
		int poiY = (int) p.getY();
		int range[] = { -1, 0, 1 };
		for (int x : range) {
			for (int y : range) {
				if (isValidPoint(poiX + x, poiY + y)) {
					result.addAll(in(poiX + x, poiY + y));
				}
			}
		}
		return result;
	}

	/**
	 * @see Board#isValidPoint(Point)
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return true, if is valid point
	 */
	private boolean isValidPoint(int x, int y) {
		return x >= 0 && x < sizeX && y >= 0 && y < sizeY;
	}

	/**
	 * Checks if p is a valid point.
	 * 
	 * @param p
	 *            the point
	 * @return true, if p is a valid point
	 */
	private boolean isValidPoint(Point p) {
		return isValidPoint(p.getX(), p.getY());
	}

	/**
	 * @see Board#in(Point)
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return the sets the
	 */
	protected Set<Entity> in(int x, int y) {
		if (isValidPoint(x, y)) {
			return cells[y][x];
		}
		throw new RuntimeException();
	}

	/**
	 * Returns all entities in a given point.
	 * 
	 * @param p
	 *            the point
	 * @return the set of all the entities in the point p.
	 */
	protected Set<Entity> in(Point p) {
		return in((int) p.getX(), (int) p.getY());
	}

	/**
	 * Adds the entity.
	 * 
	 * @see Board#add(Entity, Point)
	 * 
	 * @param e
	 *            the entity
	 * @param x
	 *            the new x
	 * @param y
	 *            the new y
	 */
	protected void add(Entity e, int x, int y) {
		add(e, new Point(x, y));
	}

	/**
	 * Adds the entity.
	 * 
	 * @param e
	 *            the entity
	 * @param p
	 *            the point
	 */
	protected void add(Entity e, Point p) {
		in(p).add(e);
		entities.put(e, p);
	}

	/**
	 * Removes the entity.
	 * 
	 * @param e
	 *            the entity
	 */
	protected void remove(Entity e) {
		in(entities.get(e)).remove(e);
		entities.remove(e);
	}

	/**
	 * Contains checks for presence of an Entity.
	 * 
	 * @param e
	 *            the entity
	 * @return true, if contains the Entity
	 */
	protected boolean contains(Entity e) {
		return entities.get(e) != null;
	}

	/**
	 * Overloaded Move.
	 * 
	 * @see Board#move(Entity, Point)
	 * 
	 * @param e
	 *            the entity
	 * @param x
	 *            the new x
	 * @param y
	 *            the new y
	 * @return true, if moved
	 */
	protected boolean move(Entity e, int x, int y) {
		Point p = new Point(x, y);
		return move(e, p);
	}

	/**
	 * Move an Entity to a new Point, dispatching collisions and visibility
	 * events.
	 * 
	 * @param e
	 *            the entity
	 * @param p
	 *            the point
	 * @return true, if the entity moved
	 */
	protected boolean move(Entity e, Point p) {
		boolean moved = true;
		Point current = entities.get(e);
		if (current == null) {
			throw new RuntimeException();
		}

		if (!isValidPoint(p)) {
			return false;
		}

		for (Entity s : in(p).toArray(new Entity[0])) {
			if (contains(e)) {
				moved &= s.hitBy(e);
			}
		}

		if (contains(e)) {
			for (Entity s : in(p).toArray(new Entity[0])) {
				moved &= e.hitsTo(s);
			}
		}

		if (moved == true) {
			warpTo(e, p);
			if (contains(e)) {
				for (Entity n : neighbours(p)) {
					n.near(e);
				}
			}
		}
		return moved;
	}

	/**
	 * Gets the entity position.
	 * 
	 * @param e
	 *            the entity
	 * @return the entity position as a Point
	 */
	protected Point getEntityPos(Entity e) {
		Point p = entities.get(e);
		if (p != null) {
			p = (Point) p.clone();
		}
		return p;
	}

	/**
	 * Warp an Entity to a new point, without dispatching collisions. The Entity
	 * must already be in the board.
	 * 
	 * @param e
	 *            the e
	 * @param p
	 *            the p
	 */
	private void warpTo(Entity e, Point p) {
		Point oldPoint;

		oldPoint = entities.get(e);
		entities.put(e, p);

		in(oldPoint).remove(e);
		in(p).add(e);
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Entry> iterator() {
		return new BoardFilter(cells);
	}

	/**
	 * A BoardFilter with all the entities.
	 * 
	 * @return the board filter
	 */
	public BoardFilter filter() {
		return new BoardFilter(cells);
	}

}