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

public class Solver {
	Status	state = new Status();
	
	// Status�^��matrix ���^����ꂽ�Ƃ��A�𓚌�̏�Ԃ�Ԃ�
	public Status solve( Status matrix ) {

//		System.out.println("<Solver.answer>");
		state.setStatus(matrix);
		
		for( int x=0; x<Const.size; ++x )
			for( int y=0; y<Const.size; ++y )
				deleteCandPeer(x,y);
		
		return state;
	}
	
	// Status�^matrix �� (p,q) �ɒ��ڂ����Ƃ��̉𓚌�̏�Ԃ�Ԃ�
	public Status solveLocal( Status matrix, int p, int q ) {
//		System.out.println("  <Solver.answerLocal>");
		
		state.setStatus(matrix);
		deleteCandPeer(p,q);
		
		return state;
	}

	// state �́@(p,q)�̏�񂩂�A��������
	// ���������ĐV���Ɍ��܂���ɐ�����z�u
	private void deleteCandPeer( int p, int q ) {
//		System.out.println( "deleteCandPeer("+p+","+q+")" );
		
		int r = state.getNum( p, q );
		if( r <= 0 )		
			return;			// �����������Ă��Ȃ��}�X�͖���
		
//		System.out.println( "("+p+","+q+")="+r );
		
		// ���̃}�X�Ɋւ��鏈��
		for( int k=1; k<=Const.size; ++k ) {
			if( k!= r ) {
				if( state.deleteCand( p, q, k ) ) {
					assignUniquePeer( p, q, k );
				}
			}
		}
		
		// ���̃}�X�̉���Ɋւ��鏈��
		for( int x=0; x<Const.size; ++x ) {
			if( x != p ) {
				if( state.deleteCand( x, q, r ) ) {
					assignUniquePeer( x, q, r );					
				}
			}
		}

		// ���̃}�X�̏c��Ɋւ��鏈��
		for( int y=0; y<Const.size; ++y ) {
			if( y != q ) {
				if( state.deleteCand( p, y, r ) ) {
					assignUniquePeer( p, y, r );					
				}
			}
		}
		
		// ���̃}�X�̃u���b�N�Ɋւ��鏈��
		int baseX = (p/Const.bsize)*Const.bsize;
		int baseY = (q/Const.bsize)*Const.bsize;		
		for( int x=baseX; x<baseX+Const.bsize; ++x )
			for( int y=baseY; y<baseY+Const.bsize; ++y ) {
				if( (x!=p) || (y!=q) ) {
					if( state.deleteCand( x, y, r )) {
						assignUniquePeer( x, y, r );
					}
				}
			}		
	}
	
	// x���r�̓������r������
	private void assignUniqueX( int p, int r ) {
		int j = 0;
		while( !state.isCand(p,j,r) )
			j++;
		if( state.assignNum(p,j,r))
			deleteCandPeer(p,j);
	}

	// y���r�̓������r������
	private void assignUniqueY( int q, int r ) {
		int i = 0;
		while( !state.isCand(i,q,r) )
			i++;
		if( state.assignNum(i,q,r))
			deleteCandPeer(i,q);
	}

	// Z�u���b�N����r�̓������r������
	private void assignUniqueZ( int i , int r ) {

		int j = 0;
		
		while( !state.isCand( Calc.ztox(i,j),Calc.ztoy(i,j),r) )
			++j;

		if( state.assignNum( Calc.ztox(i,j),Calc.ztoy(i,j),r) )
			deleteCandPeer( Calc.ztox(i,j), Calc.ztoy(i,j) );
	}
	
	// (p,q)�Ɏc�����B��̌�������
	private void assignUniqueG( int x, int y ) {
		int k=1;
		while( ! state.isCand(x,y,k) )
			++k;
		if( state.assignNum(x,y,k) )
			deleteCandPeer(x,y);
	}
	
	// (p,q) �̎��ӂ̌�₪�P�����Ȃ����𖄂߂�
	private void assignUniquePeer( int p, int q, int r ) {
		if( state.isNoAnswer() )
			return;
		
		if( state.getZCandCount(p,q,r) == 1 )
			if( !state.isZExist(p,q,r) )
				assignUniqueZ(Calc.zone(p,q),r);
		
		if( state.getXCandCount(p,q,r) == 1 )
			if( !state.isXExist(p,q,r))
				assignUniqueX(p,r);

		if( state.getYCandCount(p,q,r) == 1 )
			if( !state.isYExist(p,q,r))
				assignUniqueY(q,r);		
		
		if( state.getGCandCount(p,q) == 1 )
			if( state.getNum(p,q) == 0 )
				assignUniqueG(p,q);

	}
}
