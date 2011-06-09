package jdungeon.frontend;

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;

import jdungeon.core.world.Entity;
import jdungeon.core.world.Player;
import jdungeon.core.world.bonus.*;
import jdungeon.core.world.creatures.*;
import jdungeon.core.world.scenery.*;
import jdungeon.frontend.gui.ImageUtils;

public class BasicImages {

	private HashMap<String, Image> map = new HashMap<String, Image>();

	public BasicImages() throws IOException {
		map.put(Player.class.getName(), ImageUtils
				.loadImage("resources/hero.png"));
		map.put(HealthBonus.class.getName(), ImageUtils
				.loadImage("resources/healthBoost.png"));
		map.put(StrengthBonus.class.getName(), ImageUtils
				.loadImage("resources/attackBoost.png"));
		map.put(Dragon.class.getName(), ImageUtils
				.loadImage("resources/dragon.png"));
		map.put(Snake.class.getName(), ImageUtils
				.loadImage("resources/serpent.png"));
		map.put(Golem.class.getName(), ImageUtils
				.loadImage("resources/golem.png"));
		map.put(Splat.class.getName(), ImageUtils
				.loadImage("resources/blood.png"));
		map.put(Wall.class.getName(), ImageUtils
				.loadImage("resources/wall.png"));
		map.put("null", ImageUtils.loadImage("resources/background.png"));
	}

	public Image GiveImage(Entity data) {
		if (data == null) {
			return map.get("null");
		}
		return map.get(data.getClass().getName());
	}

}
