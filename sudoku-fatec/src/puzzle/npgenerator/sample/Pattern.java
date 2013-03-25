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

import puzzle.npgenerator.v1.*;

class Pattern {
	
	private	final int LOWERLIMIT = 16;

	private boolean[][] pattern = new boolean[Const.size][Const.size];
	
	// デモ（Auto)のために必要
	public boolean[][] generateHint() {
		for(;;) {
			int	count = Calc.randomInt(29-22+1)+ 22;
			generate(count);

			if( isPossiblePattern() )
				break;
		}
		return pattern;
	}

	// ヒント数を指定してヒントパターンを作成
	public boolean[][] generate(int hintcount) {
		clear();
		int	count = 0;

		if( (hintcount % 2) == 1) {	// 奇数処理　中央に置く
			pattern[4][4] = true;
			++count;
		}
		
		boolean foursymmetric  = Calc.randomInt(4) < 1;
		boolean eightsymmetric = Calc.randomInt(4) < 1;
		
		while( count < hintcount ) {
			int x = Calc.randomInt(Const.size);
			int y = Calc.randomInt(Const.size);
			if( pattern[x][y] || (x==4 && y==4) )
				continue;
			int z = Calc.zone(x,y);
			if( countHintsZ(z) > (z==4?3:5) || countHintsX(x) > (x==4?4:5) || countHintsY(y) > (y==4?4:5) )
				continue;
			
			pattern[x][y] = true;
			++count;
			pattern[8-x][8-y] = true;
			++count;
			if( foursymmetric && (hintcount - count)>=2) {
				pattern[y][8-x] = true;
				++count;
				pattern[8-y][x] = true;
				++count;
			}
			if( eightsymmetric && (hintcount - count)>=4 ) {
				if( ! pattern[8-x][y] ) {
					pattern[8-x][y] = true;
					++count;
				}
				if( ! pattern[x][8-y] ) {
					pattern[x][8-y] = true;
					++count;
				}
				if( ! pattern[8-y][8-x] ) {
					pattern[8-y][8-x] = true;
					++count;
				}
				if( ! pattern[y][x] ) {
					pattern[y][x] = true;
					++count;
				}			
			}
		}
		
		return pattern;
	}
	
	private void clear() {
		for( int x=0; x<Const.size; ++x )
			for( int y=0; y<Const.size; ++y )
				pattern[x][y] = false;
	}

	public int getHintCount() {
		int	n=0;
		for( int x=0; x<Const.size; ++x )
			for( int y=0; y<Const.size; ++y )
				if( pattern[x][y] )
					++n;
		return n;
	}
	
	public void printPattern(boolean [][] pat) {
		System.out.println("pattern");
		for( int y=0; y<Const.size; ++y ) {
			for( int x=0; x<Const.size; ++x) {
				System.out.print( pat[x][y] ? "X " : ". " );
			}
			System.out.println();
		}
	}

	//------------------------------------------------------------
	// パターンチェック（明らかに不可能なものを除く）	
	public  boolean	 isPossiblePattern( boolean[][] hint ) {
		for( int x=0; x<Const.size; ++x )
			for( int y=0; y<Const.size; ++y )
				pattern[x][y] = hint[x][y];
		
		return isPossiblePattern();
	}
	
	public boolean isPossiblePattern() {
		
		if( getHintCount() < LOWERLIMIT )		// ヒントが少なすぎる
			return false;
		
		int[] xh = new int[Const.size];
		int[] yh = new int[Const.size];
		int[] zh = new int[Const.size];
		for( int i=0; i<Const.size; ++i ) {
			xh[i] = countHintsX(i);
			yh[i] = countHintsY(i);
			zh[i] = countHintsZ(i);
		}

		// 空白縦列、横列チェック
		for( int i=0; i<Const.size; i+=3 ) {
			int xzerocount = 0;
			int yzerocount = 0;
			for( int j=0; j<Const.bsize; ++j ) {
				if( xh[i+j] == 0 )	++xzerocount;
				if( yh[i+j] == 0 )	++yzerocount;
			}
			if( xzerocount > 1 || yzerocount > 1 )
				return false;
		}
		
		int zhzerocount = 0;
		int[] zhxz = new int[Const.bsize];
		int[] zhyz = new int[Const.bsize];
		for( int i=0; i<Const.size; ++i )
			if( zh[i] == 0 ) {
				++ zhzerocount;
				++ zhxz[i/Const.bsize];
				++ zhyz[i%Const.bsize];
			}
		
		if( zhzerocount >= 3 ) {
			for( int i=0; i<Const.size; ++i )
				if( zh[i] == 0 ) {
					if( zhxz[i/Const.bsize] == 2 && zhyz[i%Const.bsize] == 2 )
						return false;
				}
		}

		if( zhzerocount >= 2 ) {
			for( int i=0; i<Const.bsize; ++i ) {
				if( (zhxz[i] == 2) && (zh[i*3]+zh[i*3+1]+zh[i*3+2] < 4) )
					return false;
				if( (zhyz[i] == 2) && (zh[i%3]+zh[i%3+3]+zh[i%3+6] < 4) )
					return false;
			}
		}

		
		return true;
	}

	private int countHintsX( int i ) {
		int n = 0;
		for( int j=0; j<Const.size; ++j )
			n += pattern[i][j] ? 1 : 0;
		return n;
	}
	private int countHintsY( int j ) {
		int n = 0;
		for( int i=0; i<Const.size; ++i )
			n += pattern[i][j] ? 1 : 0;
		return n;
	}
	private int countHintsZ( int i ) {
		int n = 0;
		for( int j=0; j<Const.size; ++j ) {
			int x = Calc.ztox(i, j);
			int y = Calc.ztoy(i, j);
			n += pattern[x][y] ? 1 : 0;
		}
		return n;
	}
} 
