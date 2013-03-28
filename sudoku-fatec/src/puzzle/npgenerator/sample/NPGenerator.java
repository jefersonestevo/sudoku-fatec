package puzzle.npgenerator.sample;

/*
 *    Copyright(C) 2007 Time Intermedia Corporation <puzzle@timedia.co.jp>
 *    All rights reserved.
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *    
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import puzzle.npgenerator.v1.Const;
import puzzle.npgenerator.v1.Eval;
import puzzle.npgenerator.v1.Generator;
import puzzle.npgenerator.v1.Problem;
import puzzle.npgenerator.v1.Status;

// ƒAƒvƒŒƒbƒgƒrƒ…�[ƒA‚ªŽQ�Æ‚·‚éHTML ƒ^ƒO‹L�q
/*
 <applet code="NPGenerator" width=600 height=550>
 </applet>
 */

public class NPGenerator extends JApplet {
	private static final long serialVersionUID = 1994988027090819069L;
	private static NPBody appbody; // ƒAƒvƒŠƒP�[ƒVƒ‡ƒ“ƒtƒŒ�[ƒ€

	// ƒAƒvƒŒƒbƒg—p
	public void init() {
		appbody = new NPBody();
		add(appbody);
		appbody.getFocusOnBoard();
	}

	// ƒAƒvƒŠƒP�[ƒVƒ‡ƒ“—p
	public static void main(String[] args) {
		JFrame frame = new JFrame("Number Place Generator Version 1.0.1");
		appbody = new NPBody();
		frame.add(appbody, "Center");

		frame.setSize(600, 550);
		frame.setVisible(true);
		appbody.getFocusOnBoard();
	}
}

// GUI–{‘Ì‚Ì�Å�ãˆÊƒpƒlƒ‹
// ŽÀŽ¿“I‚È–{‘Ì
class NPBody extends JPanel {

	private static final long serialVersionUID = -6253615286835720351L;

	private Generator generator = new Generator();
	private Pattern pattern = new Pattern();
	private Problem problem;

	// private int[][] question = new int[Const.size][Const.size];
	private int[][] answer = new int[Const.size][Const.size];
	private boolean[][] hint = new boolean[Const.size][Const.size];

	private int hintcount = 0;
	private boolean isunique = false;

	private int difficultypoint = 0;

	private JBoard jboard;
	private JButton clearallbutton; // ‘SƒNƒŠƒA
	private JButton clearwhitebutton; // ”’ƒ}ƒXƒNƒŠƒA
	private JButton clearnumberbutton; // �”ŽšƒNƒŠƒA
	private JLabel hintcountlabel; // ƒqƒ“ƒg�”
	private JLabel modelabel; // Œ»�Ý‚Ìƒ‚�[ƒh‚ð•\
	private JButton setbutton; // Set ƒ{ƒ^ƒ“
	private JButton execbutton; // Generate ƒ{ƒ^ƒ“
	private JButton stopbutton; // Stop ƒ{ƒ^ƒ“
	private static boolean stoppushed;
	private JButton playbutton; // Play ƒ{ƒ^ƒ“
	private JButton solvebutton; // Solve�@�@ƒ{ƒ^ƒ“
	private JButton autobutton; // Solve�@�@ƒ{ƒ^ƒ“
	private JCheckBox checkmark;
	private boolean checkmarkstate;

	private static Thread genrunning; // ŽÀ�s’†
	private static Thread autorunning; // ŽÀ�s’†

	private final static int SETUP_MODE = 1;
	private final static int GENERATING_MODE = 2;
	private final static int FINISH_MODE = 3;
	private final static int AUTO_MODE = 4;
	private final static int STOPPED_MODE = 5;
	private final static int PLAYING_MODE = 6;

	private int currentmode = SETUP_MODE;

	private Point cursor = new Point(0, 0); // Žæ‚è‚ ‚¦‚¸�¶�ã

	// ƒRƒ“ƒXƒgƒ‰ƒNƒ^�[
	public NPBody() {
		setLayout(new BorderLayout());

		add(makeButtons(), "North"); // �§Œäƒ{ƒ^ƒ“—Þ

		jboard = new JBoard();
		add(jboard, "Center");

		JLabel copylabel = new JLabel(
				"Copyright(c)2007  Time Intermedia Corporation.  All rights reserved.",
				JLabel.CENTER);
		add(copylabel, "South");
	}

	// ƒqƒ“ƒg‚Ì”z—ñ‚Ì�‰Šú‰»
	private void clearHint() {
		for (int x = 0; x < 9; ++x)
			for (int y = 0; y < 9; ++y) {
				hint[x][y] = false;
			}
	}

