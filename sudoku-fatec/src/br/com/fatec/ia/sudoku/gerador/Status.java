package br.com.fatec.ia.sudoku.gerador;


public class Status {

    private int[][] num = new int[Constantes.TAMANHO][Constantes.TAMANHO];

    private boolean[][][] cand = new boolean[Constantes.TAMANHO][Constantes.TAMANHO][Constantes.TAMANHO + 1];

    private boolean[][] xExist = new boolean[Constantes.TAMANHO][Constantes.TAMANHO + 1];
    private boolean[][] yExist = new boolean[Constantes.TAMANHO][Constantes.TAMANHO + 1];
    private boolean[][] zExist = new boolean[Constantes.TAMANHO][Constantes.TAMANHO + 1];

    private int[][] gCandCount = new int[Constantes.TAMANHO][Constantes.TAMANHO];

    private int[][] xCandCount = new int[Constantes.TAMANHO][Constantes.TAMANHO + 1];
    private int[][] yCandCount = new int[Constantes.TAMANHO][Constantes.TAMANHO + 1];
    private int[][] zCandCount = new int[Constantes.TAMANHO][Constantes.TAMANHO + 1];

    private boolean semResposta;

    private int contadorEspaco;

    public void setStatus(Status st) {
        semResposta = st.semResposta;
        contadorEspaco = st.contadorEspaco;

        for (int i = 0; i < Constantes.TAMANHO; ++i) {
            for (int j = 0; j < Constantes.TAMANHO; ++j) {
                num[i][j] = st.num[i][j];
                gCandCount[i][j] = st.gCandCount[i][j];

                for (int k = 1; k <= Constantes.TAMANHO; ++k) {
                    cand[i][j][k] = st.cand[i][j][k];
                }

            }
            for (int k = 1; k <= Constantes.TAMANHO; ++k) {
                xExist[i][k] = st.xExist[i][k];
                yExist[i][k] = st.yExist[i][k];
                zExist[i][k] = st.zExist[i][k];

                xCandCount[i][k] = st.xCandCount[i][k];
                yCandCount[i][k] = st.yCandCount[i][k];
                zCandCount[i][k] = st.zCandCount[i][k];
            }
        }
    }

    public void limpar(boolean _palpite[][]) {
        semResposta = false;
        contadorEspaco = 0;

        for (int i = 0; i < Constantes.TAMANHO; ++i) {
            for (int j = 0; j < Constantes.TAMANHO; ++j) {
                if (_palpite != null && !_palpite[i][j])
                    num[i][j] = 0;

                if (num[i][j] == 0)
                    contadorEspaco++;

                gCandCount[i][j] = Constantes.TAMANHO;

                for (int k = 1; k <= Constantes.TAMANHO; ++k)
                    cand[i][j][k] = true;
            }

            for (int k = 1; k <= Constantes.TAMANHO; ++k) {
                xCandCount[i][k] = yCandCount[i][k] = zCandCount[i][k] = Constantes.TAMANHO;
                xExist[i][k] = yExist[i][k] = zExist[i][k] = false;
            }
        }
    }

    public boolean setarNumero(int p, int q, int r) {
        if (num[p][q] == 0) {
            if (xExist[p][r] || yExist[q][r] || zExist[Calculadora.zona(p, q)][r])
                semResposta = true;
            num[p][q] = r;
            --contadorEspaco;
            xExist[p][r] = true;
            yExist[q][r] = true;
            zExist[Calculadora.zona(p, q)][r] = true;

            return true;
        }
        else {
            return false;
        }
    }

    public boolean deletarCand(int p, int q, int r) {
        if (cand[p][q][r]) {
            cand[p][q][r] = false;
            if (--gCandCount[p][q] == 0)
                semResposta = true;
            if (--xCandCount[p][r] == 0)
                semResposta = true;
            if (--yCandCount[q][r] == 0)
                semResposta = true;
            if (--zCandCount[Calculadora.zona(p, q)][r] == 0)
                semResposta = true;

            return true;
        }
        else {
            return false;
        }
    }

    public boolean isCand(int x, int y, int r) {
        return cand[x][y][r];
    }

    public int[][] getNum() {
        return num;
    }

    public int getNum(int x, int y) {
        return num[x][y];
    }

    public void setNum(int x, int y, int r) {
        num[x][y] = r;
    }

    public int getGCandCount(int x, int y) {
        return gCandCount[x][y];
    }

    public int getXCandCount(int x, int y, int r) {
        return xCandCount[x][r];
    }

    public int getXCandCount(int x, int r) {
        return xCandCount[x][r];
    }

    public int getYCandCount(int x, int y, int r) {
        return yCandCount[y][r];
    }

    public int getYCandCount(int y, int r) {
        return yCandCount[y][r];
    }

    public int getZCandCount(int x, int y, int r) {
        return zCandCount[Calculadora.zona(x, y)][r];
    }

    public int getZCandCount(int z, int r) {
        return zCandCount[z][r];
    }

    public boolean isXExist(int x, int y, int r) {
        return xExist[x][r];
    }

    public boolean isXExist(int x, int r) {
        return xExist[x][r];
    }

    public boolean isYExist(int x, int y, int r) {
        return yExist[y][r];
    }

    public boolean isYExist(int y, int r) {
        return yExist[y][r];
    }

    public boolean isZExist(int x, int y, int r) {
        return zExist[Calculadora.zona(x, y)][r];
    }

    public boolean isZExist(int z, int r) {
        return zExist[z][r];
    }

    public boolean naoPossuiResposta() {
        return semResposta;
    }

    public int getContadorEspaco() {
        return contadorEspaco;
    }

}
