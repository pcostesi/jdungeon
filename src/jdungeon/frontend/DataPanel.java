package jdungeon.frontend;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jdungeon.core.world.Entity;
import jdungeon.core.world.Player;
import jdungeon.core.world.Point;
import jdungeon.core.world.Vulnerable;
import jdungeon.core.world.World;
import jdungeon.core.world.creatures.Monster;

public class DataPanel extends JPanel {

	private static final long serialVersionUID = 2771191044280926526L;

	private JPanel general = new JPanel();
	private JLabel playerhp = null;
	private JLabel playerstr = null;
	private JLabel playerlvl = null;
	private JLabel playerexp = null;
	private JLabel enemyhp = null;
	private JLabel enemystr = null;
	private JLabel enemylvl = null;
	private JLabel enemyname = null;
	private JLabel playername = null;
	Point lastubication = null;
	Point currentubication = null;
	Vulnerable enemy;
	World w;

	public DataPanel(World w) {
		this.w = w;
		createPlayer();
	}

	public JPanel getPanel() {
		general.setLayout(new BoxLayout(general, BoxLayout.Y_AXIS));
		repaint();
		return general;
	}

	public void event() {
		Player a = w.getPlayer();
		playerhp.setText("Salud: " + Integer.toString(a.getHP()) + "/"
				+ a.getMaxHealth() + "\n");
		playerstr
				.setText("Fuerza: " + Integer.toString(a.getStrength()) + "\n");
		playerlvl.setText("Level: " + Integer.toString(a.getLevel()));
		playerexp.setText("Experiencia: " + Integer.toString(a.getExperience())
				+ "/" + Integer.toString(a.getLevel() * 5) + "\n");
		updateMonster();
	}

	public void showMonster(Entity a) {
		if (!(a instanceof Vulnerable)) {
			return;
		}
		if (enemyhp == null || enemystr == null || enemylvl == null) {
			createEnemy((Vulnerable) a);
			return;
		}
		this.enemy = (Vulnerable) a;
		enemyname.setText(enemy.getClass().getSimpleName());
		enemyhp.setText("Salud: " + Integer.toString(((Vulnerable) a).getHP())
				+ "/" + ((Vulnerable) a).getMaxHealth() + "\n");
		enemystr.setText("Fuerza: "
				+ Integer.toString(((Vulnerable) a).getStrength()) + "\n");
		enemylvl.setText("Level: "
				+ Integer.toString(((Vulnerable) a).getLevel()) + "\n");

	}

	public void createPlayer() {
		Player a = w.getPlayer();
		playername = new JLabel(a.getName());
		playerhp = new JLabel("Salud: " + Integer.toString(a.getHP()) + "/"
				+ a.getMaxHealth() + "\n");
		playerstr = new JLabel("Fuerza: " + Integer.toString(a.getStrength())
				+ "\n");
		playerlvl = new JLabel("Level: " + Integer.toString(a.getLevel())
				+ "\n");
		playerexp = new JLabel("Experiencia: "
				+ Integer.toString(a.getExperience()) + "/"
				+ Integer.toString(a.getLevel() * 5) + "\n");
		general.add(playername);
		general.add(playerhp);
		general.add(playerstr);
		general.add(playerlvl);
		general.add(playerexp);
	}

	public void createEnemy(Vulnerable a) {
		enemyname = new JLabel(a.getClass().getSimpleName());
		enemyhp = new JLabel("Salud: " + Integer.toString(a.getHP()) + "/"
				+ a.getMaxHealth());
		enemystr = new JLabel("Fuerza: " + Integer.toString(a.getStrength()));
		enemylvl = new JLabel("Level: " + Integer.toString(a.getLevel()));
		this.enemy = a;
		general.add(enemyname);
		general.add(enemyhp);
		general.add(enemystr);
		general.add(enemylvl);
		general.repaint();
	}

	public void update(Point p) {
		Monster[] elem;
		if((elem = w.getEntityInPoint(p, new Monster[0])).length > 0){
			showMonster(elem[0]);
			updateMonster();
			if(lastubication == null){
				lastubication = currentubication;
			}
			currentubication = p;
		}else if((elem = w.getEntityInPoint(lastubication, new Monster[0])).length > 0){
			showMonster(elem[0]);
			updateMonster();
			currentubication = lastubication;
		} else if (enemyname != null) {
			removeEnemy();
			general.repaint();
		}
	}

	private void updateMonster() {
		if (enemyhp == null) {
			return;
		}
		if (currentubication != null && 
				(w.getEntityInPoint(currentubication, new Monster[0])).length == 0) {
			removeEnemy();
			return;
		}
		enemyhp.setText("Salud: " + Integer.toString(enemy.getHP()) + "/"
				+ (enemy.getMaxHealth() + "\n"));
		enemystr.setText("Fuerza: " + Integer.toString(enemy.getStrength())
				+ "\n");
		enemylvl.setText("Level: " + Integer.toString(enemy.getLevel()) + "\n");
	}

	private void removeEnemy() {
		general.remove(enemyname);
		general.remove(enemyhp);
		general.remove(enemystr);
		general.remove(enemylvl);
		enemyname = null;
		enemyhp = null;
		enemystr = null;
		enemylvl = null;
		currentubication = null;
		lastubication = null;
	}
}