	// ‰ð‚Ì”z—ñ‚Ì�‰Šú‰»
	private void clearAnswer(boolean mkrest) {
		for (int x = 0; x < 9; ++x)
			for (int y = 0; y < 9; ++y) {
				if (!mkrest || !hint[x][y])
					answer[x][y] = 0;
			}
	}

	// �ã‘¤‚Ì�§Œäƒ{ƒ^ƒ“—Þ
	private JPanel makeButtons() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JPanel upperpanel = new JPanel();
		JPanel lowerpanel = new JPanel();

		// �ã‚Ìƒ{ƒ^ƒ““™‚Ì•À‚Ñ
		makeModeLabel();
		upperpanel.add(modelabel);

		makeClearAllButton();
		upperpanel.add(clearallbutton);

		makeClearWhiteButton();
		upperpanel.add(clearwhitebutton);

		makeClearNumberButton();
		upperpanel.add(clearnumberbutton);

		makeCheckMark();
		upperpanel.add(checkmark);

		makeHintCountLabel();
		upperpanel.add(hintcountlabel);

		panel.add(upperpanel, "North");

		// ‰º‚Ìƒ{ƒ^ƒ““™‚Ì•À‚Ñ
		makeSetupButton();
		lowerpanel.add(setbutton);

		makeExecutionButton();
		lowerpanel.add(execbutton);

		makePlayButton();
		lowerpanel.add(playbutton);

		makeStopButton();
		lowerpanel.add(stopbutton);

		makeAutoButton();
		lowerpanel.add(autobutton);

		makeSolveButton();
		lowerpanel.add(solvebutton);

		panel.add(lowerpanel, "South");

