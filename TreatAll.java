/**
 * This is Scenario 1 of the simulation.
 * @author Vu Nguyen
 */
class TreatAll extends Scenario {
	//******************************************************
	//*******  DO NOT EDIT ANYTHING IN THIS SECTION  *******
	//******************************************************
	
	/**
	 *  The time to administer a treatment regimen.
	 */
	protected final int treatmentTime;
	
	/**
	 *  The line for treatment.
	 */
	protected Line<Person> treatmentLine = new Line<>();
	
	/**
	 * Accepts the treatment time.
	 * 
	 * @param treatmentTime the time to administer a treatment regimen
	 */
	public TreatAll(int treatmentTime) {
		this.treatmentTime = treatmentTime;
	}
	
	/**
	 *  Allows the GUI access to the treatment line.
	 *  
	 *  @return the treatment line
	 */
	public Line<Person> getTreatmentLine() {
		return treatmentLine;
	}
	
	//******************************************************
	//*******         MAKE YOUR CHANGES BELOW        *******
	//******************************************************
	
	/**
	 * Return the treatment time of doctor.
	 * @return treatment time
	 */
	protected  int getTreatmentTime(){
		return treatmentTime;
	}

	/**
	 * Setting the time.
	 */
	protected int movingTime = getTreatmentTime();
	
	
	/**
	 * get the size of the pending and return them.
	 * @return 
	 */
	@Override
	public int getPending() {
		//see comments in Scenario.java
		int pendingPeople = treatmentLine.getSize();
		//O(1)
		return pendingPeople; //replace this default return!
	}

	/**
 	* Method to add person in.
 	*/
	@Override
	public void addPerson(Person p) {
		if(getPending() >= 0){
			treatmentLine.add(p, treatmentLine.getSize());
		}
	}

	//The moving time might need a display update to show that they are available when it's 0;
	/**
	 * Moving the time by 1 in treatment line.
	 * Looping through and update each person timeleft.
	 * If there are death remove then or if the person got cure then remove them and update numSafe.
	 */
	@Override
	public void tick() {
		//handle any deaths in line
		movingTime -= 1;
		for(int i = 0; i < treatmentLine.getSize(); i++){
			treatmentLine.get(i).tick();
			if(treatmentLine.get(i).isDead()){
				treatmentLine.remove(i);
				numDeaths += 1;
				if(i == 0){
					movingTime = getTreatmentTime();
				}
				//i -= 1
				i-= 1;
			}
		}
		//handle treatment being administered
		//handle person successfully treated and if the person on the head die and is the only one that was in the line
		if(movingTime == 0){
			treatmentLine.remove(0);
			numSafe += 1;
			movingTime = getTreatmentTime();
		}
		//Keeping the treatmentTime the same if it an empty line
		if(treatmentLine.getSize() == 0){
			movingTime = getTreatmentTime();
		}
		//Don't forget... if the first person in line dies (the person being treated)
		//start the next person's treatment!
	}
	/**
	 * Express the time of Doctor.
	 * @return the string to display
	 */
	public String toString() {
		//Replace "??" with the number of minutes until the doctor is free
		//i.e. the treatment time remaining
		if(movingTime == getTreatmentTime() && treatmentLine.getSize()==0){
			return "Scenario 1: Doctor Free in " + "0" + " minute(s).";
		}
		return "Scenario 1: Doctor Free in " + movingTime + " minute(s).";
	}
}
