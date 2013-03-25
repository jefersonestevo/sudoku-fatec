package br.com.fatec.ia.sudoku.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class ModalSelecaoNumero extends JDialog {

	private static final long serialVersionUID = 8421514990168941803L;

	public ModalSelecaoNumero(JFrame parent, boolean modal) {
		super(parent, modal);
		Dimension dimensionParentFrame = parent.getSize();
		setSize(new Dimension((parent == null) ? 300
				: dimensionParentFrame.width / 2, 75));
		Dimension dimensionDialog = getSize();
		int x = parent.getX()
				+ ((dimensionParentFrame.width - dimensionDialog.width) / 2);
		setLocation(x, parent.getY() + parent.getInsets().top + 100);
		setUndecorated(true);
		setModal(modal);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// TODO - Gerar modal com os numeros de 1 a 9 para selecao pelo usuario
		JButton buttonClose = new JButton("Close");
		buttonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		getContentPane().add(buttonClose, BorderLayout.CENTER);
	}

}
