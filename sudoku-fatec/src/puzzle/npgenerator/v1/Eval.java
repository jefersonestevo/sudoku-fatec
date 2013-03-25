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

public class Eval {
	private	Status	state = new Status();
	private	boolean 	changed;

	private	int		zfixcount;
	private	int		xfixcount;
	private	int		yfixcount;
	private	int		gfixcount;
	
	private	int	[]	zSpaceCount = new int[Const.size];
	private	int[]	xSpaceCount	= new int[Const.size];
	private	int[]	ySpaceCount	= new int[Const.size];
	
	// Status型のmatrix が与えられたとき、解答後の状態を返す
	public Status solve( int[][] pr ) {
		init( pr );

//		state.printboard();
		
		changed = true;
		while( !state.isNoAnswer() && changed && state.getSpaceCount()>0 ) {
			changed = false;

			searchAllCand(1);			if( changed )	continue;
			searchAllCand(2);			if( changed )	continue;
			searchAllCand(3);			if( changed )	continue;
			
			searchZCandOne(9);			if( changed )	continue;

			searchYCandOne(9);			if( changed )	continue;

			searchXCandOne(9);			if( changed )	continue;
			
			searchGCandOne();
		}

		return state;
	}

	// 難易度ポイントを返す
	public	int	getPoint() {
		return	zfixcount + xfixcount*3 + yfixcount*3 + gfixcount*15;
	}
	
	private void init( int[][] pr ) {
		state = new Status();
		state.clear( null );
		for( int i=0; i<Const.size; ++i )
			zSpaceCount[i] = xSpaceCount[i] = ySpaceCount[i] = Const.size;
		zfixcount = xfixcount = yfixcount = gfixcount = 0;
		
		for( int x=0; x<Const.size; ++x )
			for( int y=0; y<Const.size; ++y ) {
				if( pr[x][y] <= 0 )
					continue;
				state.assignNum(x,y,pr[x][y]);
				deleteCand(x,y);
				decSpaceCounts( '_', x, y, pr[x][y] );
			}
	}

	private void searchAllCand( int lim ) {
		searchZCandOne( lim );		if( changed )	return;	
		searchYCandOne( lim );		if( changed )	return;
		searchXCandOne( lim );		if( changed )	return;		
	}
	
	private void searchZCandOne( int lim ) {
		for( int i=0; i<Const.size; ++i ) {
			if( zSpaceCount[i]  == 0 || zSpaceCount[i] > lim )
				continue;
			for( int r=1; r<=Const.size; ++r )
				if( state.getZCandCount(i,r) == 1 )
					if( !state.isZExist(i,r) ) {
						assignUniqueZ(i,r);	
						changed = true;
						return;
					}
		}
	}
	
	// 横列に対する処理
	private void searchYCandOne( int lim ) {
		for( int i=0; i<Const.size; ++i ) {
			if( ySpaceCount[i]  == 0 || ySpaceCount[i] > lim )
				continue;
			for( int r=1; r<=Const.size; ++r )
				if( state.getYCandCount(i,r) == 1 )
					if( !state.isYExist(i,r) ) {
						assignUniqueY(i,r);	
						changed = true;
						return;
					}
		}
	}

	// 縦列に対する処理
	private void searchXCandOne( int lim ) {
		for( int i=0; i<Const.size; ++i ) {
			if( xSpaceCount[i]  == 0 || xSpaceCount[i] > lim )
				continue;
			for( int r=1; r<=Const.size; ++r )
				if( state.getXCandCount(i,r) == 1 )
					if( !state.isXExist(i,r) ) {
						assignUniqueX(i,r);	
						changed = true;
						return;
					}
		}
	}

	// マス単独の処理
	private void searchGCandOne() {
		for( int x=0; x<Const.size; ++x )
			for( int y=0; y<Const.size; ++y )
				if( state.getGCandCount(x, y) == 1 )
					if( state.getNum(x,y) == 0 ) {
						assignUniqueG(x,y);
						changed = true;
						return;
					}
	}
	
	// state の　(p,q)の情報から、候補を消す
	// 候補を消して新たに決まる個所に数字を配置
	private void deleteCand( int p, int q ) {
		int r = state.getNum( p, q );
		if( r <= 0 )		
			return;			// 数字が入っていないマスは無視
		
//		System.out.println( "("+p+","+q+")="+r );
		
		// そのマスに関する処理
		for( int k=1; k<=Const.size; ++k ) {
			if( k!= r ) {
				state.deleteCand( p, q, k );
			}
		}
		
		// そのマスの横列に関する処理
		for( int x=0; x<Const.size; ++x ) {
			if( x != p ) {
				state.deleteCand( x, q, r );
			}
		}

		// そのマスの縦列に関する処理
		for( int y=0; y<Const.size; ++y ) {
			if( y != q ) {
				state.deleteCand( p, y, r );
			}
		}
		
		// そのマスのブロックに関する処理
		int baseX = (p/Const.bsize)*Const.bsize;
		int baseY = (q/Const.bsize)*Const.bsize;		
		for( int x=baseX; x<baseX+Const.bsize; ++x )
			for( int y=baseY; y<baseY+Const.bsize; ++y ) {
				if( (x!=p) || (y!=q) ) {
					state.deleteCand( x, y, r );
				}
			}		
	}

	private void decSpaceCounts( char tc, int x, int y, int v ) {
//		System.out.println( "assign" + tc + "["+x+"]["+y+"]="+ v );
		int	z = Calc.zone(x,y);
		--zSpaceCount[z];
		--xSpaceCount[x];
		--ySpaceCount[y];

		if( zSpaceCount[z]==0 || xSpaceCount[x]==0 || ySpaceCount[y]==0 )
			return;
		
		switch( tc ) {
		case '_':  break;
		case 'Z':	++zfixcount;
		    if( zSpaceCount[z] > 3 )	++zfixcount;
			break;
		case 'X':	++xfixcount;
			if( xSpaceCount[x] > 2 )	++xfixcount;
			break;
		case 'Y':	++yfixcount;
			if( ySpaceCount[y] > 2 )	++yfixcount;
			break;
		case 'G':	++gfixcount;
			break;
		}
	}
	
	// x列でrの入る個所にrを入れる
	private void assignUniqueX( int p, int r ) {
		int j = 0;
		while( !state.isCand(p,j,r) )
			j++;
		if( state.assignNum(p,j,r)) {
			deleteCand(p,j);
			decSpaceCounts( 'X', p, j, r );
		}
	}

	// y列でrの入る個所にrを入れる
	private void assignUniqueY( int q, int r ) {
		int i = 0;
		while( !state.isCand(i,q,r) )
			i++;
		if( state.assignNum(i,q,r)) {
			deleteCand(i,q);
			decSpaceCounts( 'Y', i, q, r );
		}
	}

	// Zブロック内でrの入る個所にrを入れる
	private void assignUniqueZ( int i, int r ) {
		int j = 0;
		
		while( !state.isCand( Calc.ztox(i,j),Calc.ztoy(i,j),r) )
			++j;

		int x = Calc.ztox(i,j);
		int y = Calc.ztoy(i,j);
		if( state.assignNum( x, y ,r) ) {
			deleteCand( x, y );
			decSpaceCounts( 'Z', x, y, r );
		}
	}
	
	// (p,q)に残った唯一の候補を入れる
	private void assignUniqueG( int x, int y ) {
		int k=1;
		while( ! state.isCand(x,y,k) )
			++k;
		if( state.assignNum(x,y,k) ) {
			deleteCand(x,y);
			decSpaceCounts( 'G', x, y, k );
		}
	}
}
