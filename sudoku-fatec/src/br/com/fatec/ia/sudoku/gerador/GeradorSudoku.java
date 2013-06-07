package br.com.fatec.ia.sudoku.gerador;

import br.com.fatec.ia.sudoku.Dificuldade;
import br.com.fatec.ia.sudoku.JogoSudoku;

public class GeradorSudoku {

    public static JogoSudoku gerarJogoSudoku(Dificuldade dificuldade) {
        boolean[][] casasParaGeracao = new boolean[9][9];

        for (int i = 0; i < casasParaGeracao.length; i++) {
            for (int j = 0; j < casasParaGeracao[i].length; j++) {
                casasParaGeracao[i][j] = true;
            }
        }

        GeradorSudoku gerador = new GeradorSudoku();
        ProblemaSudoku problem = gerador.gerar(casasParaGeracao);

        int[][] jogo = new int[9][9];
        problem.getSolucao(jogo);

        return new JogoSudoku(jogo, dificuldade);
    }

    private ProblemaSudoku problem = new ProblemaSudoku();
    private Status matriz = new Status();
    private Status matriz2 = new Status();
    private Status matriz3 = new Status();

    private RespostaInicial respostaInicial = new RespostaInicial();

    private int memo[] = new int[Constantes.TAMANHO];
    private int espaco;

    private VerificadorResultado solver = new VerificadorResultado();

    public ProblemaSudoku gerar(boolean[][] hint) {

        problem.setPalpites(hint);

        int[][] num = matriz.getNum();

        respostaInicial.make();
        respostaInicial.get(num);

        matriz.limpar(hint);

        matriz = solver.resolver(matriz);
        espaco = matriz.getContadorEspaco();

        if (espaco > 0)
            gerarNovo();

        matriz.limpar(hint);
        matriz = solver.resolver(matriz);

        if (matriz.getContadorEspaco() == 0 && !matriz.naoPossuiResposta()) {
            problem.setUnico(true);
        }
        else {
            problem.setUnico(false);
        }

        Status state = problem.getEstadoAtual();
        state.setStatus(matriz);
        problem.setSolucao(matriz.getNum());

        matriz.limpar(hint);
        problem.setQuestao(matriz.getNum());

        return problem;
    }

    private void gerarNovo() {
        int base, base2;

        base = base2 = 0;

        for (;;) {
            if (trocarPalpite(base))
                base2 = base;
            base = ++base % Constantes.TAMANHO;
            if (base == base2 || matriz.getContadorEspaco() == 0)
                break;
        }
    }

    private boolean trocarPalpite(int q) {
        boolean ok = false;

        for (int i = 0; i < Constantes.TAMANHO; ++i) {
            memo[i] = problem.isPalpite(i, q) ? matriz.getNum(i, q) : 0;
            matriz.setNum(i, q, 0);
        }

        matriz.limpar(problem.getPalpites());
        matriz = solver.resolver(matriz);
        matriz2.setStatus(matriz);

        for (int i = 0; i < Constantes.TAMANHO; ++i) {
            if (!problem.isPalpite(i, q))
                continue;

            matriz.setStatus(matriz2);
            for (int j = 0; j < Constantes.TAMANHO; ++j)
                if (memo[j] > 0 && j != i) {
                    matriz.setNum(j, q, memo[j]);
                    matriz = solver.resolverLocal(matriz, j, q);
                }
            matriz3.setStatus(matriz);
            boolean abaixo = false;

            int mat = memo[i] % Constantes.TAMANHO + 1;
            while (!matriz.isCand(i, q, mat)) {
                mat = mat % Constantes.TAMANHO + 1;
            }

            matriz.setarNumero(i, q, mat);
            matriz = solver.resolverLocal(matriz, i, q);

            while (mat != memo[i] && matriz.getContadorEspaco() > 0) {
                if (espaco > matriz.getContadorEspaco() && !matriz.naoPossuiResposta()) {
                    abaixo = true;
                    ok = true;
                    espaco = matriz.getContadorEspaco();
                    memo[i] = mat;
                    break;
                }

                matriz.setStatus(matriz3);
                mat = mat % Constantes.TAMANHO + 1;
                while (!matriz.isCand(i, q, mat)) {
                    mat = mat % Constantes.TAMANHO + 1;
                }
                matriz.setarNumero(i, q, mat);
                matriz = solver.resolverLocal(matriz, i, q);

            }

            if (matriz.getContadorEspaco() == 0 && !matriz.naoPossuiResposta()) {
                espaco = 0;
                break;
            }
            if (abaixo) {
                i = 0;
                if (espaco == 0)
                    break;
            }
        }

        return ok;
    }


}
