package jdungeon.core.world.filter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import jdungeon.core.world.Entity;
import jdungeon.core.world.Point;

/**
 * The Class BoardFilter.
 */
public class BoardFilter implements Iterator<Entry>, Iterable<Entry> {

	/** The entries. */
	private List<Entry> entries;

	/** The walk. */
	private int walk;

	/**
	 * Instantiates a new board filter.
	 * 
	 * This class is generative and greedy (executes the filters when created
	 * instead of when iterated).
	 * 
	 * @param cells
	 *            the cells
	 */
	public BoardFilter(Set<Entity>[][] cells) {
		walk = 0;
		entries = new ArrayList<Entry>();

		for (int y = 0; y < cells.length; y++) {
			for (int x = 0; x < cells[y].length; x++) {

				Point p = new Point(x, y);
				if (cells[y][x].size() == 0) {
					entries.add(new Entry(p));
				} else {
					for (Entity e : cells[y][x]) {
						entries.add(new Entry(p, e));
					}
				}
			}
		}

	}

	/**
	 * Count the objects fulfilling the criteria.
	 * 
	 * @return the number of elements
	 */
	public int count() {
		return entries.size();
	}

	/**
	 * Filter the objects given a Filter criterion.
	 * 
	 * @param f
	 *            the filter
	 * @return this very same filter
	 */
	public BoardFilter filter(Filter f) {
		List<Entry> newList = new ArrayList<Entry>(entries.size());
		for (Entry e : entries) {
			if (f.apply(e.getPoint(), e.getEntity())) {
				newList.add(e);
			}
		}
		entries = newList;
		return this;
	}

	/**
	 * Gets one (and only one) entry.
	 * 
	 * @return null, if the count is different to 1, the entity otherwise
	 */
	public Entry get() {
		if (this.count() == 1) {
			return entries.get(0);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		if (walk < 0 || walk >= entries.size()) {
			walk = 0;
			return false;
		}
		return true;
	}

	/**
	 * Keep all entries in this point
	 * 
	 * @param p
	 *            the p
	 * @return this very same filter
	 */
	public BoardFilter in(Point p) {
		return in(new Point[] { p });
	}

	/**
	 * Keep all entries in this points
	 * 
	 * @param points
	 *            the points
	 * @return this very same filter
	 */
	public BoardFilter in(final Point[] points) {
		this.filter(new Filter() {

			@Override
			public boolean apply(Point p, Entity e) {
				boolean result = false;
				for (Point wanted : points) {
					if (wanted != null && wanted.equals(p)) {
						return true;
					}
				}
				return result;
			}

		});
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Entry> iterator() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#next()
	 */
	@Override
	public Entry next() {
		if (hasNext()) {
			return entries.get(walk++);
		}
		throw new NoSuchElementException();
	}

	/**
	 * Keep only the entities where there is no entity of class c in the same
	 * cell.
	 * 
	 * @param c
	 *            the class
	 * @return this very same filter
	 */
	public BoardFilter noPointsContaining(Class<? extends Entity> c) {

		for (Entry e : entries.toArray(new Entry[0])) {
			if (c.isInstance(e.getEntity())) {
				notIn(e.getPoint());
			}
		}

		return this;
	}

	/**
	 * Discard all the elements of this class
	 * 
	 * @param c
	 *            the class
	 * @return this very same filter
	 */
	public BoardFilter not(final Class<? extends Entity> c) {
		this.filter(new Filter() {
			@Override
			public boolean apply(Point p, Entity e) {
				return c.isInstance(e) == false;
			}
		});
		return this;
	}

	/**
	 * Discard all the entries in this point.
	 * 
	 * @see BoardFilter#notIn(Point[])
	 * 
	 * @param p
	 *            the p
	 * @return this very same filter
	 */
	public BoardFilter notIn(Point p) {
		return notIn(new Point[] { p });
	}

	/**
	 * Discard all the entries in these points.
	 * 
	 * @param points
	 *            the points
	 * @return this very same filter
	 */
	public BoardFilter notIn(final Point[] points) {
		this.filter(new Filter() {

			@Override
			public boolean apply(Point p, Entity e) {
				for (Point wanted : points) {
					if (wanted.equals(p)) {
						return false;
					}
				}
				return true;
			}

		});
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove() {
		entries.remove(--walk);
	}

	/**
	 * To array.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param t
	 *            the array of type T
	 * @return the t[]
	 */
	@SuppressWarnings("unchecked")
	public <T extends Entity> T[] toArray(T[] t) {
		Set<T> newEntities = new HashSet<T>();
		Class<T> cls = (Class<T>) t.getClass().getComponentType();
		for (Entry e : this) {
			if (cls.isInstance(e.getEntity())) {
				newEntities.add(e.getEntity(cls));
			}
		}
		return newEntities.toArray(t);
	}

	/**
	 * Keep all elements which are instances of this class.
	 * 
	 * @param c
	 *            the c
	 * @return this very same filter
	 */
	public BoardFilter where(final Class<? extends Entity> c) {
		this.filter(new Filter() {

			@Override
			public boolean apply(Point p, Entity e) {
				return c.isInstance(e);
			}

		});
		return this;
	}

}
