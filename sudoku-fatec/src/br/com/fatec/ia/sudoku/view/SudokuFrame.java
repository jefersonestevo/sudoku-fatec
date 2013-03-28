package br.com.fatec.ia.sudoku.view;

import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.Timer;

import br.com.fatec.ia.sudoku.utils.SudokuUtils;
import br.com.fatec.ia.sudoku.view.menu.MenuListener;
import br.com.fatec.ia.sudoku.view.utils.Messages;
import br.com.fatec.ia.sudoku.view.utils.SudokuConstants;

public class SudokuFrame extends JFrame {

	private static final long serialVersionUID = -4793779446674833364L;

	/**
	 * Panel para armazenar a matriz do jogo do Sudoku. Cada vez que o menu for
	 * alterado, será repintado este painel
	 */
	private JPanel panelCorpo;

	/**
	 * Panel para armazenar o label a ser apresentado para o jogo. <br />
	 * Se o jogo estiver em andamento, mostrará o timer. <br />
	 * SE o jogo estiver finalizado, exibirá uma mensagem de térmido do jogo.
	 */
	private JPanel panelLabel;

	/**
	 * Armazena os botoes para cada posicao do Jogo do Sudoku
	 */
	private Map<Point, JButton> botoes = new HashMap<Point, JButton>();

	private boolean isJogoTerminado = false;
	private Timer timer;

	public SudokuFrame() {
		super(Messages.getMessage("Nome_App"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		setSize(400, 340);
		setResizable(false);

		geraMenu();

		panelCorpo = new JPanel();
		panelCorpo.setSize(500, 500);
		panelCorpo.setVisible(true);
		panelCorpo.setLocation(getX(), getY() + 100);
		add(panelCorpo);
		limpar();
	}

	public void addNovoJogo(PanelJogoHolder holder) {
		zerarTimer();
		panelCorpo.removeAll();
		panelCorpo.setSize(500, 500);
		panelCorpo.setVisible(true);

		panelCorpo.add(getPanelLabel());
		panelCorpo.add(holder.getPainel());
		panelCorpo.revalidate();
		panelCorpo.repaint();

		botoes.clear();
		botoes.putAll(holder.getBotoes());
		isJogoTerminado = false;
		geraMenu();
	}

	private JPanel getPanelLabel() {
		panelLabel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		final JLabel labelTempo = new JLabel(
				SudokuUtils.getTempoExibicao(SudokuHolder.getTempoJogoAtual()));
		timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!isJogoTerminado) {
					SudokuHolder.atualizarTimerJogo();
					labelTempo.setText(SudokuUtils
							.getTempoExibicao(SudokuHolder.getTempoJogoAtual()));
				}
			}
		});
		timer.setInitialDelay(0);
		timer.start();
		panelLabel.add(labelTempo);
		return panelLabel;
	}

	public void alterarValorBotao(int x, int y, String novoValor) {
		JButton botao = botoes.get(new Point(x, y));
		botao.setText(novoValor);
	}

	public void invalidarBotao(int x, int y) {
		JButton botao = botoes.get(new Point(x, y));
		if (SudokuHolder.isPermitidoPalpite(x, y))
			botao.setBackground(SudokuConstants.COR_CAMPO_INVALIDO);
		else
			botao.setBackground(SudokuConstants.COR_CAMPO_REAL_INVALIDO);
	}

	public void validarBotao(int x, int y) {
		JButton botao = botoes.get(new Point(x, y));
		if (SudokuHolder.isPermitidoPalpite(x, y))
			botao.setBackground(SudokuConstants.COR_PADRAO);
		else
			botao.setBackground(SudokuConstants.COR_JOGO_REAL);
	}

	public void finalizarJogo() {
		for (JButton botao : botoes.values()) {
			botao.setEnabled(false);
		}

		panelLabel.removeAll();
		JLabel label = new JLabel();
		label.setText(Messages.getMessage("Mensagem_Jogo_Finalizado")
				+ SudokuUtils.getTempoExibicao(SudokuHolder.getTempoJogoAtual()));
		panelLabel.add(label);

		isJogoTerminado = true;
		zerarTimer();
	}

	public void limpar() {
		if (this.botoes != null)
			this.botoes.clear();
		if (this.panelCorpo != null) {
			this.panelCorpo.removeAll();
			this.panelCorpo.revalidate();
			this.panelCorpo.repaint();
		}
		if (this.panelLabel != null) {
			this.panelLabel.removeAll();
			this.panelLabel.revalidate();
			this.panelLabel.repaint();
		}
		zerarTimer();
		geraMenu();
	}

	private void geraMenu() {

		JMenu menuJogo = new JMenu(Messages.getMessage("Menu_Jogo"));

		JMenuItem menuNovo = new JMenuItem(Messages.getMessage("Menu_Novo"));
		menuNovo.setName(MenuListener.MENU_NOVO);
		menuNovo.addActionListener(new MenuListener());
		menuJogo.add(menuNovo);
		menuJogo.add(new JSeparator());

		if (SudokuHolder.getJogoAtual() != null) {
			JMenuItem menuSalvar = new JMenuItem(
					Messages.getMessage("Menu_Salvar"));
			menuSalvar.setName(MenuListener.MENU_SALVAR);
			menuSalvar.addActionListener(new MenuListener());
			menuJogo.add(menuSalvar);
		}

		JMenuItem menuCarregar = new JMenuItem(
				Messages.getMessage("Menu_Carregar"));
		menuCarregar.setName(MenuListener.MENU_CARREGAR);
		menuCarregar.addActionListener(new MenuListener());
		menuJogo.add(menuCarregar);
		menuJogo.add(new JSeparator());

		JMenuItem menuSair = new JMenuItem(Messages.getMessage("Menu_Sair"));
		menuSair.setName(MenuListener.MENU_SAIR);
		menuSair.addActionListener(new MenuListener());
		menuJogo.add(menuSair);

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menuJogo);
		setJMenuBar(menuBar);
	}

	private void zerarTimer() {
		if (timer != null)
			timer.stop();
	}

}
