package br.com.fatec.ia.sudoku.gerador;


public class VerificadorResultado {
    Status estado = new Status();

    public Status resolver(Status matriz) {

        estado.setStatus(matriz);

        for (int x = 0; x < Constantes.TAMANHO; ++x)
            for (int y = 0; y < Constantes.TAMANHO; ++y)
                deletarPontoCandidato(x, y);

        return estado;
    }

    public Status resolverLocal(Status matrix, int p, int q) {
        estado.setStatus(matrix);
        deletarPontoCandidato(p, q);

        return estado;
    }

    private void deletarPontoCandidato(int p, int q) {

        int r = estado.getNum(p, q);
        if (r <= 0)
            return;
        for (int k = 1; k <= Constantes.TAMANHO; ++k) {
            if (k != r) {
                if (estado.deletarCand(p, q, k)) {
                    setarPontoUnico(p, q, k);
                }
            }
        }

        for (int x = 0; x < Constantes.TAMANHO; ++x) {
            if (x != p) {
                if (estado.deletarCand(x, q, r)) {
                    setarPontoUnico(x, q, r);
                }
            }
        }

        for (int y = 0; y < Constantes.TAMANHO; ++y) {
            if (y != q) {
                if (estado.deletarCand(p, y, r)) {
                    setarPontoUnico(p, y, r);
                }
            }
        }

        int baseX = (p / Constantes.TAMANHO_BLOCO) * Constantes.TAMANHO_BLOCO;
        int baseY = (q / Constantes.TAMANHO_BLOCO) * Constantes.TAMANHO_BLOCO;
        for (int x = baseX; x < baseX + Constantes.TAMANHO_BLOCO; ++x)
            for (int y = baseY; y < baseY + Constantes.TAMANHO_BLOCO; ++y) {
                if ((x != p) || (y != q)) {
                    if (estado.deletarCand(x, y, r)) {
                        setarPontoUnico(x, y, r);
                    }
                }
            }
    }

    private void setarXUnico(int p, int r) {
        int j = 0;
        while (!estado.isCand(p, j, r))
            j++;
        if (estado.setarNumero(p, j, r))
            deletarPontoCandidato(p, j);
    }

    private void setarYUnico(int q, int r) {
        int i = 0;
        while (!estado.isCand(i, q, r))
            i++;
        if (estado.setarNumero(i, q, r))
            deletarPontoCandidato(i, q);
    }

    private void setarZUnico(int i, int r) {

        int j = 0;

        while (!estado.isCand(Calculadora.ztox(i, j), Calculadora.ztoy(i, j), r))
            ++j;

        if (estado.setarNumero(Calculadora.ztox(i, j), Calculadora.ztoy(i, j), r))
            deletarPontoCandidato(Calculadora.ztox(i, j), Calculadora.ztoy(i, j));
    }

    private void setarGUnico(int x, int y) {
        int k = 1;
        while (!estado.isCand(x, y, k))
            ++k;
        if (estado.setarNumero(x, y, k))
            deletarPontoCandidato(x, y);
    }

    private void setarPontoUnico(int p, int q, int r) {
        if (estado.naoPossuiResposta())
            return;

        if (estado.getZCandCount(p, q, r) == 1)
            if (!estado.isZExist(p, q, r))
                setarZUnico(Calculadora.zona(p, q), r);

        if (estado.getXCandCount(p, q, r) == 1)
            if (!estado.isXExist(p, q, r))
                setarXUnico(p, r);

        if (estado.getYCandCount(p, q, r) == 1)
            if (!estado.isYExist(p, q, r))
                setarYUnico(q, r);

        if (estado.getGCandCount(p, q) == 1)
            if (estado.getNum(p, q) == 0)
                setarGUnico(p, q);

    }
}
