package br.com.fatec.ia.sudoku;

import java.awt.Point;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

public class JogoSudoku implements Serializable {

	private static final long serialVersionUID = 469302297056158448L;

	public static final int POSICAO_EM_BRANCO = -1;
	public static final int POSICAO_NAO_JOGADA = -2;
	public static final int POSICAO_INUTILIZAVEL = -3;
	private static final int QNTD_CASAS_QUADRANTE = 3;
	private static final int QNTD_CASAS = 9;
	private static final int QNTD_TOTAL_CASAS = QNTD_CASAS * QNTD_CASAS;

	private int[][] jogoCompleto = new int[QNTD_CASAS][QNTD_CASAS];
	private int[][] jogoReal = new int[QNTD_CASAS][QNTD_CASAS];
	private int[][] palpitesUsuario = new int[QNTD_CASAS][QNTD_CASAS];
	private boolean[][] palpitePossivel = new boolean[QNTD_CASAS][QNTD_CASAS];
	private Dificuldade dificuldade = Dificuldade.FACIL;
	private long segundosJogo;
	private boolean jogoFinalizado = false;
	// private Set<PosicaoSudoku> pontosInvalidos = new
	// HashSet<PosicaoSudoku>();

	private transient boolean timerParado = false;

	public JogoSudoku(int[][] jogoCompleto, Dificuldade dificuldade) {
		super();
		if (dificuldade != null)
			this.dificuldade = dificuldade;

		this.jogoCompleto = new int[QNTD_CASAS][QNTD_CASAS];
		System.arraycopy(jogoCompleto, 0, this.jogoCompleto, 0,
				jogoCompleto.length);
		preencherQuadrantes();
		montarJogoReal();
		this.segundosJogo = 0l;
	}

