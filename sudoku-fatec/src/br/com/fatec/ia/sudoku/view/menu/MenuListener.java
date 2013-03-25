package br.com.fatec.ia.sudoku.view.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import br.com.fatec.ia.sudoku.Dificuldade;
import br.com.fatec.ia.sudoku.view.SudokuHolder;
import br.com.fatec.ia.sudoku.view.utils.Messages;

public class MenuListener implements ActionListener {

	public static final String MENU_NOVO = "menuNovo";
	public static final String MENU_SAIR = "menuSair";

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JMenuItem) {
			JMenuItem menu = ((JMenuItem) e.getSource());

			if (MENU_NOVO.equals(menu.getName())) {
				if (JOptionPane
						.showConfirmDialog(
								null,
								Messages.getMessage("Deseja_Realmente_Criar_Novo_Jogo"),
								Messages.getMessage("Confirmar"),
								JOptionPane.OK_CANCEL_OPTION) == 0) {
					SudokuHolder.gerarNovoJogo(Dificuldade.INTERMEDIARIO);
				}
			} else if (MENU_SAIR.equals(menu.getName())) {
				System.exit(0);
			}

		}
	}
}
