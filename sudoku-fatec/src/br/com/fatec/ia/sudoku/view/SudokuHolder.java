package br.com.fatec.ia.sudoku.view;

import br.com.fatec.ia.sudoku.Dificuldade;
import br.com.fatec.ia.sudoku.JogoSudoku;
import br.com.fatec.ia.sudoku.gerador.GeradorSudoku;

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

	public static void gerarNovoJogo(Dificuldade dificuldade) {
		JogoSudoku jogo = new GeradorSudoku().gerarJogoSudoku(dificuldade);
		gerarJogoAPartirDe(jogo);
	}

	public static void gerarJogoAPartirDe(JogoSudoku jogo) {
		getFramePrincipal().addNovoJogo(
				GeradorSudokuView.gerarNovoPanelSudoku(jogo));
		setJogoAtual(jogo);

		int[][] palpitesUsuario = jogo.getPalpitesUsuario();
		for (int x = 0; x < palpitesUsuario.length; x++) {
			for (int y = 0; y < palpitesUsuario[x].length; y++) {
				if (palpitesUsuario[x][y] != JogoSudoku.POSICAO_NAO_JOGADA
						&& palpitesUsuario[x][y] != JogoSudoku.POSICAO_EM_BRANCO) {
					adicionarPalpiteUsuario(x, y,
							Integer.toString(palpitesUsuario[x][y]));
				}
			}
		}
	}

	public static void adicionarPalpiteUsuario(int x, int y, String novoValor) {
		if (getJogoAtual().addPalpite(x, y, Integer.parseInt(novoValor))) {
			getFramePrincipal().alterarValorBotao(x, y, novoValor);
			if (getJogoAtual().isFimJogo()) {
				// TODO - Implementar fim do jogo
			}
		}
	}

	public static void removerPalpiteUsuario(int x, int y) {
		if (getJogoAtual().removerPalpite(x, y)) {
			getFramePrincipal().alterarValorBotao(x, y, "");
		}
	}

}
