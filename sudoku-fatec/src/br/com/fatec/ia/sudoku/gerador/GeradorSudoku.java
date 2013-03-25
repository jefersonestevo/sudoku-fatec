package br.com.fatec.ia.sudoku.gerador;

import puzzle.npgenerator.v1.Generator;
import puzzle.npgenerator.v1.Problem;
import br.com.fatec.ia.sudoku.Dificuldade;
import br.com.fatec.ia.sudoku.JogoSudoku;

public class GeradorSudoku {

	public JogoSudoku gerarJogoSudoku(Dificuldade dificuldade) {
		Generator generator = new Generator();
		boolean[][] casasParaGeracao = new boolean[9][9];

		for (int i = 0; i < casasParaGeracao.length; i++) {
			for (int j = 0; j < casasParaGeracao[i].length; j++) {
				casasParaGeracao[i][j] = true;
			}
		}

		Problem problem = generator.make(casasParaGeracao);

		int[][] jogo = new int[9][9];
		problem.getSolution(jogo);

		return new JogoSudoku(jogo, dificuldade);
	}

}
