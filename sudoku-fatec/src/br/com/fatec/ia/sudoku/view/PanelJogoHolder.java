package br.com.fatec.ia.sudoku.view;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;

public class PanelJogoHolder {

	private Map<Point, JButton> botoes = new HashMap<Point, JButton>();
	private JPanel painel = null;

	public PanelJogoHolder() {
		botoes.clear();
	}

	public JPanel getPainel() {
		return painel;
	}

	public void setPainel(JPanel painel) {
		this.painel = painel;
	}

	public Map<Point, JButton> getBotoes() {
		return botoes;
	}

}
