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

import	java.awt.*;
import	java.awt.event.*;
import	javax.swing.*;

import puzzle.npgenerator.v1.*;

// アプレットビューアが参照するHTML タグ記述
/*
 	<applet code="NPGenerator" width=600 height=550>
 	</applet>
 */

public class NPGenerator extends JApplet {
    private	static NPBody		appbody;	// アプリケーションフレーム

    // アプレット用
    public void init() {
    	appbody = new NPBody();
    	add(appbody);
		appbody.getFocusOnBoard();   	
    }
    
    // アプリケーション用
    public static void main(String[] args) {
		JFrame frame = new JFrame("Number Place Generator Version 1.0.1");
		appbody = new NPBody();
		frame.add( appbody, "Center" );
		
		frame.setSize(600,550);
		frame.setVisible(true);
		appbody.getFocusOnBoard();
	}
}

// GUI本体の最上位パネル
//	実質的な本体
class	NPBody extends JPanel {
	private Generator	generator = new Generator();
	private Pattern	pattern   = new Pattern();
	private Problem	problem;
	
	//	private int[][] question = new int[Const.size][Const.size];
	private int[][] answer   = new int[Const.size][Const.size];
	private boolean[][] hint = new boolean[Const.size][Const.size];

	private int	     hintcount = 0;
	private boolean    isunique  = false;	
	
	private int		difficultypoint = 0;

	private    JBoard 		jboard;
	private	JButton		clearallbutton;		// 全クリア
	private	JButton		clearwhitebutton;	// 白マスクリア
	private	JButton		clearnumberbutton;	// 数字クリア
	private	JLabel		hintcountlabel;		// ヒント数
	private	JLabel		modelabel;			// 現在のモードを表
	private	JButton		setbutton;			// Set      ボタン
	private	JButton		execbutton;			// Generate ボタン
	private	JButton		stopbutton;			// Stop		ボタン
	private	static boolean		stoppushed;
	private	JButton		playbutton;			// Play     ボタン
	private	JButton		solvebutton;		// Solve　　ボタン
	private	JButton		autobutton;			// Solve　　ボタン
	private 	JCheckBox 	checkmark;
	private 	boolean	checkmarkstate;	
	
	private	static Thread		genrunning;			// 実行中
	private	static Thread		autorunning;		// 実行中

	private	final	static	int	SETUP_MODE 		= 1;
	private	final	static	int GENERATING_MODE = 2;
	private	final	static	int FINISH_MODE		= 3;
	private	final	static	int AUTO_MODE 		= 4;
	private	final	static	int	STOPPED_MODE	= 5;
	private	final	static int	PLAYING_MODE 	= 6;

	private	int			currentmode = SETUP_MODE;
	
	private	Point		cursor = new Point(0,0);	// 取りあえず左上
	
    
	// コンストラクター
	public NPBody() {
		setLayout(new BorderLayout());		
		
		add( makeButtons(), "North" );		// 制御ボタン類
		
		jboard = new JBoard();
		add(jboard,"Center");
		
		JLabel copylabel = new JLabel(
				"Copyright(c)2007  Time Intermedia Corporation.  All rights reserved.", 
				JLabel.CENTER );
		add( copylabel, "South");
	}

	// ヒントの配列の初期化
	private void clearHint() {
		for( int x=0; x<9; ++x )
			for( int y=0; y<9; ++y ) {
				hint[x][y] = false;			
			}
	}
	
	// 解の配列の初期化
	private void clearAnswer( boolean mkrest ) {
		for( int x=0; x<9; ++x )
			for( int y=0; y<9; ++y ) {
				if( !mkrest || !hint[x][y])
					answer[x][y] = 0;		
			}		
	}
	
	// 上側の制御ボタン類
	private JPanel makeButtons() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		JPanel upperpanel = new JPanel();
		JPanel lowerpanel = new JPanel();

		// 上のボタン等の並び	
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

		panel.add(upperpanel,"North");

		// 下のボタン等の並び
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
		
