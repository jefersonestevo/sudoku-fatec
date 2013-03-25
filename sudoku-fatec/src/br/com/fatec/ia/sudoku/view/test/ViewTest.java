package br.com.fatec.ia.sudoku.view.test;

import br.com.fatec.ia.sudoku.view.SudokuHolder;

public class ViewTest {

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SudokuHolder.getFramePrincipal();
			}
		});
	}

}
