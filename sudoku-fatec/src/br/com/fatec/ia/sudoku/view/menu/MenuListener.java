package br.com.fatec.ia.sudoku.view.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;

import br.com.fatec.ia.sudoku.Dificuldade;
import br.com.fatec.ia.sudoku.utils.SaveUtils;
import br.com.fatec.ia.sudoku.view.SudokuHolder;
import br.com.fatec.ia.sudoku.view.utils.Messages;

public class MenuListener implements ActionListener {

	public static final String MENU_NOVO = "menuNovo";
	public static final String MENU_SAIR = "menuSair";
	public static final String MENU_SALVAR = "menuSalvar";
	public static final String MENU_CARREGAR = "menuCarregar";

	public MenuListener() {

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JMenuItem) {
			JMenuItem menu = ((JMenuItem) e.getSource());

			if (MENU_NOVO.equals(menu.getName())) {
				Dificuldade dificuldade = getDificuldadeNovoJogo();
				if (dificuldade != null) {
					SudokuHolder.gerarNovoJogo(dificuldade);
				}
			} else if (MENU_SALVAR.equals(menu.getName())) {
				SudokuHolder.pararTimer();
				if (JOptionPane.showConfirmDialog(null,
						Messages.getMessage("Deseja_Realmente_Salvar_Jogo"),
						Messages.getMessage("Confirmar"),
						JOptionPane.OK_CANCEL_OPTION) == 0) {
					SaveUtils.salvarJogo();
					SudokuHolder.limparPanelPrincipal();
				}
				SudokuHolder.voltarTimer();
			} else if (MENU_CARREGAR.equals(menu.getName())) {
				SudokuHolder.pararTimer();
				if (JOptionPane.showConfirmDialog(null,
						Messages.getMessage("Deseja_Realmente_Carregar_Jogo"),
						Messages.getMessage("Confirmar"),
						JOptionPane.OK_CANCEL_OPTION) == 0) {
					SaveUtils.carregarJogo();
				}
				SudokuHolder.voltarTimer();
			} else if (MENU_SAIR.equals(menu.getName())) {
				System.exit(0);
			}

		}
	}

	private Dificuldade getDificuldadeNovoJogo() {
		Object[] dificuldades = { Messages.getMessage("Facil"),
				Messages.getMessage("Intermediario"),
				Messages.getMessage("Dificil") };

		String dif = (String) JOptionPane.showInputDialog(null,
				Messages.getMessage("Deseja_Realmente_Criar_Novo_Jogo"),
				Messages.getMessage("Confirmar"), JOptionPane.PLAIN_MESSAGE,
				null, dificuldades, Messages.getMessage("Intermediario"));

		if (StringUtils.isBlank(dif))
			return null;

		if (Messages.getMessage("Facil").equals(dif)) {
			return Dificuldade.FACIL;
		} else if (Messages.getMessage("Dificil").equals(dif)) {
			return Dificuldade.DIFICIL;
		} else if (Messages.getMessage("Intermediario").equals(dif)) {
			return Dificuldade.INTERMEDIARIO;
		}

		return null;
	}
}
