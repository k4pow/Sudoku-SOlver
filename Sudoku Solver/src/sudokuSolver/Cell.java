package sudokuSolver;

public class Cell {
	
	private int number;
	private boolean[] potential = {false, true, true, true, true, true, true, true, true, true};
	private int boxID;
	
	//USEFUL METHODS:
	public boolean canBe(int number) {
		return potential[number];
	}
	public void cantBe(int number) {
		potential[number] = false;
	}
	public void setCanBe(int number) {
		potential[number] = true;
	}
	
	public int getNumberOfPotentials() {
		int potentials = 0;
		for(int i = 1; i < 10; i++)
			if(potential[i] == true)
				potentials++;
		return potentials;
	}
	
	public int getFirstPotential() {
		for(int i = 1; i < 10; i++)
			if(potential[i] == true)
				return i;
		return -1;
	}
	
	public int getNextPotential() {
		int counter = 1;
		for(int i = 1; i < 10; i++) {
			if(potential[i]==true && counter == 1)
				counter++;
			else if(potential[i]==true && counter == 2)
				return i;
		}
		return -1;
			
	}
	
	//GETTERS AND SETTERS
	public int getNumber() {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
		for(int i = 1; i < 10; i++)
			if(i != number)
				potential[i] = false;
	}
	
	public boolean[] getPotential() {
		return potential;
	}
	public void setPotential(boolean[] potential) {
		for(int i = 0; i < 10; i++)
		this.potential[i] = potential[i];
	}
	public int getBoxID() {
		return boxID;
	}
	public void setBoxID(int boxID) {
		this.boxID = boxID;
	}
}
