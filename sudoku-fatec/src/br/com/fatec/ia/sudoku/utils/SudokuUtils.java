package br.com.fatec.ia.sudoku.utils;

import br.com.fatec.ia.sudoku.JogoSudoku;

public class SudokuUtils {

	private static final long MULT_MINUTO = 60;
	private static final long MULT_HORA = 60 * MULT_MINUTO;
	
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

	public static String getTempoExibicao(long timer) {
		Long hora = 0l;
		Long minuto = 0l;
		Long segundo = 0l;

		String horas = "00";
		String minutos = "00";
		String segundos = "00";
		if (timer >= MULT_HORA) {
			hora = timer / MULT_HORA;
			horas = new String(Long.toString(hora));
		}
		if (timer >= MULT_MINUTO) {
			minuto = (timer - (hora * MULT_HORA)) / MULT_MINUTO;
			minutos = new String(Long.toString(minuto));
		}

		if (minuto > 0) {
			segundo = (timer - (hora * MULT_HORA) - (minuto * MULT_MINUTO));
			segundos = new String(Long.toString(segundo));
		} else {
			segundos = new String(Long.toString(timer));
		}

		return alinharAEsquerda(horas, 2) + ":" + alinharAEsquerda(minutos, 2)
				+ ":" + alinharAEsquerda(segundos, 2);
	}

	public static String alinharAEsquerda(String str, Integer qndtCasas) {
		if (str != null && str.length() < qndtCasas) {
			for (int i = str.length(); i < qndtCasas; i++) {
				str = "0" + str;
			}
		}
		return str;
	}

}
