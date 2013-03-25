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

import java.util.Random;

public class Calc {
	public static int zone( int p, int q ) {
		return (p/Const.bsize)*Const.bsize + (q/Const.bsize);
	}
	
	public static int area( int p, int q ) {
		return (p % Const.bsize)*Const.bsize + (q % Const.bsize);
	}
	
	public static int ztox( int i, int j ) {
		return (i / Const.bsize) * Const.bsize + j / Const.bsize;
	}
	
	public static int ztoy( int i, int j ) {
		return (i % Const.bsize) * Const.bsize + j % Const.bsize;
	} 
	
	public static void copyBoard( int[][] frombd, int[][] tobd ) {
		for( int i=0; i<Const.size; ++i )
			for( int j=0; j<Const.size; ++j )
				tobd[i][j] = frombd[i][j];
	}
	
	private static Random	random = new Random();
	
	public static void srand( long seed ) {
		random = new Random(seed);
	}
	
	public static double random() {
		return random.nextDouble();
	}
	
	public static int randomInt( int n ) {
		return random.nextInt(n);
	}
}
