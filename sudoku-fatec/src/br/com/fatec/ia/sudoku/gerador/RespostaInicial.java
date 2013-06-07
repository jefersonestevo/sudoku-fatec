package br.com.fatec.ia.sudoku.gerador;


class RespostaInicial {
    private int[][] resposta = new int[Constantes.TAMANHO][Constantes.TAMANHO];
    private boolean troca[] = new boolean[Constantes.TAMANHO];

    public void get(int[][] ans) {
        Calculadora.copiarJogo(resposta, ans);
    }

    public void make() {
        for (int k = 1; k <= Constantes.TAMANHO; k++)
            for (int i = 0; i < Constantes.TAMANHO_BLOCO; i++)
                for (int j = 0; j < Constantes.TAMANHO_BLOCO; j++) {
                    resposta[Constantes.TAMANHO_BLOCO * j + (i + k) % Constantes.TAMANHO_BLOCO][Constantes.TAMANHO_BLOCO * i + (j + k + (k / Constantes.TAMANHO_BLOCO))
                        % Constantes.TAMANHO_BLOCO] = k;
                }

        for (int i = 0; i < Constantes.TAMANHO; i++)
            for (int j = 0; j < Constantes.TAMANHO; j++) {
                int r1 = i;
                int n1 = resposta[r1][j];
                int a = (int) (Math.random() * Constantes.TAMANHO_BLOCO);
                int r2 = Constantes.TAMANHO_BLOCO * (r1 / Constantes.TAMANHO_BLOCO) + a;
                int n2 = resposta[r2][j];
                if (n1 != n2) {
                    for (int k = 0; k < Constantes.TAMANHO; k++)
                        troca[k] = false;
                    troca[j] = true;

                    while (n1 != n2) {
                        int k = 0;
                        while (resposta[r1][k] != n2)
                            k++;
                        troca[k] = true;
                        n2 = resposta[r2][k];
                    }
                    for (int k = 0; k < Constantes.TAMANHO; k++)
                        if (troca[k] == true) {
                            int wk = resposta[r1][k];
                            resposta[r1][k] = resposta[r2][k];
                            resposta[r2][k] = wk;
                        }
                }
            }

        for (int i = 0; i < Constantes.TAMANHO; i++)
            for (int j = 0; j < Constantes.TAMANHO; j++) {
                int c1 = j;
                int n1 = resposta[i][c1];
                int a = (int) (Math.random() * Constantes.TAMANHO_BLOCO);
                int c2 = Constantes.TAMANHO_BLOCO * (c1 / Constantes.TAMANHO_BLOCO) + a;
                int n2 = resposta[i][c2];
                if (n1 != n2) {
                    for (int k = 0; k < Constantes.TAMANHO; k++)
                        troca[k] = false;
                    troca[i] = true;

                    while (n1 != n2) {
                        int k = 0;
                        while (resposta[k][c1] != n2)
                            k++;
                        troca[k] = true;
                        n2 = resposta[k][c2];
                    }
                    for (int k = 0; k < Constantes.TAMANHO; k++)
                        if (troca[k] == true) {
                            int wk = resposta[k][c1];
                            resposta[k][c1] = resposta[k][c2];
                            resposta[k][c2] = wk;
                        }
                }
            }
    }
}
