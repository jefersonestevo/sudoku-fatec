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

class TestSolver {
	private static Status		matrix  = new Status();	
	
	private static Solver solver = new Solver();
	
	final static int[][] question = {

		// ‰ð‚¯‚é
		{0,0,3,0,0,0,7,0,0},
		{0,2,0,4,0,6,0,8,0},
		{1,0,0,0,5,0,0,0,9},
		{0,4,0,0,0,0,0,1,0},
		{0,0,6,0,0,0,2,0,0},
		{0,1,0,0,0,0,0,3,0},
		{8,0,0,0,1,0,0,0,4},
		{0,6,0,9,0,2,0,5,0},
		{0,0,7,0,0,0,6,0,0},
	/*
		// ‰ð‚¯‚È‚¢
		{0,0,0,0,0,3,0,8,0},	
		{0,0,0,0,0,2,4,0,0},
		{5,0,1,0,0,4,0,0,0},
		{7,2,0,0,0,0,0,0,0},
		{9,0,0,0,0,0,0,0,6},
		{0,0,0,0,0,0,0,5,8},
		{0,0,0,7,0,0,2,0,1},
		{0,0,9,6,0,0,0,0,0},
		{0,8,0,5,0,0,0,0,0},
	*/
	};
	
	private static boolean[][] hint = new boolean[Const.size][Const.size];	
	
	private static int[][]	answer   = new int[Const.size][Const.size];		// “š‚¦
	
	public static void main(String[] args) {
		System.out.println("Problem:");
		Status.printboard( question );	
		
		for( int x=0; x<Const.size; ++x )
			for( int y=0; y<Const.size; ++y )
				hint[x][y] = question[x][y] != 0;
		matrix.setNum( question );
		matrix.clear(hint);
		matrix = solver.solve(matrix);	
		
		boolean isnoanswer = matrix.isNoAnswer();
		int spacecount = matrix.getSpaceCount();
		matrix.getNum( answer );

		if( isnoanswer ) {
			System.out.println("no answer!");
		} else {
			if( spacecount > 0 )
				System.out.println( "spacecount = " + spacecount );
			else 
				System.out.println( "Answer:" );

			Status.printboard(answer);
		}
	}
}
