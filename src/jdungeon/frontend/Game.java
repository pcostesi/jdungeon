package jdungeon.frontend;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import jdungeon.parser.FailureException;
import jdungeon.parser.WorldAssembler;

import jdungeon.core.world.Entity;
import jdungeon.core.world.MapObserver;
import jdungeon.core.world.World;
import jdungeon.core.world.Point;

public class Game extends JFrame implements MapObserver, GUI {

	private static final long serialVersionUID = -193613824256272240L;
	private MapDrawer map = null;
	private DataPanel panel;
	private JPanel listener;
	private World w = null;
	private World clone = null;
	private Menu menuData;
	private Dimension dim;
	private BasicImages basicImag;

	public Game() {
		startListener();
		try {
			basicImag = new BasicImages();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error al cargar imagenes");
			System.exit(1);
		}
	}

	private String askName() {
		String aux = JOptionPane
				.showInputDialog("Ingrese el nombre del jugador");
		return aux;
	}

	@Override
	public void end() {
		endMap();
	}

	public void endMap() {
		setSize(500,500);
		remove(map.GetGP());
		remove(panel.getPanel());
		repaint();
		map = null;
		panel = null;
		menuData.removeOptions();
	}

	@Override
	public void load(File a) {
		try {
			w = WorldAssembler.loadGame(a);
			w.subscribe(this);
			w.getBoss();
			w.getPlayer();
		} catch (FailureException e) {
			JOptionPane.showMessageDialog(this,
					"No se ha podido cargar el juego");
			return;
		}
		menuData.addOptions();
		startGame();
	}

	@Override
	public void NewGame(String mapname) {
		try {
			w = WorldAssembler.createNewWorld(mapname);
			w.subscribe(this);
			w.getBoss();
			w.getPlayer();
			clone = (World) w.clone();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error al parsear mapa");
			System.exit(1);
		}
		w.getPlayer().setName(askName());
		startGame();
	}

	@Override
	public void onBossDeath() {
		w.syncEvent();
		JOptionPane.showMessageDialog(this, "Has ganado el juego");
		endMap();
	}

	@Override
	public void onMapChange(Point p, Entity[] entities) {
		if (map == null) {
			return;
		}
		map.updatePoint(p, entities, panel);
	}

	@Override
	public void onPlayerDeath() {
		w.syncEvent();
		JOptionPane.showMessageDialog(this, "Has perdido el juego");
		endMap();
	}

	@Override
	public void quit() {
		JOptionPane.showMessageDialog(this, "Saliendo del programa");
		System.exit(1);
	}

	@Override
	public void restart() {
		endMap();
		try {
			w = (World) clone.clone();
			w.syncEvent();
		} catch (CloneNotSupportedException e) {
			JOptionPane.showMessageDialog(this,
					"No se ha podido reiniciar el juego");
			return;
		}
		startGame();
	}

	@Override
	public void save(File a) {
		String name;
		if (w == null) {
			JOptionPane.showMessageDialog(this,
					"El juego no se encuentra iniciado");
			return;
		}
		name = JOptionPane
				.showInputDialog("Ingrese el nombre de archivo a guardar");
		try {
			WorldAssembler.saveGame(name, a, w);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
					"No se ha podido guardar el juego");
		}
	}

	public void start() {
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		Menu menuData = new Menu();
		this.menuData = menuData;
		setTitle("jDungeon");
		setLayout(new BorderLayout());
		setSize(500, 500);
		setLocationRelativeTo(getRootPane()); 
		setResizable(false);
		Set<String> names = null;
		try {
			names = WorldAssembler.getWorldList();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "No hay mapas correctos");
			System.exit(1);
		}
		add(menuData.NewGame(this, names), BorderLayout.NORTH);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public void startGame() {
		if (map != null && panel != null) {
			endMap();
		}
		menuData.addOptions();
		map = new MapDrawer(basicImag, this, w.getX(), w.getY());
		add(map.GetGP());
		panel = new DataPanel(w);
		add(panel.getPanel(), BorderLayout.EAST);
		panel.event();
		System.out.println(w.getX());
		setSize(w.getX()*30+120, w.getY()*30+45);
		w.syncEvent();
		setLocationRelativeTo(getRootPane()); 
	}

	private void startListener() {
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (w == null) {
					return;
				}
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					w.getPlayer().moveUp();
					break;
				case KeyEvent.VK_DOWN:
					w.getPlayer().moveDown();
					break;
				case KeyEvent.VK_LEFT:
					w.getPlayer().moveLeft();
					break;
				case KeyEvent.VK_RIGHT:
					w.getPlayer().moveRight();
					break;
				}
				if (panel != null) {
					panel.event();
					repaint();
				}
			}
		});
	}

	public void updatePoint(Point p) {
		panel.update(p);
	}
}
