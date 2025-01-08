/**
 *  The abstract parent class of all scenarios
 *  for the simulator.
 */
abstract class Scenario {
	/**
	 *  The number of people who have been determined
	 *  safe from the microbe in this scenario.
	 */
	protected int numSafe = 0;
	
	/**
	 *  The number of people who have been died
	 *  due to the microbe in this scenario.
	 */
	protected int numDeaths = 0;
	
	/**
	 *  Provides the number of safe people for the
	 *  simulator.
	 *  
	 *  @return the number of safe people
	 */
	public int getSafe() {
		return numSafe;
	}
	
	/**
	 *  Provides the number of deaths for the
	 *  simulator.
	 *  
	 *  @return the number of deaths
	 */
	public int getDeaths() {
		return numDeaths;
	}
	
	/**
	 *  Provides the number of people still being
	 *  processed in this scenario.
	 *  
	 *  @return the number of pending people
	 */
	abstract public int getPending();
	
	/**
	 *  Adds a person when they arrive from Mars.
	 * @param p the person to add
	 */
	abstract public void addPerson(Person p);
	
	/**
	 *  Moves time forward (1 minute).
	 */
	abstract public void tick();
}
