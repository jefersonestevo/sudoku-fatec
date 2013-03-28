package br.com.fatec.ia.sudoku.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import br.com.fatec.ia.sudoku.JogoSudoku;
import br.com.fatec.ia.sudoku.view.SudokuHolder;

public class SaveUtils {

	private static final String ARQUIVO_SAVE = "savegame.sudoku";

	public static boolean existeJogoSalvo() {
		File f = new File(ARQUIVO_SAVE);
		return f.exists();
	}

	public static void salvarJogo() {
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(ARQUIVO_SAVE);
			out = new ObjectOutputStream(fos);
			out.writeObject(SudokuHolder.getJogoAtual());
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			IOUtils.closeQuietly(fos);
			IOUtils.closeQuietly(out);
		}
	}

	public static void carregarJogo() {
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream(ARQUIVO_SAVE);
			in = new ObjectInputStream(fis);
			JogoSudoku jogo = (JogoSudoku) in.readObject();
			SudokuHolder.gerarJogoAPartirDe(jogo);
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} finally {
			IOUtils.closeQuietly(fis);
			IOUtils.closeQuietly(in);
		}
	}

}
