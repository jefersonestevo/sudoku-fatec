package br.com.fatec.ia.sudoku.view;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import br.com.fatec.ia.sudoku.view.menu.MenuListener;

public class SudokuFrame extends JFrame {

	private static final long serialVersionUID = -4793779446674833364L;

	private JPanel panelCorpo;

	public SudokuFrame() {
		super(Messages.getMessage("Nome_App"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		pack();
		setSize(500, 500);
		setResizable(false);

		geraMenu();

		panelCorpo = new JPanel();
		panelCorpo.setSize(500, 500);
		panelCorpo.setVisible(true);
		add(panelCorpo);
	}

	public void addNovoJogo(JPanel panelJogo) {
		panelCorpo.removeAll();
		panelCorpo.add(panelJogo);
		panelCorpo.setSize(500, 500);
		panelCorpo.setVisible(true);

		panelCorpo.revalidate();
		panelCorpo.repaint();
	}

	private void geraMenu() {

		JMenu menuJogo = new JMenu(Messages.getMessage("Menu_Jogo"));

		JMenuItem menuNovo = new JMenuItem(Messages.getMessage("Menu_Novo"));
		menuNovo.setName(MenuListener.MENU_NOVO);
		menuNovo.addActionListener(new MenuListener());

		JMenuItem menuSair = new JMenuItem(Messages.getMessage("Menu_Sair"));
		menuSair.setName(MenuListener.MENU_SAIR);
		menuSair.addActionListener(new MenuListener());

		menuJogo.add(menuNovo);
		menuJogo.add(new JSeparator());
		menuJogo.add(menuSair);

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menuJogo);
		setJMenuBar(menuBar);
	}

	public JPanel getPanelCorpo() {
		return panelCorpo;
	}

}
