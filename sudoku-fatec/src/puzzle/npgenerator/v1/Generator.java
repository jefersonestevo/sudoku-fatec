package puzzle.npgenerator.v1;

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

public class Generator {
	
	private Problem	problem = new Problem();
	private Status		matrix  = new Status();
	private Status		matrix2 = new Status();
	private Status		matrix3 = new Status();
	
	private InitAnswer	initanswer = new InitAnswer();
	
	private int memo[] = new int[Const.size];
	private int space;

	// ソルバー
	private Solver solver = new Solver();
	
	public Problem make( boolean[][] hint ) {
		
		problem.setHint( hint );		// ヒントを保管する

		int[][] num = matrix.getNum();	// matrixの盤面
				
		initanswer.make();		// numに初期解パターンを入れる
		initanswer.get( num );
		
//		System.out.println("-init solution-");
//		matrix.printboard();
		
		matrix.clear(hint);						// ヒント位置の数字だけ残す
			
		matrix = solver.solve(matrix);			// 解く
		space = matrix.getSpaceCount();
		
		if( space > 0 )
			makeForever();						// 出来るまで頑張る！

		matrix.clear(hint);
		matrix = solver.solve(matrix);

//		System.out.println("spacecount=" + matrix.getSpaceCount());
//		matrix.printboard();
		
		if(matrix.getSpaceCount()==0 && !matrix.isNoAnswer() ) {
			problem.setUnique(true);
		} else {
			problem.setUnique(false);
		}
		
		Status state = problem.getState();
		state.setStatus( matrix );
		problem.setSolution( matrix.getNum() );
		
//		System.out.println("-before clear hint-");
//		matrix.printboard();

		matrix.clear(hint);
		problem.setQuestion( matrix.getNum() );
		
//		System.out.println("-before clear hint-");
//		matrix.printboard();
		
		return problem;
	}
	
	//-----------------------------------------------------------------
	//  出来るまで永久ループ
	//-----------------------------------------------------------------
	private void makeForever() {
		int base, base2;
	
		base = base2 = 0;
		
		for(;;) {
			if( changeHint(base) )
				base2 = base;
			base = ++base % Const.size;
			if( base==base2 || matrix.getSpaceCount()==0 )
				break;		
		}
	}
	
	//-----------------------------------------------------------------
	//  y==q の横列に対して。。。
	//-----------------------------------------------------------------
	private boolean changeHint( int q ) {
		boolean ok = false;

//		System.out.println(">>changeHint 1");
//		matrix.printboard();
		
		// q の行をmemoにコピーし、Num[][] のその行を全部０にする
		for( int i=0; i<Const.size; ++i ) {
			memo[i] = problem.isHint(i,q) ? matrix.getNum(i,q) : 0;
			matrix.setNum(i,q,0);
		}
		
//		System.out.println(">>changeHint 2");
//		matrix.printboard();		
		
		matrix.clear(problem.getHint());
		matrix = solver.solve(matrix);
		matrix2.setStatus(matrix);
		
//		System.out.println(">>changeHint 3");
//		matrix.printboard();		
		
		for( int i=0; i<Const.size; ++i ) {
//			System.out.println("i="+i);
			if( ! problem.isHint(i,q) )
				continue;		// ヒントでない時は何もしない
			
			matrix.setStatus(matrix2);
			for( int j=0; j<Const.size; ++j )
				if( memo[j]>0 && j!=i ) {
					matrix.setNum(j,q,memo[j]);
					matrix = solver.solveLocal(matrix,j,q);
				}
			matrix3.setStatus(matrix);
			boolean down = false;	// 空白が減少したかのフラグ
			
			int mat = memo[i] % Const.size + 1;
			while( !matrix.isCand(i,q,mat) ) {
				mat = mat % Const.size + 1;
			}
			
			matrix.assignNum(i,q,mat);
			matrix = solver.solveLocal(matrix,i,q);
//			System.out.println( "spaceCount()="+matrix.getSpaceCount() );
			
			while( mat!=memo[i] && matrix.getSpaceCount() > 0 ) {
//				System.out.println( "i="+i+" mat="+mat + " memo[i]="+Memo[i] );
				if( space>matrix.getSpaceCount() && !matrix.isNoAnswer() ) {
					down = true;
					ok = true;
					space = matrix.getSpaceCount();
					memo[i] = mat;
//					System.out.println("break-while");
					break;		// -----break -----
				}
				
				matrix.setStatus(matrix3);
				mat = mat % Const.size + 1;
				while( !matrix.isCand(i,q,mat) ) {
					mat = mat % Const.size + 1;
				}
				matrix.assignNum(i,q,mat);
				matrix = solver.solveLocal(matrix,i,q);

//				matrix.printboard();
			}
//			System.out.println( "changeHint  space=" + matrix.getSpaceCount() );

			if( matrix.getSpaceCount()==0 && !matrix.isNoAnswer() ) {
				space = 0;
				break;
			}
			if( down ) {
				i = 0;
				if( space == 0 )
					break;
			}
		}
		
		return ok;
	}
}

