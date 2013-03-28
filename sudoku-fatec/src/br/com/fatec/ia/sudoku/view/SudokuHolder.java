package br.com.fatec.ia.sudoku.view;

import java.util.Set;

import br.com.fatec.ia.sudoku.Dificuldade;
import br.com.fatec.ia.sudoku.JogoSudoku;
import br.com.fatec.ia.sudoku.PosicaoSudoku;
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

	private static void setJogoAtual(JogoSudoku jogoAtual) {
		SudokuHolder.jogoAtual = jogoAtual;
	}

	public static void gerarNovoJogo(Dificuldade dificuldade) {
		JogoSudoku jogo = new GeradorSudoku().gerarJogoSudoku(dificuldade);
		gerarJogoAPartirDe(jogo);
	}

	public static void gerarJogoAPartirDe(JogoSudoku jogo) {
		setJogoAtual(jogo);
		getFramePrincipal().addNovoJogo(
				GeradorSudokuView.gerarNovoPanelSudoku(jogo));

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

	public static void atualizarTimerJogo() {
		getJogoAtual().atualizarTimer(1l);
	}

	public static long getTempoJogoAtual() {
		return getJogoAtual().getTimer();
	}

	public static void adicionarPalpiteUsuario(int x, int y, String novoValor) {
		if (getJogoAtual().addPalpite(x, y, Integer.parseInt(novoValor))) {
			getFramePrincipal().alterarValorBotao(x, y, novoValor);
			atualizarCorBotoes(x, y, novoValor);
			if (getJogoAtual().validarFimJogo()) {
				getFramePrincipal().finalizarJogo();
			}
		}
	}

	public static void removerPalpiteUsuario(int x, int y) {
		if (getJogoAtual().removerPalpite(x, y)) {
			getFramePrincipal().alterarValorBotao(x, y, "");
			atualizarCorBotoes(x, y,
					Integer.toString(JogoSudoku.POSICAO_INUTILIZAVEL));
		}
	}

	private static void atualizarCorBotoes(int x, int y, String novoValor) {
		Set<PosicaoSudoku> pontosLinhaColunaQuadrante = getJogoAtual()
				.getPontosInvalidosLinhaColunaQuadrante(x, y,
						Integer.parseInt(novoValor));
		for (PosicaoSudoku posicao : pontosLinhaColunaQuadrante) {
			int vlrX = new Double(posicao.getPonto().getX()).intValue();
			int vlrY = new Double(posicao.getPonto().getY()).intValue();
			if (posicao.isInvalido()) {
				getFramePrincipal().invalidarBotao(vlrX, vlrY);
			} else {
				getFramePrincipal().validarBotao(vlrX, vlrY);
			}
		}
	}

	public static void limparPanelPrincipal() {
		setJogoAtual(null);
		getFramePrincipal().limpar();
	}

	public static void pararTimer() {
		if (getJogoAtual() != null)
			getJogoAtual().pararTimer();
	}

	public static void voltarTimer() {
		if (getJogoAtual() != null)
			getJogoAtual().voltarTimer();
	}

	public static boolean isPermitidoPalpite(int x, int y) {
		return getJogoAtual().isPermitidoPalpite(x, y);
	}

}
