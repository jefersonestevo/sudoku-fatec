package br.com.fatec.ia.sudoku.test;

import br.com.fatec.ia.sudoku.Dificuldade;
import br.com.fatec.ia.sudoku.JogoSudoku;
import br.com.fatec.ia.sudoku.gerador.GeradorSudoku;
import br.com.fatec.ia.sudoku.utils.SudokuUtils;

public class SudokuTest {

	public static void main(String[] args) {

		GeradorSudoku gerador = new GeradorSudoku();
		JogoSudoku sudoku = gerador.gerarJogoSudoku(Dificuldade.DIFICIL);

		SudokuUtils.printSudoku(sudoku.getJogoCompleto());
		System.out.println("\n");
		SudokuUtils.printSudoku(sudoku.getJogoReal());

		System.out.println("\n");
		System.out.println(sudoku.isFimJogo());

		for (int i = 0; i < sudoku.getJogoCompleto().length; i++) {
			for (int j = 0; j < sudoku.getJogoCompleto()[i].length; j++) {
				sudoku.addPalpite(i, j, sudoku.getJogoCompleto()[i][j]);
			}
		}
		System.out.println(sudoku.isFimJogo());
	}

}
