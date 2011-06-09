package jdungeon.frontend;

import java.io.File;

import jdungeon.core.world.Point;

public interface GUI {
	public void NewGame(String a);
	public void save(File a);
	public void load(File a);
	public void updatePoint(Point p);
	public void quit();
	public void end();
	public void restart();
}
