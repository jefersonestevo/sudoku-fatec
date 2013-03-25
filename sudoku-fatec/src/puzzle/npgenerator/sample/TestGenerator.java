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

public class TestGenerator {

	private static Generator	generator = new Generator();
	private static	 Problem	problem;

	private static int[][] initproblem = {
			{0,0,3,0,0,0,7,0,0},
			{0,2,0,4,0,6,0,8,0},
			{1,0,0,0,5,0,0,0,9},
			{0,4,0,0,0,0,0,1,0},
			{0,0,6,0,0,0,2,0,0},
			{0,1,0,0,0,0,0,3,0},
			{8,0,0,0,1,0,0,0,4},
			{0,6,0,9,0,2,0,5,0},
			{0,0,7,0,0,0,6,0,0} };	

	public static void main(String[] args) {
	
		int[][]	question = new int[Const.size][Const.size];		// –â‘è
		int[][]	answer   = new int[Const.size][Const.size];		// “š‚¦
		boolean[][] hint = new boolean[Const.size][Const.size];	// ƒqƒ“ƒg

		for( int x=0; x<Const.size; ++x )
			for( int y=0; y<Const.size; ++y )
				hint[x][y] = initproblem[x][y] != 0;
		
		boolean     isunique;
		
		Status.printhint( hint );
		
		long starttime = System.currentTimeMillis();
		
		problem = generator.make( hint );
		
		long endtime = System.currentTimeMillis();
		
		isunique = problem.isUnique();
		answer   = problem.getSolution();
		question = problem.getQuestion();

		System.out.println("time " + (int)(endtime - starttime) + " msec");
		System.out.println("Ans. unique="+isunique );
		Status.printboard( answer );

		if( isunique ) {
			System.out.println();
			Status.printboard( question );	
		}
	}
}
