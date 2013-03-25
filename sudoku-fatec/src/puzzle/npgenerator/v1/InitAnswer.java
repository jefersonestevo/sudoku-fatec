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

class InitAnswer {
	private int[][] answer = new int[Const.size][Const.size];
	private boolean change[] = new boolean[Const.size];
	
    public void get( int[][] ans ) {
    	Calc.copyBoard( answer, ans );
    }
	
	public void make() {
		for(int k = 1; k <= Const.size; k++)
			for (int i = 0; i < Const.bsize; i++)
				for (int j = 0; j < Const.bsize; j++) {
					answer[Const.bsize * j + (i + k) % Const.bsize ]
					       [Const.bsize * i + (j + k + (k / Const.bsize)) % Const.bsize] = k;
				}

		for (int i = 0; i < Const.size; i++)
			for (int j = 0; j < Const.size; j++) {
				int r1 = i;
				int n1 = answer[r1][j];
				int a = (int) (Math.random() * Const.bsize);
				int r2 = Const.bsize * (r1 / Const.bsize) + a;
				int n2 = answer[r2][j];
				if (n1 != n2) {
					for (int k = 0; k < Const.size; k++)
						change[k] = false;
					change[j] = true;

					while (n1 != n2) {
						int k = 0;
						while (answer[r1][k] != n2)
							k++;
						change[k] = true;
						n2 = answer[r2][k];
					}
					for (int k = 0; k < Const.size; k++)
						if (change[k] == true) {
							int wk = answer[r1][k];
							answer[r1][k] = answer[r2][k];
							answer[r2][k] = wk;
						}
				}
			}

		for (int i = 0; i < Const.size; i++)
			for (int j = 0; j < Const.size; j++) {
				int c1 = j;
				int n1 = answer[i][c1];
				int a = (int) (Math.random() * Const.bsize);
				int c2 = Const.bsize * (c1 / Const.bsize) + a;
				int n2 = answer[i][c2];
				if (n1 != n2) {
					for (int k = 0; k < Const.size; k++)
						change[k] = false;
					change[i] = true;

					while (n1 != n2) {
						int k = 0;
						while (answer[k][c1] != n2)
							k++;
						change[k] = true;
						n2 = answer[k][c2];
					}
					for (int k = 0; k < Const.size; k++)
						if (change[k] == true) {
							int wk = answer[k][c1];
							answer[k][c1] = answer[k][c2];
							answer[k][c2] = wk;
						}
				}
			}
	}
}
