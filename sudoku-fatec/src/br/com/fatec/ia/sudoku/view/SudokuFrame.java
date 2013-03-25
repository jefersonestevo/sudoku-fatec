package br.com.fatec.ia.sudoku.view;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import br.com.fatec.ia.sudoku.view.menu.MenuListener;
import br.com.fatec.ia.sudoku.view.utils.Messages;

public class SudokuFrame extends JFrame {

	private static final long serialVersionUID = -4793779446674833364L;

	/**
	 * Panel para armazenar a matriz do jogo do Sudoku. Cada vez que o menu for
	 * alterado, será repintado este painel
	 */
	private JPanel panelCorpo;

	/**
	 * Armazena os botoes para cada posicao do Jogo do Sudoku
	 */
	private Map<Point, JButton> botoes = new HashMap<Point, JButton>();

	public SudokuFrame() {
		super(Messages.getMessage("Nome_App"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		setSize(500, 500);
		setResizable(false);

		geraMenu();

		panelCorpo = new JPanel();
		panelCorpo.setSize(500, 500);
		panelCorpo.setVisible(true);
		add(panelCorpo);
	}

	public void addNovoJogo(PanelJogoHolder holder) {
		panelCorpo.removeAll();
		panelCorpo.add(holder.getPainel());
		panelCorpo.setSize(500, 500);
		panelCorpo.setVisible(true);

		panelCorpo.revalidate();
		panelCorpo.repaint();

		botoes.clear();
		botoes.putAll(holder.getBotoes());
	}

	public void alterarValorBotao(int x, int y, String novoValor) {
		JButton botao = botoes.get(new Point(x, y));
		botao.setText(novoValor);
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

}
