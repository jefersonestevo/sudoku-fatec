package br.com.fatec.ia.sudoku.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import br.com.fatec.ia.sudoku.view.menu.SelecionarNumeroListener;
import br.com.fatec.ia.sudoku.view.utils.Messages;
import br.com.fatec.ia.sudoku.view.utils.SudokuConstants;

public class ModalSelecaoNumero extends JDialog {

	private static final long serialVersionUID = 8421514990168941803L;

	private int coordX;
	private int coordY;

	public ModalSelecaoNumero(JFrame parent, boolean modal, int coordX,
			int coordY) {
		super(parent, modal);
		Dimension dimensionParentFrame = parent.getSize();
		setSize(180, 150);
		Dimension dimensionDialog = getSize();
		int x = parent.getX()
				+ ((dimensionParentFrame.width - dimensionDialog.width) / 2);
		setLocation(x, parent.getY() + parent.getInsets().top + 150);
		setResizable(false);
		setModal(modal);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		this.coordX = coordX;
		this.coordY = coordY;
		adicionarValoresInternos();
	}

	private void adicionarValoresInternos() {
		SelecionarNumeroListener listener = new SelecionarNumeroListener(
				coordX, coordY, this);

		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setLayout(new BoxLayout(panelPrincipal,
				BoxLayout.PAGE_AXIS));

		// Adicionar botoes numericos
		JPanel panelSelecaoNumeros = new JPanel(new GridLayout(3, 3));
		for (int i = 1; i <= 9; i++) {
			JButton botao = new JButton(Integer.toString(i));
			botao.setName(SudokuConstants.PREFIXO_BOTAO + i);
			botao.addActionListener(listener);
			panelSelecaoNumeros.add(botao);
		}
		panelPrincipal.add(panelSelecaoNumeros);

		// Adicinando Botoes inferiores
		JPanel panelBotoesInferiores = new JPanel(new FlowLayout(
				FlowLayout.CENTER));
		JButton botaoLimpar = new JButton(Messages.getMessage("Botao_Limpar"));
		botaoLimpar.addActionListener(listener);
		botaoLimpar.setName(SudokuConstants.BOTAO_LIMPAR);
		panelBotoesInferiores.add(botaoLimpar);

		JButton botaoCancelar = new JButton(
				Messages.getMessage("Botao_Cancelar"));
		botaoCancelar.addActionListener(listener);
		botaoCancelar.setName(SudokuConstants.BOTAO_CANCELAR);
		panelBotoesInferiores.add(botaoCancelar);

		panelPrincipal.add(panelBotoesInferiores);
		getContentPane().add(panelPrincipal, BorderLayout.CENTER);
	}
}
