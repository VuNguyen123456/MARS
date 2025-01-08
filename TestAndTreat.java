//TO DO: Finish this!

// You may NOT add any arrays as a member variable in this class

// You MAY add an import to java.util.Comparable if
// you are making the Person class Comparable. 
// No other imports are allowed.
/**
 * This is Scenario 2 of the simulation.
 * @author Vu Nguyen
 */
class TestAndTreat extends TreatAll {
	//extend the TreatAll class to work for the test-and-treat scenario
	//you might want to use a similar model
	/**
	 * testing time .
	 */
	protected final int testingTime;
	/**
	 * the testing line.
	 */
	protected Line<Person> testingLine = new Line<>();
	//protected Line<Person> treatmentLine = new Line<>();
	/**
	 * moving clock for treatmenttime.
	 */
	protected int movingTreatmentTime = getTreatmentTime();
	/**
	 * moving clock for testingtime.
	 */
	protected int movingTestingTime = 0; // might need to make this 0

	
	/** 
	 * Get the testing time.
	 * @return int
	 */
	protected int getTestingTime(){
		return this.testingTime;
	}
	// protected  int getTreatmentTime(){
	// 	return treatmentTime;
	// }
	/**
	 * The Constructor of the class.
	 * This innitialize a testandtreat reusing the constructor from testAlll.
	 * @param testingTime testing time
	 * @param treatmentTime treatment time
	 */
	public TestAndTreat(int testingTime, int treatmentTime) {
		super(treatmentTime); //replace this default!
		this.testingTime = testingTime;
	}

	
	
	/** 
	 * return the pending people that are standing in line for both line.
	 * @return int
	 */
	@Override
	public int getPending() {
		//see comments in Scenario.java
		int pendingPeople = testingLine.getSize();
		pendingPeople += treatmentLine.getSize();
		//O(1)
		
		return pendingPeople; //replace this default return!
	}
	/**
	 * Add the person into the testing line to start.
	 */
	@Override
	public void addPerson(Person p) {
		if(getPending() >= 0){
			testingLine.add(p, testingLine.getSize());
		}
	}
	/**
	 * Add person from testline into treatment line if < 40%.
	 * In the order of not changing the person being treat and if test result is high them put them high
	 * @param p the person need to added
	 */
	protected void addToTreatmentLine(Person p){
		if(p.getTestResult() < 0.4){
			numSafe += 1;
			return;
		}
		if(treatmentLine.getSize() == 0){
			treatmentLine.add(p, 0);
			return;
		}
		for(int i = 1; i <treatmentLine.getSize(); i++){
			if(treatmentLine.get(i).getTestResult() < p.getTestResult()){
				treatmentLine.add(p, i);
				return;
			}
			else if(treatmentLine.get(i).getTestResult() == p.getTestResult()){
				treatmentLine.add(p, i+1);
				return;
			}
		}
		treatmentLine.add(p, treatmentLine.getSize());
	}

	/**
	 * Moving the time by 1.
	 */
	@Override
	public void tick(){
		//ticking everyone in the line first
		for(int i = 0; i < testingLine.getSize(); i++){
			testingLine.get(i).tick();
		}

		for(int i = 0; i < treatmentLine.getSize(); i++){
			treatmentLine.get(i).tick();
		}

		//In testingline, moving people into treatment or check if they are dead or not
		int proceedTreatment = 1;
		if(movingTestingTime >0){
			movingTestingTime -= 1;//Time is not moving or something
		}
		for(int i = 0; i < testingLine.getSize(); i++){
			System.out.println("It is in the testline loop");
			System.out.println("movingTestingTime: " + movingTestingTime);
			//testingLine.get(i).tick();
			if(movingTestingTime == 0){
				addToTreatmentLine(testingLine.get(0));
				testingLine.remove(0);
				i--;
				movingTestingTime = getTestingTime();
				if(treatmentLine.getSize() == 1){
					proceedTreatment = 0;
				} 
				continue;
			}
			if(testingLine.getSize() != 0 && testingLine.get(i).isDead()){
				testingLine.remove(i);
				numDeaths += 1;
				i--;
			}
		}
		if(testingLine.getSize() == 0){
			movingTestingTime = getTestingTime();
		}

		//During treatmentLine
		if(treatmentLine.getSize() != 0 && proceedTreatment == 1){
			movingTreatmentTime -= 1;
			for(int i = 0; i < treatmentLine.getSize(); i++){
				//treatmentLine.get(i).tick();
				if(treatmentLine.get(i).isDead()){
					treatmentLine.remove(i);
					numDeaths += 1;
					if(i == 0){
						movingTreatmentTime = getTreatmentTime();
					}
					//i -= 1
					i-= 1;
				}
			}	
		}
		//handle treatment being administered
		//handle person successfully treated and if the person on the head die and is the only one that was in the line
		if(movingTreatmentTime == 0){
			treatmentLine.remove(0);
			numSafe += 1;
			movingTreatmentTime = getTreatmentTime();
		}
		//Keeping the treatmentTime the same if it an empty line
		if(treatmentLine.getSize() == 0){
			movingTreatmentTime = getTreatmentTime();
		}
		//Don't forget... if the first person in line dies (the person being treated)
		//start the next person's treatment!
	}
	/**
	 * get the testing line.
	 * @return the line
	 */
	public Line<Person> getTestingLine() {
		return testingLine; //replace this default return!
	}
	/**
	 * return the string esspression of the time need for testing and doctor avalability.
	 * @return the string depends on situation
	 */
	public String toString() {
		//Replace "??" with the time until the next test is available and
		//the number of minutes until the doctor is free
		if(movingTreatmentTime == getTreatmentTime() && treatmentLine.getSize() == 0 && movingTestingTime == getTestingTime() && testingLine.getSize() == 0){
			return "Scenario 2: Test available in " + "0" + " minute(s). Doctor Free in " + "0" + " minute(s).";
		}
		if(movingTreatmentTime == getTreatmentTime() && treatmentLine.getSize() == 0){
			return "Scenario 2: Test available in " + movingTestingTime + " minute(s). Doctor Free in " + "0" + " minute(s).";
		}
		if(movingTestingTime == getTestingTime() && testingLine.getSize() == 0){
			return "Scenario 2: Test available in " + "0" + " minute(s). Doctor Free in " + movingTreatmentTime + " minute(s).";
		}
		return "Scenario 2: Test available in " + movingTestingTime + " minute(s). Doctor Free in " + movingTreatmentTime + " minute(s).";
	}
}