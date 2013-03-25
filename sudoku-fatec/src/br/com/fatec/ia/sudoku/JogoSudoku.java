package br.com.fatec.ia.sudoku;


public class JogoSudoku {

	public static final int POSICAO_EM_BRANCO = -1;
	public static final int POSICAO_NAO_JOGADA = -2;
	private static final int QNTD_CASAS = 9;
	private static final int QNTD_TOTAL_CASAS = QNTD_CASAS * QNTD_CASAS;

	private int[][] jogoCompleto = new int[QNTD_CASAS][QNTD_CASAS];
	private int[][] jogoReal = new int[QNTD_CASAS][QNTD_CASAS];
	private int[][] palpitesUsuario = new int[QNTD_CASAS][QNTD_CASAS];
	private boolean[][] palpitePossivel = new boolean[QNTD_CASAS][QNTD_CASAS];
	private Dificuldade dificuldade = Dificuldade.FACIL;

	public JogoSudoku(int[][] jogoCompleto, Dificuldade dificuldade) {
		super();
		if (dificuldade != null)
			this.dificuldade = dificuldade;

		this.jogoCompleto = new int[QNTD_CASAS][QNTD_CASAS];
		System.arraycopy(jogoCompleto, 0, this.jogoCompleto, 0,
				jogoCompleto.length);
		montarJogoReal();
	}

	private void montarJogoReal() {
		this.jogoReal = new int[QNTD_CASAS][QNTD_CASAS];
		for (int x = 0; x < jogoCompleto.length; x++) {
			for (int y = 0; y < jogoCompleto[x].length; y++) {
				jogoReal[x][y] = jogoCompleto[x][y];
			}
		}

		for (int i = 0; i < (QNTD_TOTAL_CASAS - dificuldade
				.getQuantidadeCasasExibidas()); i++) {
			gerarPosicaoEmBranco(this.jogoReal);
		}

		for (int x = 0; x < jogoReal.length; x++) {
			for (int y = 0; y < jogoReal[x].length; y++) {
				if (jogoReal[x][y] != POSICAO_EM_BRANCO) {
					palpitePossivel[x][y] = false;
					palpitesUsuario[x][y] = POSICAO_EM_BRANCO;
				} else {
					palpitePossivel[x][y] = true;
					palpitesUsuario[x][y] = POSICAO_NAO_JOGADA;
				}
			}
		}
	}

	private void gerarPosicaoEmBranco(int[][] jogo) {
		int valorX = new Double(Math.random() * QNTD_CASAS).intValue();
		int valorY = new Double(Math.random() * QNTD_CASAS).intValue();

		// Se esta casa ja estiver com posicao em branco, verifica uma outra
		if (jogo[valorX][valorY] == POSICAO_EM_BRANCO) {
			gerarPosicaoEmBranco(jogo);
		} else {
			jogo[valorX][valorY] = POSICAO_EM_BRANCO;
		}
	}

	public boolean addPalpite(int x, int y, int valor) {
		if (!isPermitidoPalpite(x, y))
			return false;
		palpitesUsuario[x][y] = valor;
		return true;
	}

	public boolean removerPalpite(int x, int y) {
		if (!isPermitidoPalpite(x, y))
			return false;
		palpitesUsuario[x][y] = POSICAO_NAO_JOGADA;
		return true;
	}

	public boolean isPermitidoPalpite(int x, int y) {
		return palpitePossivel[x][y];
	}

	public int[][] getJogoCompleto() {
		int[][] novoArray = new int[QNTD_CASAS][QNTD_CASAS];
		System.arraycopy(jogoCompleto, 0, novoArray, 0, jogoCompleto.length);
		return novoArray;
	}

	public int[][] getJogoReal() {
		int[][] novoArray = new int[QNTD_CASAS][QNTD_CASAS];
		System.arraycopy(jogoReal, 0, novoArray, 0, jogoReal.length);
		return novoArray;
	}

	public boolean isFimJogo() {
		boolean isFimJogo = false;

		testeJogoCompleto: for (int x = 0; x < jogoCompleto.length; x++) {
			for (int y = 0; y < jogoCompleto[x].length; y++) {
				// Se for possivel o palpite nesta casa
				if (palpitePossivel[x][y]) {

					// Se nao foi jogado nesta casa ainda, nao é fim de jogo
					if (palpitesUsuario[x][y] == POSICAO_NAO_JOGADA) {
						isFimJogo = false;
						break testeJogoCompleto;
					}

					// Testa se o palpite do usuario é o mesmo do jogo real. Se
					// for, continua no teste
					if (palpitesUsuario[x][y] == jogoCompleto[x][y]) {
						isFimJogo = true;
					} else {
						// Se o palpite do usuario nao for o mesmo do jogo
						// completo, nao e fim de jogo
						isFimJogo = false;
						break testeJogoCompleto;
					}

				}
			}
		}

		return isFimJogo;
	}
}
