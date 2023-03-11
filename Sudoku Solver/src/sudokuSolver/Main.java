package sudokuSolver;

import java.util.Scanner;

public class Main {

	public static void main(String[] args)throws Exception{
		String level;
		level = "hard";
		Board puzzle = new Board();
		puzzle.loadPuzzle(level);
		System.out.println("      Unsolved");
		puzzle.display();
		System.out.println();
		System.out.println("       Solved");
		puzzle.logicCycles();
		puzzle.display();
	}

}
