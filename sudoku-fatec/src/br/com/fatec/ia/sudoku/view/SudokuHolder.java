package br.com.fatec.ia.sudoku.view;

import br.com.fatec.ia.sudoku.JogoSudoku;

public class SudokuHolder {

	private static SudokuFrame framePrincipal;

	private static JogoSudoku jogoAtual;

	public static SudokuFrame getFramePrincipal() {
		if (framePrincipal == null) {
			framePrincipal = new SudokuFrame();
		}
		return framePrincipal;
	}

	public static JogoSudoku getJogoAtual() {
		return jogoAtual;
	}

	public static void setJogoAtual(JogoSudoku jogoAtual) {
		SudokuHolder.jogoAtual = jogoAtual;
	}

}
