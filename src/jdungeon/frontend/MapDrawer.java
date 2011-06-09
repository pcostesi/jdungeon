package jdungeon.frontend;


import java.awt.Color;
import java.awt.Image;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jdungeon.core.world.Entity;
import jdungeon.core.world.Point;
import jdungeon.core.world.Vulnerable;
import jdungeon.frontend.gui.GamePanel;
import jdungeon.frontend.gui.GamePanelListener;
import jdungeon.frontend.gui.ImageUtils;
import jdungeon.core.world.bonus.Bonus;
import jdungeon.core.world.creatures.Monster;

public class MapDrawer{

	private BasicImages basicImag;
	private GamePanel gp;
	GUI event;

	public MapDrawer(BasicImages images, GUI event) {
		this.basicImag = images;
		this.gp = new GamePanel(10, 10, 30, new BaseGamePanelListener(), Color.BLACK);
		this.event = event;
	}
	
	public void updatePoint(Point p, Entity[] elems, DataPanel panel) {
		Image actual;
		actual = GetImage(p, elems, panel);
		gp.clear(p.getX(), p.getY());
		gp.put(actual, p.getX(), p.getY());
		gp.repaint();
	}

	private Image GetImage(Point p, Entity[] elems, DataPanel panel) {
		SortedSet<Entity> oElems = new TreeSet<Entity>(new EntityComparator());
		Collections.addAll(oElems, elems);
		Image finalImage = basicImag.GiveImage(null);
		if (oElems.size() == 0) {
			finalImage = basicImag.GiveImage(null);
		} else {
			for (Entity a : oElems) {
				finalImage = ImageUtils.overlap(finalImage, basicImag
						.GiveImage(a));
				if (a instanceof Vulnerable) {
					finalImage = ImageUtils
							.drawString(finalImage, Integer
									.toString(((Vulnerable) a).getLevel()),
									Color.WHITE);
					if (a instanceof Monster) {
						panel.update(p);
					}
				}else if(a instanceof Bonus){
					finalImage = ImageUtils.drawString(finalImage,
							Integer.toString(((Bonus)a).getLevel())
							, Color.RED);
				}
			}

		}
		return finalImage;
	}

	private class BaseGamePanelListener implements GamePanelListener {

		@Override
		public void onMouseMoved(int row, int column) {
			Point a = new Point(column,row);
			event.updatePoint(a);
		}
	}

	public GamePanel GetGP() {
		return gp;
	}

}
