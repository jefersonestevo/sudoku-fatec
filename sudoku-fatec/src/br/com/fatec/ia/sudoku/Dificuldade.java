package br.com.fatec.ia.sudoku;

import java.io.Serializable;

public enum Dificuldade implements Serializable {
	FACIL(50), //
	INTERMEDIARIO(40), //
	DIFICIL(30);

	private int quantidadeCasasExibidas;

	private Dificuldade(int quantidadeCasasExibidas) {
		this.quantidadeCasasExibidas = quantidadeCasasExibidas;
	}

	public int getQuantidadeCasasExibidas() {
		return quantidadeCasasExibidas;
	}

}
