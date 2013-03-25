package br.com.fatec.ia.sudoku.view;

import java.util.Locale;
import java.util.ResourceBundle;

public class Messages {

	private static Locale locale = new Locale("pt", "BR");

	public static String getMessage(String chave) {
		ResourceBundle rb = ResourceBundle
				.getBundle("MessageResources", locale);
		return rb.getString(chave);
	}

	public static void setLocale(Locale locale) {
		Messages.locale = locale;
	}

}
