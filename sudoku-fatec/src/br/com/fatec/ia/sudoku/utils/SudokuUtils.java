package br.com.fatec.ia.sudoku.utils;

import br.com.fatec.ia.sudoku.JogoSudoku;

public class SudokuUtils {

	public static void printSudoku(int[][] jogo) {
		for (int i = 0; i < jogo.length; i++) {
			for (int j = 0; j < jogo[i].length; j++) {
				if (jogo[i][j] == JogoSudoku.POSICAO_EM_BRANCO
						|| jogo[i][j] == JogoSudoku.POSICAO_NAO_JOGADA) {
					System.out.print("  ");
				} else {
					System.out.print(jogo[i][j] + " ");
				}
				if ((j + 1) % 3 == 0)
					System.out.print("| ");
			}
			System.out.println();
			if ((i + 1) % 3 == 0)
				System.out.println("----------------------");
		}
	}

}
