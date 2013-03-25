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

public class Problem {
	private	Status	state = new Status();
	
	private	boolean	unique;		// 唯一解フラグ
	
	private	int[][] 	solution = new int[Const.size][Const.size];
	private	int[][]		question = new int[Const.size][Const.size];
	
	private	boolean[][]	hint = new boolean [Const.size][Const.size];

	//-----------------------------------------------------------------
	// リセット処理
	//-----------------------------------------------------------------
	public void reset() {
		state.clear(hint);
	}
	
	//-----------------------------------------------------------------
	// Status
	//-----------------------------------------------------------------
	public Status getState() {
		return state;
	}
	
	//-----------------------------------------------------------------
	// 唯一解に対する処理
	//-----------------------------------------------------------------
	public boolean isUnique() {
		return unique;
	}
	
	public void setUnique(boolean uni) {
		unique = uni;
	}
	
	//-----------------------------------------------------------------
	// ヒントの配置に関する処理
	//-----------------------------------------------------------------
	public boolean isHint( int x, int y ) {
		return hint[x][y];
	}
	
	// ヒントを戻り値で戻す
	public boolean[][] getHint() {
		return hint;
	}

	// ヒントの内容を、引数で渡されたヒント配列に代入する
	public void getHint(boolean[][] _hint) {
		copyHint(hint, _hint);
	}

	public void setHint(boolean[][] _hint) {
		copyHint(_hint, hint);
	}
	
	private void copyHint(boolean[][] fromhint, boolean[][] tohint) {
		for( int i=0; i<Const.size; ++i )
			for( int j=0; j<Const.size; ++j )
				tohint[i][j] = fromhint[i][j];
	}
	
	//-----------------------------------------------------------------
	// 解に対する処理
	//-----------------------------------------------------------------
	public int[][] getSolution() {
		return solution;
	}
	
	public void getSolution( int[][] sol ) {
		Calc.copyBoard( solution, sol );
	}

	public void setSolution( int[][] sol ) {
		Calc.copyBoard( sol, solution );
	}
	
	public void printSolution() {
		System.out.println("Solution");
		Status.printboard( solution );
	}
	
	//-----------------------------------------------------------------
	// 問に対する処理
	//-----------------------------------------------------------------
	public int[][] getQuestion() {
		return question;
	}
	
	public void setQuestion( int[][] que ) {
		Calc.copyBoard( que,  question );
	}
	
	public void printQuestion() {
		System.out.println("Question");
		Status.printboard( question );
	}
}
