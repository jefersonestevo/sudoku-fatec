package br.com.fatec.ia.sudoku.view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import br.com.fatec.ia.sudoku.JogoSudoku;
import br.com.fatec.ia.sudoku.view.menu.PreencherBotaoListener;
import br.com.fatec.ia.sudoku.view.utils.SudokuConstants;

public class GeradorSudokuView {

	public static PanelJogoHolder gerarNovoPanelSudoku(JogoSudoku jogo) {
		PanelJogoHolder holder = new PanelJogoHolder();

		// Painel do Sudoku com os separators
		JPanel panel = new JPanel(new GridLayout(3, 3));
		panel.setSize(450, 450);

		int[][] campos = jogo.getJogoReal();

		JPanel panelFilho = null;
		JPanel[][] panelQuadrante = new JPanel[3][3];
		ActionListener listenerBotao = new PreencherBotaoListener();

		for (int i = 0; i < campos.length; i++) {
			for (int j = 0; j < campos[i].length; j++) {

				int quadX = i / 3;
				int quadY = j / 3;

				panelFilho = panelQuadrante[quadX][quadY];
				if (panelFilho == null) {
					panelFilho = new JPanel(new GridLayout(3, 3));
					panelFilho.setSize(150, 150);
					panelFilho.setBorder(new LineBorder(Color.BLACK));
					panelQuadrante[quadX][quadY] = panelFilho;
				}

				JButton botao = null;
				if (JogoSudoku.POSICAO_EM_BRANCO != campos[i][j]) {
					botao = new JButton(new Integer(campos[i][j]).toString());
					botao.setBackground(SudokuConstants.COR_JOGO_REAL);
				} else {
					botao = new JButton();
					botao.addActionListener(listenerBotao);
					botao.setBackground(SudokuConstants.COR_PADRAO);
				}
				botao.setSize(45, 45);
				botao.setName(SudokuConstants.PREFIXO_BOTAO + i + j);

				panelFilho.add(botao);

				// Add botao no holder
				holder.getBotoes().put(new Point(i, j), botao);
			}
		}

		for (int i = 0; i < panelQuadrante.length; i++) {
			for (int j = 0; j < panelQuadrante[i].length; j++) {
				panel.add(panelQuadrante[i][j]);
			}
		}

		holder.setPainel(panel);
		return holder;
	}

}
