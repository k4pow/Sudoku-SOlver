package sudokuSolver;

import java.io.File;
import java.util.Scanner;
import java.util.Stack;

public class Board {

	private Cell[][] board = new Cell[9][9];
	private String level;
	private Cell[][][] saves = new Cell[81][9][9];
	private int guessCounter = 0;

	public Board() {
		
		for(int i = 0; i < 9; i++)
			for(int j = 0; j < 9; j++) {
				board[i][j] = new Cell();
				board[i][j].setBoxID(3 * (i / 3) + (j) / 3 + 1);
			}
	}

	public void loadPuzzle(String level) throws Exception {
		
		this.level = level;
		String levelfile = "easyPuzzle.txt";
		if (level.equals("medium"))
			levelfile = "mediumPuzzle.txt";
		else if (level.equals("hard"))
			levelfile = "hardPuzzle.txt";
		else if (level.equals("oni"))
			levelfile = "oniPuzzle.txt";

		Scanner input = new Scanner(new File(levelfile));

		for(int i = 0; i < 9; i++)
			for(int j = 0; j < 9; j++) {
				int number = input.nextInt();
				if(number != 0)
					solve(i, j, number);
			}
	}

	public boolean isSolved() {
		
		for(int i = 0; i < 9; i++)
			for(int j = 0; j < 9; j++)
				if(board[i][j].getNumber() == 0 || board[i][j].getNumberOfPotentials() > 1)
					return false;
		return true;
	}

	public void display() {
		
		for(int i = 0; i < 9; i++) {
			if(i % 3 == 0 && i != 0)
				System.out.println("------+-------+------");
			for(int j = 0; j < 9; j++) {
				if (j % 3 == 0 && j != 0)
					System.out.print("| ");
				System.out.print(board[i][j].getNumber() + " ");
			}
			System.out.println();
		}
	}

	public void solve(int x, int y, int number) {
		
		board[x][y].setNumber(number);
		for(int m = 0; m < 9; m++)
			if (m != y)
				board[x][m].cantBe(number);
		for(int n = 0; n < 9; n++)
			if (n != x)
				board[n][y].cantBe(number);
		for(int n = 0; n < 9; n++)
			for(int m = 0; m < 9; m++) {
				if(n == x && m == y)
					continue;
				else if(board[n][m].getBoxID() == board[x][y].getBoxID()) {
					board[n][m].cantBe(number);
				}

			}
	}

	public void revert() {
		
		guessCounter--;
		for(int i = 0; i < 9; i++)
			for(int j = 0; j < 9; j++) {
				//System.out.println(i + " " + j + " " + guessCounter);
				board[i][j].setNumber(saves[guessCounter][i][j].getNumber());
				board[i][j].setPotential(saves[guessCounter][i][j].getPotential());
			}	
		for(int i = 0; i < 9; i++)
			for(int j = 0; j < 9; j++)
				if(board[i][j].getNumberOfPotentials() != 1) {
					board[i][j].cantBe(board[i][j].getFirstPotential());
					return;
				}

	}

	public void guess() {
		
		for(int i = 0; i < 9; i++)
			for(int j = 0; j < 9; j++)
				if(board[i][j].getNumberOfPotentials() != 1) {
					solve(i, j, board[i][j].getFirstPotential());
					return;
				}
	}

	public void copy() {
		
		for(int i = 0; i < 9; i++)
			for(int j = 0; j < 9; j++) {
				saves[guessCounter][i][j] = new Cell();
				saves[guessCounter][i][j].setNumber(board[i][j].getNumber());
				saves[guessCounter][i][j].setPotential(board[i][j].getPotential());
			}
		guessCounter++;
	}

	public boolean boxCheck(int x, int y, int number) {
		
		int locationX = x % 3;
		int locationY = y % 3;
		for(int i = (x - locationX); i < (x - locationX + 3); i++)
			for(int j = (y - locationY); j < (y - locationY + 3); j++)
				if(i != x || j != y)
					if(board[x][y].getBoxID() == board[i][j].getBoxID())
						if(board[i][j].canBe(number))
							return false;
		return true;

	}

	public void logicCycles() throws Exception {

		while(isSolved() == false) {

			int changesMade = 0;

			do {
				changesMade = 0;
				changesMade += logic1();
				changesMade += logic2();
				changesMade += logic3();
				changesMade += logic4();
				if (errorFound())
					break;
			}while(changesMade != 0);
			if(errorFound())
				revert();
			else{
				copy();
				guess();
			}
		}
	}

	public int logic1() {
		
		int changesMade = 0;
		for(int x = 0; x < 9; x++)
			for(int y = 0; y < 9; y++)
				if(board[x][y].getNumberOfPotentials() == 1 && board[x][y].getNumber() == 0) {
					solve(x, y, board[x][y].getFirstPotential());
					changesMade++;
				}
		return changesMade;
	}