		return panel;
	}

	// ‘S�Á‹Žƒ{ƒ^ƒ“
	private void makeClearAllButton() {
		clearallbutton = new JButton("Clear All");
		clearallbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentmode == GENERATING_MODE || currentmode == AUTO_MODE)
					return;
				clearHint();
				clearAnswer(false);
				toSetupMode();
				jboard.paint();
				jboard.requestFocus();
			}
		});
	}

	// ”’ƒ}ƒX‚Ì�”Žš�Á‹Žƒ{ƒ^ƒ“
	private void makeClearWhiteButton() {
		clearwhitebutton = new JButton("Clear White");
		clearwhitebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentmode == GENERATING_MODE || currentmode == AUTO_MODE)
					return;
				clearAnswer(true);
				jboard.paint();
				jboard.requestFocus();
			}
		});
	}

	// �”Žš�Á‹Žƒ{ƒ^ƒ“
	private void makeClearNumberButton() {
		clearnumberbutton = new JButton("Clear Numbers");
		clearnumberbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentmode == GENERATING_MODE || currentmode == AUTO_MODE)
					return;
				clearAnswer(false);
				jboard.paint();
				jboard.requestFocus();
			}
		});
	}

	// ƒZƒbƒgƒ{ƒ^ƒ“
	private void makeSetupButton() {
		setbutton = new JButton("Setup");
		setbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentmode == GENERATING_MODE || currentmode == AUTO_MODE)
					return;
				toSetupMode();
				jboard.requestFocus();
			}
		});
	}

	// Generate(Ž©“®�¶�¬)ƒ{ƒ^ƒ“
	private void makeExecutionButton() {
		execbutton = new JButton("Generate");
		execbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentmode == GENERATING_MODE || currentmode == AUTO_MODE)
					return;
				generate();
				jboard.requestFocus();
			}
		});
	}

	// Stopƒ{ƒ^ƒ“
	private void makeStopButton() {
		stopbutton = new JButton("Stop");
		stopbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stoppushed = true;
				setModeLabel(currentmode = STOPPED_MODE);
				jboard.requestFocus();
			}
		});
	}

	// Playƒ{ƒ^ƒ“
	private void makePlayButton() {
		playbutton = new JButton("Play");
		playbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentmode == GENERATING_MODE || currentmode == AUTO_MODE)
					return;
				toPlayMode();
				jboard.requestFocus();
			}
		});
	}

	// Solveƒ{ƒ^ƒ“
	private void makeSolveButton() {
		solvebutton = new JButton("Solve");
		solvebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentmode == GENERATING_MODE || currentmode == AUTO_MODE)
					return;
				solveProblem();
				jboard.requestFocus();
			}
		});
	}

	// Autoƒ{ƒ^ƒ“
	private void makeAutoButton() {
		autobutton = new JButton("Auto");
		autobutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentmode == GENERATING_MODE || currentmode == AUTO_MODE)
					return;
				autoProblem();
				jboard.requestFocus();
			}
		});
	}

	// ƒqƒ“ƒg•\Ž¦‚Ì—L–³‚Ìƒ`ƒFƒbƒNƒ{ƒbƒNƒX
	private void makeCheckMark() {
		checkmark = new JCheckBox("Hint");
		checkmark.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkmarkstate = checkmark.isSelected();
				jboard.paint();
				jboard.requestFocus();
			}
		});

		checkmark.setSelected(checkmarkstate = true);
	}

	// ƒ‚�[ƒh•\Ž¦�iƒ‰ƒxƒ‹�j
	private void makeModeLabel() {
		modelabel = new JLabel("Setup");
	}

	private void setModeLabel(int md) {
		String str = "current mode";
		switch (md) {
		case SETUP_MODE:
			str = "Setup";
			break;
		case GENERATING_MODE:
			str = "Generating";
			break;
		case AUTO_MODE:
			str = "Auto";
			break;
		case STOPPED_MODE:
			str = "Stopped";
			break;
		case PLAYING_MODE:
			str = "Play";
			break;
		}
		modelabel.setText(str);
	}

	// ƒqƒ“ƒg
	private void makeHintCountLabel() {
		hintcountlabel = new JLabel("  0");
	}

	private void updatehintcount() {
		hintcount = 0;
		for (int x = 0; x < 9; x++)
			for (int y = 0; y < 9; y++)
				if (hint[x][y])
					++hintcount;

		hintcountlabel.setText("" + hintcount);
	}

	// ƒZƒbƒgƒ‚�[ƒh‚É‚·‚é
	private void toSetupMode() {
		setModeLabel(currentmode = SETUP_MODE);
	}

	// Ž©“®�¶�¬�Aƒ}ƒ‹ƒ`ƒXƒŒƒbƒh‚É‚·‚é
	private void generate() {
		if (currentmode == GENERATING_MODE || currentmode == AUTO_MODE)
			return;
		setModeLabel(currentmode = GENERATING_MODE);

		clearAnswer(true);
		jboard.paint();

		genrunning = new Thread() {
			public void run() {
				execution(); // Ž©“®�¶�¬ŽÀ�s
				if (!stoppushed) {
					jboard.paint();
				}
				genrunning = null;
				currentmode = FINISH_MODE;
			}
		};
		genrunning.start();
	}

	// Ž©“®�¶�¬‚ÌŽÀ�s–{‘Ì
	private void execution() {
		long starttime = System.currentTimeMillis();
		stoppushed = false;
		isunique = false;
		int loopcount = 0;
		while (!isunique) {
			if (stoppushed) {
				setModeLabel(currentmode = STOPPED_MODE);
				genrunning = null;
				return;
			}
			++loopcount;
			problem = generator.make(hint);
			isunique = problem.isUnique();
			long current = System.currentTimeMillis();
			long elapsedtime = current - starttime;
			modelabel.setText("" + loopcount + " times / " + elapsedtime
					+ " msec");

			if (currentmode == AUTO_MODE && elapsedtime > 10000)
				return;
		}
		problem.getSolution(answer);
	}

	// Autoƒ‚�[ƒh
	private void autoProblem() {
		if (currentmode == GENERATING_MODE || currentmode == AUTO_MODE)
			return;

		setModeLabel(currentmode = AUTO_MODE);
		clearAnswer(false);
		jboard.paint();

		autorunning = new Thread() {
			public void run() {
				for (;;) {
					autoexecution(); // Ž©“®�¶�¬ŽÀ�s

					if (stoppushed)
						break;

					jboard.paint();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ex) {
					}

					if (stoppushed)
						break;
				}
				autorunning = null;
			}
		};
		autorunning.start();
	}

	// Ž©“®�¶�¬(ƒpƒ^�[ƒ“�¶�¬‚ðŠÜ‚Þ)‚ÌŽÀ�s–{‘Ì
	private void autoexecution() {
		boolean[][] newhint = pattern.generateHint();
		for (int x = 0; x < 9; ++x)
			for (int y = 0; y < 9; ++y)
				hint[x][y] = newhint[x][y];
		clearAnswer(false);

		jboard.paint();
		updatehintcount();

		try {
			Thread.sleep(100);
		} catch (InterruptedException ex) {
		}

		execution();
	}

	// Playƒ‚�[ƒh
	private void toPlayMode() {
		setModeLabel(currentmode = PLAYING_MODE);
		clearAnswer(true);
		jboard.paint();
	}

	// ‰ð‚­
	private void solveProblem() {
		// long starttime = System.nanoTime();
		difficultypoint = 0;

		// solveProblemGen();
		solveProblemEval();

		// long endtime = System.nanoTime();

		// System.out.println( "solvetime " + (endtime - starttime)/1000 +
		// " micro sec" );
		if (difficultypoint > 0) {
			// System.out.println( "difficulty "+ difficultypoint );
			modelabel.setText(difficultypoint + " point");
		}
		jboard.paint();
	}

	// ƒWƒFƒlƒŒ�[ƒ^‚©‚çŒÄ‚Ôƒ\ƒ‹ƒo�[‚ðŽg‚Á‚½Solve (Œ»�Ý–¢Žg—p)
	// private void solveProblemGen() {
	// Status matrix = new Status();
	// int[][] num = matrix.getNum(); // matrix‚Ì”Õ–Ê
	// Solver solver = new Solver();
	// boolean[][] temphint = new boolean[9][9];
	// for( int x=0; x<9; ++x )
	// for( int y=0; y<9; ++y ) {
	// temphint[x][y] = (num[x][y] = answer[x][y]) != 0;
	// }
	//
	// matrix.clear(temphint);
	// matrix = solver.solve(matrix);
	//
	// num = matrix.getNum();
	// for( int x=0; x<9; ++x )
	// for( int y=0; y<9; ++y )
	// answer[x][y] = num[x][y];
	//
	// jboard.paint();
	// }

	// •]‰¿‚Ì‚½‚ß‚Ìƒ\ƒ‹ƒo�[‚ðŽg‚Á‚½Solve
	private void solveProblemEval() {
		Status matrix = new Status();
		Eval eval = new Eval();
		difficultypoint = 0;

		matrix = eval.solve(answer);
		if (!matrix.isNoAnswer() && matrix.getSpaceCount() == 0)
			difficultypoint = eval.getPoint();

		int[][] num = matrix.getNum();
		for (int x = 0; x < 9; ++x)
			for (int y = 0; y < 9; ++y)
				answer[x][y] = num[x][y];

		jboard.paint();
	}

	// ƒtƒH�[ƒJƒX‚ÌŽæ“¾
	public void getFocusOnBoard() {
		jboard.requestFocus();
	}

	// –â‘è”Õ–Ê•\Ž¦
	class JBoard extends JPanel {
		private static final long serialVersionUID = 1L;

		private Color bgcolor = Color.white;
		private Color markcolor = new Color(0xc0, 0xde, 0xff);
		private int bwidth, bheight, bsize, unit;
		private int x0, y0;
		private Graphics gr;

		JBoard() {
			super();
			setBackground(Color.white);

			addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					int xi = xidx(e.getX());
					int yi = yidx(e.getY());
					if (xi < 0 || yi < 0)
						return;

					if (currentmode == SETUP_MODE) {
						hint[xi][yi] = !hint[xi][yi];
						updatehintcount();
					}
					cursormoveto(xi, yi);
				}
			});

			addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					switch (e.getKeyCode()) {
					case KeyEvent.VK_LEFT:
						cursormove(-1, 0);
						return;
					case KeyEvent.VK_UP:
						cursormove(0, -1);
						return;
					case KeyEvent.VK_RIGHT:
						cursormove(+1, 0);
						return;
					case KeyEvent.VK_DOWN:
						cursormove(0, +1);
						return;
					}

					int v = -1;
					switch (e.getKeyChar()) {
					case ' ':
					case '0':
						v = 0;
						break;
					case '1':
						v = 1;
						break;
					case '2':
						v = 2;
						break;
					case '3':
						v = 3;
						break;
					case '4':
						v = 4;
						break;
					case '5':
						v = 5;
						break;
					case '6':
						v = 6;
						break;
					case '7':
						v = 7;
						break;
					case '8':
						v = 8;
						break;
					case '9':
						v = 9;
						break;
					}
					if (v < 0)
						return;
					if (currentmode == PLAYING_MODE && hint[cursor.x][cursor.y])
						return;

					answer[cursor.x][cursor.y] = v;
					paintCell();
				}
			});
		}

		// ƒJ�[ƒ\ƒ‹‚ÌˆÚ“®
		private void cursormove(int dx, int dy) {
			int x = (cursor.x + 9 + dx) % 9;
			int y = (cursor.y + 9 + dy) % 9;
			cursormoveto(x, y);
		}

		private void cursormoveto(int x, int y) {
			int oldx = cursor.x;
			int oldy = cursor.y;

			cursor.x = x;
			cursor.y = y;
			gr = getGraphics();
			paintCell(oldx, oldy);
			paintCell(cursor.x, cursor.y);
		}

		// •\Ž¦‚·‚é�B•\Ž¦‚·‚é“à—e‚Í�Aanswer[][]
		public void paint() {
			paint(getGraphics());
		}

		public void paint(Graphics g) {
			gr = g;
			Dimension sz = getSize();
			bwidth = sz.width;
			bheight = sz.height;
			g.setColor(Color.white);
			g.fillRect(0, 0, bwidth, bheight);

			linedraw();
			paintCells();
		}

		// �ü‚Ì•`‰æ
		private void linedraw() {
			gr.setColor(Color.black);
			bsize = Math.min(bwidth, bheight);
			unit = bsize / 10;
			x0 = (bwidth - unit * 9) / 2;
			y0 = (bheight - unit * 9) / 2;

			for (int i = 0; i <= 9; ++i) {
				int w = (i % 3 == 0) ? 5 : 1;
				gr.fillRect(x0 - w / 2, yi(i) - w / 2, unit * 9 + w, w); // ‰¡�ü
				gr.fillRect(xi(i) - w / 2, y0 - w / 2, w, unit * 9 + w); // �c�ü
			}
		}

		// ƒ}ƒX‚ÌƒCƒ“ƒfƒbƒNƒX‚Æ�Aƒpƒlƒ‹�ã‚Å‚ÌˆÊ’uŠÖŒW‚Ì•ÏŠ·
		private int xi(int i) {
			return x0 + unit * i;
		}

		private int yi(int i) {
			return y0 + unit * i;
		}

		private int xidx(int px) {
			for (int i = 0; i < 9; ++i) {
				if (xi(i) <= px && px <= xi(i + 1))
					return i;
			}
			return -1;
		}

		private int yidx(int py) {
			for (int i = 0; i < 9; ++i) {
				if (yi(i) <= py && py <= yi(i + 1))
					return i;
			}
			return -1;
		}

		// ‘Sƒ}ƒX‚Ì•`‰æ
		private void paintCells() {
			for (int x = 0; x < 9; ++x)
				for (int y = 0; y < 9; ++y)
					paintCell(x, y);
		}

		// ƒ}ƒX‚Ì•`‰æ
		private void paintCell() {
			paintCell(cursor.x, cursor.y);
		}

		private void paintCell(int x, int y) {
			gr.setColor((checkmarkstate && hint[x][y]) ? markcolor : bgcolor);
			int x0 = xi(x) + ((x % 3 == 0) ? 3 : 1);
			int x1 = xi(x + 1) - (((x + 1) % 3 == 0 ? 3 : 1));
			int y0 = yi(y) + ((y % 3 == 0) ? 3 : 1);
			int y1 = yi(y + 1) - (((y + 1) % 3 == 0 ? 3 : 1));
			gr.fillRect(x0, y0, x1 - x0 + 1, y1 - y0 + 1); // �Û‚Ç‚¢‚±‚Æ‚ð‚â‚Á‚Ä‚¢‚é‚È

			// cursor
			if (cursor.x == x && cursor.y == y)
				drawCursor(x, y);

			int v = answer[x][y];
			if (v == 0)
				return;
			String str = "" + v;
			Color col = hint[x][y] ? Color.black : Color.blue;

			gr.setColor(col);
			Font font = new Font("Dialog", Font.BOLD, unit * 4 / 5);
			gr.setFont(font);

			FontMetrics fm = getFontMetrics(gr.getFont());
			int xx = xi(x) + (unit - fm.stringWidth(str)) / 2;
			int yy = yi(y) + (unit - fm.getHeight()) / 2 + fm.getAscent();
			gr.drawString(str, xx, yy);
		}

		// ƒ}ƒX“à•”‚ÌƒJ�[ƒ\ƒ‹‚Ì•`‰æ
		private void drawCursor(int x, int y) {
			int x0 = xi(x) + ((x % 3 == 0) ? 3 : 1);
			int x1 = xi(x + 1) - (((x + 1) % 3 == 0 ? 3 : 1));
			int y0 = yi(y) + ((y % 3 == 0) ? 3 : 1);
			int y1 = yi(y + 1) - (((y + 1) % 3 == 0 ? 3 : 1));

			gr.setColor(Color.green);
			for (int i = 0; i < unit / 10; ++i)
				gr.drawRect(x0 + i, y0 + i, x1 - x0 - 2 * i, y1 - y0 - 2 * i);
		}
	}
}
