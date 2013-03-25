package br.com.fatec.ia.sudoku.view.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JDialog;

import br.com.fatec.ia.sudoku.view.SudokuHolder;
import br.com.fatec.ia.sudoku.view.utils.SudokuConstants;

public class SelecionarNumeroListener implements ActionListener {

	private int coordX;
	private int coordY;
	private JDialog parent;

	public SelecionarNumeroListener(int coordX, int coordY, JDialog parent) {
		super();
		this.coordX = coordX;
		this.coordY = coordY;
		this.parent = parent;
	}

	public void actionPerformed(ActionEvent e) {
		String nome = ((JComponent) e.getSource()).getName();
		if (nome.startsWith(SudokuConstants.PREFIXO_BOTAO)
				&& nome.replaceAll(SudokuConstants.PREFIXO_BOTAO, "").length() == 1) {
			String numero = nome.replaceAll(SudokuConstants.PREFIXO_BOTAO, "");
			SudokuHolder.adicionarPalpiteUsuario(coordX, coordY, numero);
		} else if (SudokuConstants.BOTAO_LIMPAR.equals(nome)) {
			SudokuHolder.removerPalpiteUsuario(coordX, coordY);
		}

		parent.dispose();
		parent = null;
	}
}
