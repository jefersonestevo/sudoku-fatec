package br.com.fatec.ia.sudoku.utils;

import java.util.Random;



public class Calculadora {
    public static int zona(int p, int q) {
        return (p / Constantes.TAMANHO_BLOCO) * Constantes.TAMANHO_BLOCO + (q / Constantes.TAMANHO_BLOCO);
    }

    public static int area(int p, int q) {
        return (p % Constantes.TAMANHO_BLOCO) * Constantes.TAMANHO_BLOCO + (q % Constantes.TAMANHO_BLOCO);
    }

    public static int ztox(int i, int j) {
        return (i / Constantes.TAMANHO_BLOCO) * Constantes.TAMANHO_BLOCO + j / Constantes.TAMANHO_BLOCO;
    }

    public static int ztoy(int i, int j) {
        return (i % Constantes.TAMANHO_BLOCO) * Constantes.TAMANHO_BLOCO + j % Constantes.TAMANHO_BLOCO;
    }

    public static void copiarJogo(int[][] frombd, int[][] tobd) {
        for (int i = 0; i < Constantes.TAMANHO; ++i)
            for (int j = 0; j < Constantes.TAMANHO; ++j)
                tobd[i][j] = frombd[i][j];
    }

    private static Random random = new Random();

    public static void srand(long seed) {
        random = new Random(seed);
    }

    public static double random() {
        return random.nextDouble();
    }

    public static int randomInt(int n) {
        return random.nextInt(n);
    }
}