	private void preencherQuadrantes() {
		for (int x = 0; x < jogoCompleto.length; x++) {
			for (int y = 0; y < jogoCompleto[x].length; y++) {

			}
		}
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

	public int[][] getPalpitesUsuario() {
		int[][] novoArray = new int[QNTD_CASAS][QNTD_CASAS];
		System.arraycopy(palpitesUsuario, 0, novoArray, 0,
				palpitesUsuario.length);
		return novoArray;
	}

	public boolean validarFimJogo() {
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

		if (isFimJogo)
			jogoFinalizado = true;

		return isFimJogo;
	}

	public Dificuldade getDificuldade() {
		return dificuldade;
	}

	public void atualizarTimer(long valor) {
		if (!timerParado && !jogoFinalizado)
			this.segundosJogo += valor;
	}

	public long getTimer() {
		return segundosJogo;
	}

	public void pararTimer() {
		timerParado = true;
	}

	public void voltarTimer() {
		timerParado = false;
	}

	public boolean isJogoFinalizado() {
		return jogoFinalizado;
	}

	/**
	 * Percorre as linhas, colunas e o quadrante do ponto especificado e retorna
	 * todos os pontos destes itens e se estes estão ou não inválidos.
	 * 
	 * @param x
	 * @param y
	 * @param valor
	 * @return Retorna um map com todos os pontos da linha, coluna e quadrante<br/>
	 *         Se o valor do map for true, esta posição está inválida. <br/>
	 *         Se o valor do map for false, esta posição continua válida.
	 */
	public Set<PosicaoSudoku> getPontosInvalidosLinhaColunaQuadrante(int x,
			int y, int valor) {
		Set<PosicaoSudoku> posicoes = new HashSet<PosicaoSudoku>();
		for (int i = 0; i < jogoCompleto.length; i++) {
			Set<PosicaoSudoku> posicoesInvalidas = new HashSet<PosicaoSudoku>();
			for (int j = 0; j < jogoCompleto[i].length; j++) {
				if (!isPermitidoPalpite(i, j) || !isCampoJogado(i, j))
					continue;
				PosicaoSudoku posicaoAtual = new PosicaoSudoku(new Point(i, j),
						false, valor);

				// Adiciona todos os valores invalidos
				posicoesInvalidas.addAll(getValoresInvalidosLinhaVertical(i, j,
						palpitesUsuario[i][j]));
				posicoesInvalidas.addAll(getValoresInvalidosLinhaHorizontal(i,
						j, palpitesUsuario[i][j]));
				posicoesInvalidas.addAll(getValoresInvalidosQuadrante(i, j,
						palpitesUsuario[i][j]));

				if (CollectionUtils.isNotEmpty(posicoesInvalidas)) {
					posicaoAtual.setInvalido(true);
					posicoesInvalidas.add(posicaoAtual);
				}

				posicoes.addAll(posicoesInvalidas);
			}
		}

		// Percorre todos os campos e adiciona os valores que não são invalidos
		for (int i = 0; i < jogoCompleto.length; i++) {
			for (int j = 0; j < jogoCompleto[i].length; j++) {
				int valorCampo = palpitesUsuario[i][j];
				if (!isPermitidoPalpite(i, j))
					valorCampo = jogoReal[i][j];
				PosicaoSudoku posicao = new PosicaoSudoku(new Point(i, j),
						false, valorCampo);

				if (!posicoes.contains(posicao)) {
					posicoes.add(posicao);
				}
			}
		}

		return posicoes;
	}

	private Set<PosicaoSudoku> getValoresInvalidosLinhaVertical(int x, int y,
			int valor) {
		Map<Integer, PosicaoSudoku> setValoresInvalidos = new HashMap<Integer, PosicaoSudoku>();

		for (int i = 0; i < jogoCompleto.length; i++) {
			if (i == y)
				continue;
			int valorColuna = palpitesUsuario[x][i];
			if (!isPermitidoPalpite(x, i))
				valorColuna = jogoReal[x][i];

			if (valor == valorColuna) {
				PosicaoSudoku posicaoInvalida = new PosicaoSudoku(new Point(x,
						i), true, valorColuna);
				setValoresInvalidos.put(valorColuna, posicaoInvalida);
			}
		}

		return new HashSet<PosicaoSudoku>(setValoresInvalidos.values());
	}

	private Set<PosicaoSudoku> getValoresInvalidosLinhaHorizontal(int x, int y,
			int valor) {
		Set<PosicaoSudoku> setValoresInvalidos = new HashSet<PosicaoSudoku>();

		for (int i = 0; i < jogoCompleto.length; i++) {
			if (i == x)
				continue;
			int valorLinha = palpitesUsuario[i][y];
			if (!isPermitidoPalpite(i, y))
				valorLinha = jogoReal[i][y];

			if (valor == valorLinha) {
				PosicaoSudoku posicaoInvalida = new PosicaoSudoku(new Point(i,
						y), true, valorLinha);
				setValoresInvalidos.add(posicaoInvalida);
			}

		}

		return setValoresInvalidos;
	}

	private Set<PosicaoSudoku> getValoresInvalidosQuadrante(int x, int y,
			int valor) {
		Set<PosicaoSudoku> setValoresInvalidos = new HashSet<PosicaoSudoku>();

		int quadXInicio = (x / QNTD_CASAS_QUADRANTE) * QNTD_CASAS_QUADRANTE;
		int quadYInicio = (y / QNTD_CASAS_QUADRANTE) * QNTD_CASAS_QUADRANTE;
		for (int i = quadXInicio; i < (quadXInicio + QNTD_CASAS_QUADRANTE); i++) {
			for (int j = quadYInicio; j < (quadYInicio + QNTD_CASAS_QUADRANTE); j++) {
				if (i == x && j == y)
					continue;
				int palpiteQuadrante = palpitesUsuario[i][j];
				if (!isPermitidoPalpite(i, j))
					palpiteQuadrante = jogoReal[i][j];

				if (valor == palpiteQuadrante) {
					PosicaoSudoku posicaoInvalida = new PosicaoSudoku(
							new Point(i, j), true, palpiteQuadrante);
					setValoresInvalidos.add(posicaoInvalida);
				}

			}
		}

		return setValoresInvalidos;
	}

	private boolean isCampoJogado(int x, int y) {
		return palpitesUsuario[x][y] != POSICAO_NAO_JOGADA;
	}
}
