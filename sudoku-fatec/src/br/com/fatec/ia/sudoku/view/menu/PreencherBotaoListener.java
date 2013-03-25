package br.com.fatec.ia.sudoku.view.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import br.com.fatec.ia.sudoku.view.ModalSelecaoNumero;
import br.com.fatec.ia.sudoku.view.SudokuHolder;
import br.com.fatec.ia.sudoku.view.utils.SudokuConstants;

public class PreencherBotaoListener implements ActionListener {

	public void actionPerformed(ActionEvent e) {
		JButton botaoSelecionado = (JButton) e.getSource();

		String coordTotal = botaoSelecionado.getName().replaceAll(
				SudokuConstants.PREFIXO_BOTAO, "");
		int coordX = Integer.parseInt(coordTotal.substring(0, 1));
		int coordY = Integer.parseInt(coordTotal.substring(1, 2));
		
		ModalSelecaoNumero modalSelecaoNumero = new ModalSelecaoNumero(
				SudokuHolder.getFramePrincipal(), true, coordX, coordY);
		modalSelecaoNumero.setVisible(true);

	}

}
