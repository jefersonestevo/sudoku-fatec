package br.com.fatec.ia.sudoku.view.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import br.com.fatec.ia.sudoku.view.ModalSelecaoNumero;
import br.com.fatec.ia.sudoku.view.SudokuHolder;

public class PreencherBotaoListener implements ActionListener {

	public void actionPerformed(ActionEvent e) {
		JButton botaoSelecionado = (JButton) e.getSource();
		System.out.println(botaoSelecionado.getName());

		// TODO - Retirar coordenadas do botão para selecao
		ModalSelecaoNumero modalSelecaoNumero = new ModalSelecaoNumero(
				SudokuHolder.getFramePrincipal(), true);
		modalSelecaoNumero.setVisible(true);

	}

}