		panel.add(lowerpanel,"South");		
		
		return panel;
	}
	
	// 全消去ボタン
	private void makeClearAllButton() {
		clearallbutton = new JButton( "Clear All");
		clearallbutton.addActionListener(
			new ActionListener() {
				public void actionPerformed( ActionEvent e ) {
					if( currentmode == GENERATING_MODE || currentmode == AUTO_MODE )
						return;
					clearHint();
					clearAnswer(false);
					toSetupMode();
					jboard.paint();
					jboard.requestFocus();
				}
			}
		);
	}

	// 白マスの数字消去ボタン
	private void makeClearWhiteButton() {
		clearwhitebutton = new JButton( "Clear White");
		clearwhitebutton.addActionListener(
			new ActionListener() {
				public void actionPerformed( ActionEvent e ) {
					if( currentmode == GENERATING_MODE || currentmode == AUTO_MODE )
						return;
					clearAnswer(true);
					jboard.paint();
					jboard.requestFocus();
				}
			}
		);
	}
	
	// 数字消去ボタン
	private void makeClearNumberButton() {
		clearnumberbutton = new JButton( "Clear Numbers");
		clearnumberbutton.addActionListener(
			new ActionListener() {
				public void actionPerformed( ActionEvent e ) {
					if( currentmode == GENERATING_MODE || currentmode == AUTO_MODE )
						return;
					clearAnswer(false);
					jboard.paint();
					jboard.requestFocus();
				}
			}
		);
	}

	// セットボタン
	private void makeSetupButton() {
		setbutton = new JButton( "Setup" );
		setbutton.addActionListener(
			new ActionListener() {
				public void actionPerformed( ActionEvent e ) {
					if( currentmode == GENERATING_MODE || currentmode == AUTO_MODE )
						return;
					toSetupMode();
					jboard.requestFocus();
				}
			}
		);
	}

	// Generate(自動生成)ボタン
	private void makeExecutionButton() {
		execbutton = new JButton( "Generate" );
		execbutton.addActionListener(
			new ActionListener() {
				public void actionPerformed( ActionEvent e ) {
					if( currentmode == GENERATING_MODE || currentmode == AUTO_MODE )
						return;
					generate();
					jboard.requestFocus();
				}
			}
		);
	}	

	// Stopボタン
	private void makeStopButton() {
		stopbutton = new JButton( "Stop" );
		stopbutton.addActionListener(
			new ActionListener() {
				public void actionPerformed( ActionEvent e ) {
					stoppushed = true;
					setModeLabel( currentmode = STOPPED_MODE );
					jboard.requestFocus();
				}
			}
		);
	}	

	// Playボタン
	private void makePlayButton() {
		playbutton = new JButton( "Play" );
		playbutton.addActionListener(
			new ActionListener() {
				public void actionPerformed( ActionEvent e ) {
					if( currentmode == GENERATING_MODE || currentmode == AUTO_MODE )
						return;
					toPlayMode();
					jboard.requestFocus();
				}
			}
		);
	}
	
	// Solveボタン
	private void makeSolveButton() {
		solvebutton = new JButton( "Solve" );
		solvebutton.addActionListener(
			new ActionListener() {
				public void actionPerformed( ActionEvent e ) {
					if( currentmode == GENERATING_MODE || currentmode == AUTO_MODE )
						return;
					solveProblem();
					jboard.requestFocus();
				}
			}
		);
	}		

	// Autoボタン
	private void makeAutoButton() {
		autobutton = new JButton( "Auto" );
		autobutton.addActionListener(
			new ActionListener() {
				public void actionPerformed( ActionEvent e ) {
					if( currentmode == GENERATING_MODE || currentmode == AUTO_MODE )
						return;
					autoProblem();
					jboard.requestFocus();
				}
			}
		);
	}
	
	// ヒント表示の有無のチェックボックス
	private void makeCheckMark() {
		checkmark = new JCheckBox("Hint");
		checkmark.addActionListener(
			new ActionListener() {
				public void actionPerformed( ActionEvent e ) {
					checkmarkstate = checkmark.isSelected();
					jboard.paint();
					jboard.requestFocus();
				}
			}
		);
		
		checkmark.setSelected( checkmarkstate = true );		
	}

	// モード表示（ラベル）
	private void makeModeLabel() {
		modelabel = new JLabel( "Setup" );
	}
	private void setModeLabel( int md ) {
		String str = "current mode";
		switch( md ) {
		case SETUP_MODE:		str = "Setup";		break;
		case GENERATING_MODE:	str = "Generating";	break;
		case AUTO_MODE:			str = "Auto";		break;
		case STOPPED_MODE:		str = "Stopped";	break;
		case PLAYING_MODE:		str = "Play";		break;
		}
		modelabel.setText(str);
	}

	// ヒント
	private void makeHintCountLabel() {
		hintcountlabel = new JLabel( "  0" );
	}
	
	private void updatehintcount() {
		hintcount = 0;
		for( int x=0; x<9; x++ )
			for( int y=0; y<9; y++ )
				if( hint[x][y] )
					++hintcount;
		
		hintcountlabel.setText( ""+hintcount );
	}

	
	// セットモードにする
	private void toSetupMode() {
		setModeLabel( currentmode = SETUP_MODE );
	}

	// 自動生成、マルチスレッドにする
	private void generate() {
		if( currentmode == GENERATING_MODE || currentmode == AUTO_MODE )
			return;
		setModeLabel( currentmode = GENERATING_MODE );
		
		clearAnswer(true);
		jboard.paint();

		genrunning = new Thread() {
			public void run() {
				execution();	// 自動生成実行
				if( ! stoppushed ) {
					jboard.paint();	
				}
				genrunning = null;
				currentmode = FINISH_MODE;
			}
		};
		genrunning.start();
	}

	// 自動生成の実行本体
	private void execution() {
		long	starttime = System.currentTimeMillis();
		stoppushed = false;
		isunique = false;
		int loopcount = 0;
		while( !isunique ) {
			if( stoppushed ) {
				setModeLabel( currentmode = STOPPED_MODE );
				genrunning = null;
				return;
			}
			++loopcount;
			problem = generator.make( hint );
			isunique = problem.isUnique();
			long current = System.currentTimeMillis();
			long elapsedtime = current - starttime;
			modelabel.setText( "" + loopcount + " times / " + elapsedtime + " msec" );
			
			if( currentmode == AUTO_MODE  &&  elapsedtime > 10000 )
				return;
		}
		problem.getSolution(answer);
	}

	// Autoモード
	private void autoProblem() {
		if( currentmode == GENERATING_MODE || currentmode == AUTO_MODE )
			return;

		setModeLabel( currentmode = AUTO_MODE );
		clearAnswer(false);
		jboard.paint();
	
		autorunning = new Thread() {
			public void run() {
				for(;;) {
					autoexecution();	// 自動生成実行
					
					if( stoppushed )
						break;
					
					jboard.paint();	
					try {Thread.sleep(1000); } catch( InterruptedException ex ) {}

					if( stoppushed )
						break;					
				}
				autorunning = null;
			}
		};
		autorunning.start();		
	}

	// 自動生成(パターン生成を含む)の実行本体
	private void autoexecution() {
		boolean[][] newhint = pattern.generateHint();	
		for( int x=0; x<9; ++x )
			for( int y=0; y<9; ++y )
				hint[x][y] = newhint[x][y];
		clearAnswer(false);

		jboard.paint();	
		updatehintcount();
		
		try {Thread.sleep(100); } catch( InterruptedException ex ) {}
		
		execution();
	}
	
	// Playモード
	private void toPlayMode() {
		setModeLabel( currentmode = PLAYING_MODE );
		clearAnswer(true);
		jboard.paint();
	}

	// 解く
	private void solveProblem() {
//		long starttime = System.nanoTime();
		difficultypoint = 0;

		//		solveProblemGen();
		solveProblemEval();
		
//		long endtime = System.nanoTime();
		
//		System.out.println( "solvetime " + (endtime - starttime)/1000 + " micro sec" );
		if( difficultypoint > 0 ) {
//			System.out.println( "difficulty "+ difficultypoint );
			modelabel.setText( difficultypoint + " point" );
		}
		jboard.paint();	
	}

	// ジェネレータから呼ぶソルバーを使ったSolve (現在未使用)
	private void solveProblemGen() {
		Status	matrix  = new Status();
		int[][] num = matrix.getNum();		// matrixの盤面
		Solver 	solver = new Solver();
		boolean[][] temphint = new boolean[9][9];
		for( int x=0; x<9; ++x )
			for( int y=0; y<9; ++y ) {
				temphint[x][y] = (num[x][y] = answer[x][y]) != 0;
			}

		matrix.clear(temphint);
		matrix = solver.solve(matrix);
		
		num = matrix.getNum();
		for( int x=0; x<9; ++x )
			for( int y=0; y<9; ++y )
				answer[x][y] = num[x][y];

		jboard.paint();	
	}

	// 評価のためのソルバーを使ったSolve
	private void solveProblemEval() {
		Status	matrix  = new Status();
		Eval 	eval = new Eval();
		difficultypoint = 0;
		
		matrix = eval.solve( answer );
		if( !matrix.isNoAnswer() && matrix.getSpaceCount()==0 )
			difficultypoint = eval.getPoint();
		
		int[][] num = matrix.getNum();
		for( int x=0; x<9; ++x )
			for( int y=0; y<9; ++y )
				answer[x][y] = num[x][y];

		jboard.paint();		
	}
	
	// フォーカスの取得
	public void getFocusOnBoard() {
		jboard.requestFocus();
	}
	
	// 問題盤面表示
	class JBoard extends JPanel {
		private Color bgcolor = Color.white;
		private Color markcolor = new Color(0xc0,0xde,0xff);
		private int	bwidth, bheight, bsize, unit;
		private int	x0, y0;
		private Graphics gr;
		
		JBoard() {
			super();
			setBackground( Color.white );
			
			addMouseListener(
				new MouseAdapter() {
					public void mousePressed( MouseEvent e ) {
						int xi = xidx(e.getX());
						int yi = yidx(e.getY());
						if( xi<0 || yi<0 )
							return;

						if( currentmode == SETUP_MODE ) {
							hint[xi][yi] = !hint[xi][yi];
							updatehintcount();
						}
						cursormoveto( xi, yi );						
					}
				}
			);
			
			addKeyListener(
				new KeyAdapter() {
					public void keyPressed( KeyEvent e ) {
						switch( e.getKeyCode() ) {
						case KeyEvent.VK_LEFT:
							cursormove( -1, 0 );	return;
						case KeyEvent.VK_UP:
							cursormove( 0, -1 );	return;
						case KeyEvent.VK_RIGHT:
							cursormove( +1, 0 );	return;
						case KeyEvent.VK_DOWN:
							cursormove( 0, +1 );	return;
						}
						
						int v = -1;
						switch( e.getKeyChar() ) {
						case ' ':
						case '0':	v = 0;	break;
						case '1':	v = 1;	break;	
						case '2':	v = 2;	break;
						case '3':	v = 3;	break;
						case '4':	v = 4;	break;
						case '5':	v = 5;	break;
						case '6':	v = 6;	break;
						case '7':	v = 7;	break;
						case '8':	v = 8;	break;
						case '9':	v = 9;	break;
						}
						if( v < 0 )
							return;
						if( currentmode == PLAYING_MODE && hint[cursor.x][cursor.y])
							return;
						
						answer[cursor.x][cursor.y] = v;
						paintCell();
					}
				}
			);
		}

		// カーソルの移動
		private void cursormove( int dx, int dy ) {
			int x = (cursor.x + 9 + dx) % 9;
			int y = (cursor.y + 9 + dy) % 9;	
			cursormoveto( x, y );
		}
		
		private void cursormoveto( int x, int y ) {
			int oldx = cursor.x;
			int oldy = cursor.y;

			cursor.x = x;
			cursor.y = y;
			gr = getGraphics();
			paintCell( oldx, oldy );
			paintCell( cursor.x, cursor.y );			
		}
		 
		// 表示する。表示する内容は、answer[][]
		public void paint() {
			paint( getGraphics() );
		}

		public void paint( Graphics g ) {
			gr = g;
			Dimension sz = getSize();
			bwidth  = sz.width;
			bheight = sz.height;
			g.setColor( Color.white);
			g.fillRect(0,0, bwidth, bheight );
			
			linedraw();
			paintCells();
		}
		
		// 線の描画
		private void linedraw() {
			gr.setColor( Color.black);
			bsize = Math.min(bwidth,bheight);
			unit = bsize / 10;
			x0 = (bwidth - unit*9) / 2;
			y0 = (bheight - unit*9) / 2;
			
			for( int i=0; i<=9; ++i ) {
				int	w = (i%3==0) ? 5 : 1;
				gr.fillRect( x0-w/2, yi(i)-w/2, unit*9+w, w );	// 横線
				gr.fillRect( xi(i)-w/2, y0-w/2, w, unit*9+w );	// 縦線
			}
		}

		// マスのインデックスと、パネル上での位置関係の変換
		private int xi( int i ) { return x0 + unit * i; }
		private int yi( int i ) { return y0 + unit * i; }

		private int xidx( int px ) {
			for( int i=0; i<9; ++i ) {
				if( xi(i) <= px && px<=xi(i+1) )
					return i;
			}
			return -1;
		}

		private int yidx( int py ) {
			for( int i=0; i<9; ++i ) {
				if( yi(i) <= py && py<=yi(i+1) )
					return i;
			}
			return -1;
		}
		
		// 全マスの描画
		private void paintCells() {
			for( int x=0; x<9; ++x )
				for( int y=0; y<9; ++y )
					paintCell( x, y );
		}

		// マスの描画
		private void paintCell() {
			paintCell(cursor.x,cursor.y);
		}
		
		private void paintCell( int x, int y ) {
			gr.setColor((checkmarkstate && hint[x][y]) ? markcolor : bgcolor);
			int x0 = xi(x)   + ((x%3==0)  ?3:1);
			int x1 = xi(x+1) - (((x+1)%3==0?3:1));
			int y0 = yi(y)   + ((y%3==0)  ?3:1);
			int y1 = yi(y+1) - (((y+1)%3==0?3:1));
			gr.fillRect( x0,y0,x1-x0+1,y1-y0+1);	// 際どいことをやっているな
			
			// cursor
			if( cursor.x == x && cursor.y == y )
				drawCursor(x,y);
			
			int v = answer[x][y];
			if( v == 0)
				return;
			String str = ""+v;
			Color col = hint[x][y] ? Color.black : Color.blue;
			
			gr.setColor( col );
			Font font = new Font( "Dialog", Font.BOLD, unit*4/5 );
			gr.setFont(font);
						
			FontMetrics fm = getFontMetrics( gr.getFont() );
			int xx = xi(x) + (unit - fm.stringWidth(str))/2;
			int yy = yi(y) + (unit - fm.getHeight())/2 + fm.getAscent();
			gr.drawString( str, xx, yy );		
		}
		
		// マス内部のカーソルの描画
		private void drawCursor( int x, int y ) {
			int x0 = xi(x)   + ((x%3==0)  ?3:1);
			int x1 = xi(x+1) - (((x+1)%3==0?3:1));
			int y0 = yi(y)   + ((y%3==0)  ?3:1);
			int y1 = yi(y+1) - (((y+1)%3==0?3:1));
			
			gr.setColor(Color.green);
			for( int i=0; i<unit/10; ++i )
				gr.drawRect(x0+i,y0+i,x1-x0-2*i,y1-y0-2*i);
		}
	}
}

