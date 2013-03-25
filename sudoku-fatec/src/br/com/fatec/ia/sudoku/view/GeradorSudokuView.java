package br.com.fatec.ia.sudoku.view;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import br.com.fatec.ia.sudoku.Dificuldade;
import br.com.fatec.ia.sudoku.JogoSudoku;
import br.com.fatec.ia.sudoku.gerador.GeradorSudoku;
import br.com.fatec.ia.sudoku.view.menu.PreencherBotaoListener;

public class GeradorSudokuView {

	public static JPanel gerarNovoJogoSudoku(Dificuldade dificuldade) {
		// Painel do Sudoku com os separators
		JPanel panel = new JPanel(new GridLayout(3, 3));

		JogoSudoku jogo = new GeradorSudoku().gerarJogoSudoku(dificuldade);
		SudokuHolder.setJogoAtual(jogo);

		int[][] campos = jogo.getJogoReal();

		JPanel panelFilho = null;
		JPanel[][] panelQuadrante = new JPanel[3][3];
		ActionListener listenerBotao = new PreencherBotaoListener();

		for (int i = 0; i < campos.length; i++) {
			for (int j = 0; j < campos[i].length; j++) {

				int quadX = i / 3;
				// quadX = quadX * 3;
				int quadY = j / 3;
				// quadY = quadY * 3;

				panelFilho = panelQuadrante[quadX][quadY];
				if (panelFilho == null) {
					panelFilho = new JPanel(new GridLayout(3, 3));
					panelQuadrante[quadX][quadY] = panelFilho;
				}

				String titulo = "";
				if (JogoSudoku.POSICAO_EM_BRANCO != campos[i][j]) {
					titulo = new Integer(campos[i][j]).toString();
				}
				JButton botao = new JButton(titulo);
				botao.setName("btn" + i + j);
				botao.addActionListener(listenerBotao);
				panelFilho.add(botao);

			}
		}

		for (int i = 0; i < panelQuadrante.length; i++) {
			for (int j = 0; j < panelQuadrante[i].length; j++) {
				panel.add(panelQuadrante[i][j]);
			}
		}

		return panel;
	}

	public static void adicionarPalpiteUsuario(int x, int y, int valor) {
		JogoSudoku jogo = SudokuHolder.getJogoAtual();
		jogo.addPalpite(x, y, valor);

		JPanel panel = SudokuHolder.getFramePrincipal().getPanelCorpo();
		JButton btn = (JButton) panel.findComponentAt(x, y);

		btn.setText("" + valor);
	}

}