	public int logic2() {

		boolean solvable = true;
		int changesMade = 0;

		for(int x = 0; x < 9; x++)
			for(int y = 0; y < 9; y++)
				if(board[x][y].getNumberOfPotentials() > 1 && board[x][y].getNumber() == 0)
					for(int i = 0; i <= 9; i++)
						if(board[x][y].canBe(i)) {
							for(int j = 0; j < 9; j++) {
								if(j == y)
									continue;
								else if(board[x][j].canBe(i)) {
									solvable = false;
									break;
								}
							}
							if(solvable == true) {
								solve(x, y, i);
								changesMade++;
							}

						}
		for(int x = 0; x < 9; x++)
			for(int y = 0; y < 9; y++) {
				if(board[x][y].getNumberOfPotentials() > 1 && board[x][y].getNumber() == 0)
					for(int i = 0; i <= 9; i++) {
						if(board[x][y].canBe(i)) {
							for(int j = 0; j < 9; j++) {
								if(j == x)
									continue;
								else if(board[j][x].canBe(i)) {
									solvable = false;
									break;
								}
							}
							if(solvable == true) {
								solve(x, y, i);
								changesMade++;
							}
						}
					}
			}

		return changesMade;
	}

	public int logic3() {

		boolean solvable = true;
		int changesMade = 0;

		for(int x = 0; x < 8; x++)
			for(int y = 0; y < 9; y++)
				if(board[x][y].getNumberOfPotentials() > 1 && board[x][y].getNumber() == 0)
					for(int i = 0; i <= 9; i++)
						if(board[x][y].canBe(i)) {
							solvable = boxCheck(x, y, i);
							if(solvable == true) {
								solve(x, y, i);
								changesMade++;
							}
						}

		return changesMade;
	}

	public int logic4() {
		
		int changesMade = 0;

		for (int x = 0; x < 9; x++)
			for (int y = 0; y < 9; y++)
				if (board[x][y].getNumberOfPotentials() == 2)
					for (int i = y + 1; i < 9; i++)
						if (board[x][i].getNumberOfPotentials() == 2)
							if (board[x][y].getFirstPotential() == board[x][i].getFirstPotential())
								if (board[x][y].getNextPotential() == board[x][i].getNextPotential())
									for (int j = 0; j < 9; j++) {
										if (j == y || j == i)
											continue;
										if (board[x][j].canBe(board[x][y].getFirstPotential())) {
											board[x][j].cantBe(board[x][y].getFirstPotential());
											changesMade++;
										}
										if (board[x][j].canBe(board[x][y].getNextPotential())) {
											board[x][j].cantBe(board[x][y].getNextPotential());
											changesMade++;
										}

									}

		for(int y = 0; y < 9; y++)
			for(int x = 0; x < 9; x++)
				if(board[x][y].getNumberOfPotentials() == 2)
					for(int i = x + 1; i < 9; i++)
						if(board[i][y].getNumberOfPotentials() == 2)
							if(board[x][y].getFirstPotential() == board[i][y].getFirstPotential())
								if(board[x][y].getNextPotential() == board[i][y].getNextPotential())
									for(int j = 0; j < 9; j++) {
										if(j == x || j == i)
											continue;
										if(board[j][y].canBe(board[x][y].getFirstPotential())) {
											board[j][y].cantBe(board[x][y].getFirstPotential());
											changesMade++;
										}
										if(board[j][y].canBe(board[x][y].getNextPotential())) {
											board[j][y].cantBe(board[x][y].getNextPotential());
											changesMade++;
										}
									}
		int locationX, locationY;
		for(int x = 0; x < 9; x++)
			for(int y = 0; y < 9; y++)
				if(board[x][y].getNumberOfPotentials() == 2) {
					// searches box only
					locationX = x % 3;
					locationY = y % 3;
					for(int i = (x - locationX); i < (x - locationX + 3); i++)
						for(int j = (y - locationY); j < (y - locationY + 3); j++) {
							if(i == x && j == y)
								continue;
							if(board[i][j].getNumberOfPotentials() == 2)
								if(board[i][j].getFirstPotential() == board[x][y].getFirstPotential())
									if(board[i][j].getNextPotential() == board[x][y].getNextPotential())
										for(int n = (x - locationX); n < (x - locationX + 3); n++)
											for(int m = (y - locationY); m < (y - locationY + 3); m++)
												if(!((n == x && m == y) || (n == i && m == j))) {
													if(board[n][m].canBe(board[x][y].getFirstPotential())) {
														board[n][m].cantBe(board[x][y].getFirstPotential());
														changesMade++;
													}
													if(board[n][m].canBe(board[x][y].getNextPotential())) {
														board[n][m].cantBe(board[x][y].getNextPotential());
														changesMade++;
													}
												}
						}
				}
		return changesMade;
	}

	public boolean errorFound() {
		
		for(int x = 0; x < 9; x++)
			for(int y = 0; y < 9; y++)
				if(board[x][y].getNumberOfPotentials() == 0)
					return true;
		return false;
	}
}
