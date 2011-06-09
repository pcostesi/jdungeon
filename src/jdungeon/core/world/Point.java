package jdungeon.core.world;

import java.io.Serializable;

/**
 * The Class Point.
 */
public class Point implements Cloneable, Serializable{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4623311443368129188L;
	
	/** The x. */
	private int x;
	
	/** The y. */
	private int y;
	
	/**
	 * Instantiates a new point.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Translate (move) the point by a delta.
	 *
	 * @param dx the dx
	 * @param dy the dy
	 */
	public void translate(int dx, int dy){
		this.x += dx;
		this.y += dy;
	}
	
	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public int getX(){
		return x;
	}
	
	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public int getY(){
		return y;
	}
	
	/**
	 * Equals.
	 *
	 * @param p the p
	 * @return true, if successful
	 */
	public boolean equals(Point p){
		return p != null && p.getX() == x && p.getY() == y;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone(){
		return new Point(x, y);
	}
	
	
}
