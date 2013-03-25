package br.com.fatec.ia.sudoku;

import br.com.fatec.ia.sudoku.view.SudokuHolder;

public class Main {

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SudokuHolder.getFramePrincipal();
			}
		});
	}

}
