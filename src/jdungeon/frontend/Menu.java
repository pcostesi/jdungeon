package jdungeon.frontend;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Menu extends JFrame {

	private static final long serialVersionUID = -653989361793094437L;
	private JMenuBar menu;
	private JMenu game;
	private JMenu select;
	private JMenu nGame;
	private JMenuItem lGame;
	private JMenuItem sGame;
	private JMenuItem exit;
	private JMenuItem restart;
	private JMenuItem endGame;
	private File file;
	private GUI event;
	
	public JMenuBar NewGame(final GUI event, final Set<String> name) {
		this.event = event;
		menu = new JMenuBar();
		select = new JMenu("File");
		nGame = new JMenu("Nueva Partida");
		JMenuItem mapName = null;
		nGame.setToolTipText("Se inicia una Nueva Partida");
		for (final String a : name) {
			mapName = new JMenuItem(a);
			mapName.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					event.NewGame(a);
				}
			});
			nGame.add(mapName);
		}
		sGame = new JMenuItem("Guardar Partida");
		sGame.setToolTipText("Se guarda la partida");
		sGame.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e) {
				if(sGame.isEnabled()){
					JFileChooser explorer = new JFileChooser();
					explorer.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					explorer.setMultiSelectionEnabled(false);
					explorer.setApproveButtonText("Guardar");
					int returnVal;
					returnVal = explorer.showOpenDialog(explorer);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						file = explorer.getSelectedFile();
						event.save(file);
					}
				}
			}
		});
		lGame = new JMenuItem("Cargar Juego");
		lGame.setToolTipText("Se carga una partida");
		lGame.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e) {
				JFileChooser explorer = new JFileChooser();
				explorer.setMultiSelectionEnabled(false);
				explorer.setApproveButtonText("Cargar");
				int returnVal;
				returnVal = explorer.showOpenDialog(explorer);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					file = explorer.getSelectedFile();
					event.load(file);
				}
			}
		});
		exit = new JMenuItem("Salir");
		exit.setToolTipText("Sale del juego");
		exit.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e) {
				event.quit();
			}
		});
		game = new JMenu("Juego");
		endGame = new JMenuItem("Terminar Partida");
		endGame.setToolTipText("Termina la partida actual");
		endGame.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e) {
				event.end();
			}
		});
		restart = new JMenuItem("Reiniciar Juego");
		restart.setToolTipText("Reinicia el mapa actual");
		restart.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e) {
				event.restart();
			}
		});
		game.add(endGame);
		game.add(restart);
		menu.add(select);
		select.add(nGame);
		select.add(sGame);
		select.add(lGame);
		select.add(exit);
		return menu;

	}
	
	public void addOptions(){
		menu.add(game);
	}
	public void removeOptions(){
		menu.remove(game);
	}
	

}
