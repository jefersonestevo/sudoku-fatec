package br.com.fatec.ia.sudoku.gerador;


public class ProblemaSudoku {
    private Status estadoAtual = new Status();

    private boolean unico;

    private int[][] solucao = new int[Constantes.TAMANHO][Constantes.TAMANHO];
    private int[][] questao = new int[Constantes.TAMANHO][Constantes.TAMANHO];

    private boolean[][] palpite = new boolean[Constantes.TAMANHO][Constantes.TAMANHO];

    public void resetar() {
        estadoAtual.limpar(palpite);
    }

    public Status getEstadoAtual() {
        return estadoAtual;
    }

    public boolean isUnico() {
        return unico;
    }

    public void setUnico(boolean uni) {
        unico = uni;
    }

    public boolean isPalpite(int x, int y) {
        return palpite[x][y];
    }

    public boolean[][] getPalpites() {
        return palpite;
    }

    public void setPalpites(boolean[][] _hint) {
        copiarPalpites(_hint, palpite);
    }

    private void copiarPalpites(boolean[][] palpiteAnterior, boolean[][] palpiteNovo) {
        for (int i = 0; i < Constantes.TAMANHO; ++i)
            for (int j = 0; j < Constantes.TAMANHO; ++j)
                palpiteNovo[i][j] = palpiteAnterior[i][j];
    }

    public int[][] getSolucao() {
        return solucao;
    }

    public void getSolucao(int[][] sol) {
        Calculadora.copiarJogo(solucao, sol);
    }

    public void setSolucao(int[][] sol) {
        Calculadora.copiarJogo(sol, solucao);
    }

    public int[][] getQuestao() {
        return questao;
    }

    public void setQuestao(int[][] que) {
        Calculadora.copiarJogo(que, questao);
    }

}
