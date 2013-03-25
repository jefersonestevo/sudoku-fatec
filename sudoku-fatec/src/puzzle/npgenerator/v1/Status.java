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

public class Status {

	// その座標の値
	private int[][]	num	= new int[Const.size][Const.size];

	// 候補　その座標にその値は入るか　[x][y][r]
	private boolean[][][]	cand = new boolean[Const.size][Const.size][Const.size+1];

	// その列、ブロックに、その数は存在するか
	private boolean[][] xExist = new boolean[Const.size][Const.size+1];
	private boolean[][] yExist = new boolean[Const.size][Const.size+1];
	private boolean[][] zExist = new boolean[Const.size][Const.size+1];

	// そのマスに入りうる数字の候補数
	private int[][] gCandCount = new int [Const.size][Const.size];
	
	// その列、ブロック内の、その数が入りうるマスの数
	private int[][] xCandCount = new int[Const.size][Const.size+1];
	private int[][] yCandCount = new int[Const.size][Const.size+1];
	private int[][] zCandCount = new int[Const.size][Const.size+1];
	
	// 解の有無
	private boolean noAnswer;

	// 空きマスの数
	private int spaceCount;
	
	// 引数で渡されたStatusを現Statusにセットする
	public void setStatus(Status st) {
		noAnswer 	= st.noAnswer;
		spaceCount	= st.spaceCount;
		
		for( int i=0; i<Const.size; ++i ) {
			for( int j=0; j<Const.size; ++j ) {
				num[i][j] = st.num[i][j];
				gCandCount[i][j] = st.gCandCount[i][j];	
				
				for( int k=1; k<=Const.size; ++k ) {
					cand[i][j][k] = st.cand[i][j][k];
				}
				
			}
			for( int k=1; k<=Const.size; ++k ) {
				xExist[i][k] = st.xExist[i][k];
				yExist[i][k] = st.yExist[i][k];	
				zExist[i][k] = st.zExist[i][k];	
				
				xCandCount[i][k] = st.xCandCount[i][k];
				yCandCount[i][k] = st.yCandCount[i][k];
				zCandCount[i][k] = st.zCandCount[i][k];
			}
		}
	}
	
	// hintだけを残して、クリアする
	public void clear( boolean hint[][] ) {
		noAnswer = false;
		spaceCount = 0;
		
		for( int i=0; i<Const.size; ++i ) {
			for( int j=0; j<Const.size; ++j ) {
				if( hint!=null && !hint[i][j] )
					num[i][j] = 0;	// ヒントでなければクリア
				
				if( num[i][j] == 0 )
					spaceCount++;
				
				 gCandCount[i][j] = Const.size;

				 for( int k=1; k<=Const.size; ++k )
					 cand[i][j][k] = true;
			}
			
			for( int k=1; k<=Const.size; ++k ) {
				xCandCount[i][k] = yCandCount[i][k] = zCandCount[i][k] = Const.size;			
				xExist[i][k] = yExist[i][k] = zExist[i][k] = false;
			}
		}
	}
	
	// 空きマスを数える
	public int countSpace() {
		int n = 0;
		
		for( int x=0; x<Const.size; ++x )
			for( int y=0; y<Const.size; ++y )
				if( num[x][y] == 0 )
					++n;
		
		return n;
	}
	
	// マスに数をセットする
	// (p,q) が空ならば、数ｒを配置し、true を返す
	//       にすでに数字が入っていれば、何もせずfalseを返す
	//		0<=p,q<size, 1<=r<=size
	public boolean assignNum( int p, int q, int r ) {
		if( num[p][q] == 0 ) {
			if( xExist[p][r] || yExist[q][r] || zExist[Calc.zone(p,q)][r] )
				noAnswer = true;
			num[p][q] = r;
			--spaceCount;
			xExist[p][r] = true;
			yExist[q][r] = true;
			zExist[Calc.zone(p,q)][r] = true;
			
			return true;
		} else {
			return false;
		}
	}
	
	// マスから候補を除去する
	// (p,q)の候補にｒがあれば、除去してtrueを返す
	//                  なければ、何もせずfalseを返す
	//		0<=p,q<size, 1<=r<=size
	public boolean deleteCand( int p, int q, int r ) {
		if( cand[p][q][r] ) {
			cand[p][q][r] = false;
			if( --gCandCount[p][q] == 0 )	
				noAnswer = true;
			if( --xCandCount[p][r] == 0 )	
				noAnswer = true;
			if( --yCandCount[q][r] == 0 )   
				noAnswer = true;	
			if( --zCandCount[Calc.zone(p,q)][r] == 0 )  
				noAnswer = true;
			
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isCand( int x, int y, int r ) {
		return cand[x][y][r];
	}
	
	public int[][] getNum() {
		return num;
	}
	
	public int getNum( int x, int y ) {
		return num[x][y];
	}

	public void setNum( int x, int y, int r ) {
		num[x][y] = r;
	}
	
	public void setNum( int[][] prob ) {
		Calc.copyBoard( prob, num );
	}

	public void getNum( int[][] prob ) {
		Calc.copyBoard( num, prob );
	}
	
	public int getGCandCount( int x, int y ) {
		return gCandCount[x][y];
	}
	
	public int getXCandCount( int x, int y, int r ) {
		return xCandCount[x][r];
	}
	public int getXCandCount( int x, int r ) {
		return xCandCount[x][r];	
	}
	
	public int getYCandCount( int x, int y, int r ) {
		return yCandCount[y][r];
	}
	public int getYCandCount( int y, int r ) {
		return yCandCount[y][r];	
	}
	
	public int getZCandCount( int x, int y, int r ) {
		return zCandCount[Calc.zone(x,y)][r];
	}

	public int getZCandCount( int z, int r ) {
		return zCandCount[z][r];
	}
	
	public  boolean isXExist( int x, int y, int r ) {
		return xExist[x][r];
	}
	public  boolean isXExist( int x, int r ) {
		return xExist[x][r];
	}
	
	public  boolean isYExist( int x, int y, int r ) {
		return yExist[y][r];
	}
	public  boolean isYExist( int y, int r ) {
		return yExist[y][r];
	}
	
	public  boolean isZExist( int x, int y, int r ) {
		return zExist[Calc.zone(x,y)][r];
	}
	
	public boolean isZExist( int z, int r ) {
		return zExist[z][r];
	}
	
	public boolean isNoAnswer() {
		return noAnswer;
	}
	
	public int getSpaceCount() {
		return spaceCount;
	}
	
	public void printboard() {
		System.out.println("num:");
		printboard(num);
	}
		
	public void printGCandCount() {
		System.out.println("gCand");
		printboard( gCandCount );
	}
	
	public static void printboard( int[][] num ) {
		for( int y=0; y<Const.size; ++y ) {
			for( int x=0; x<Const.size; ++x) {
				if( num[x][y] == 0 )
					System.out.print( ". " );
				else
					System.out.print( num[x][y] + " " );
			}
			System.out.println();
		}
	}

	public void printXCandCount() {
		System.out.println("xCandCount");
		printXYZCand( xCandCount );
	}
	
	public void printXYZCand( int[][] candcount ) {
		for( int j=0; j<Const.size; ++j ) {
			for( int k=1; k<=Const.size; ++k ) {
				System.out.print( candcount[j][k] + " ");
			}
			System.out.println();
		}
	}
	
	public static void printhint( boolean[][] hint ) {
		for( int y=0; y<Const.size; ++y ) {
			for( int x=0; x<Const.size; ++x) {
				System.out.print( hint[x][y] ? "X " : ". ");
			}
			System.out.println();
		}
	}

}
