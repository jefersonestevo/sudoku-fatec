package br.com.fatec.ia.sudoku;

import java.awt.Point;
import java.io.Serializable;

public class PosicaoSudoku implements Serializable {

	private static final long serialVersionUID = 7294291984803767274L;

	private Point ponto;
	private Boolean invalido;
	private Integer valor;

	public PosicaoSudoku(Point ponto) {
		super();
		this.ponto = ponto;
	}
	
	public PosicaoSudoku(Point ponto, Boolean invalido, Integer valor) {
		super();
		this.ponto = ponto;
		this.invalido = invalido;
		this.valor = valor;
	}

	public Point getPonto() {
		return ponto;
	}

	public void setPonto(Point ponto) {
		this.ponto = ponto;
	}

	public Boolean isInvalido() {
		return invalido;
	}

	public void setInvalido(Boolean invalido) {
		this.invalido = invalido;
	}

	public Integer getValor() {
		return valor;
	}

	public void setValor(Integer valor) {
		this.valor = valor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ponto == null) ? 0 : ponto.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PosicaoSudoku other = (PosicaoSudoku) obj;
		if (ponto == null) {
			if (other.ponto != null)
				return false;
		} else if (!ponto.equals(other.ponto))
			return false;
		return true;
	}
}
