//********************************************************
//*******  DO NOT EDIT ANYTHING BELOW THIS LINE    *******
//*******        EXCEPT TO ADD JAVADOCS            *******
//*******                                          *******
//*******   YOU MAY CHANGE THE toString() method   *******
//*******     OR IMPLEMENT Comparable<Person>      *******
//*******   (IF YOU WOULD FIND HELPFUL TO DO SO)   *******
//********************************************************
/**
 * This class is for person in line.
 * @author Vu Nguyen
 */
class Person {
	/**
	 * number of people.
	 */
	private static int numPeople = 0;
	/**
	 * test result.
	 */
	private double testResult;
	/**
	 * the left in a person.
	 */
	private int timeLeft;
	/**
	 * Is the person dead.
	 */
	private boolean isDead;
	/**
	 * id of person.
	 */
	private final int id;
	/**
	 * Constructor of a person for the TreatAll.
	 * @param testResult the test result of that person
	 * @param timeLeft the time that person have left
	 */
	public Person(double testResult, int timeLeft) {
		this.testResult = testResult;
		this.timeLeft = timeLeft;
		this.id = numPeople++;
	}
	/**
	 * Constructor of a person for the Test and Treat.
	 * @param testResult test result of that person
	 * @param timeLeft time that person have left
	 * @param id id of that person assinged
	 */
	private Person(double testResult, int timeLeft, int id) {
		this.testResult = testResult;
		this.timeLeft = timeLeft;
		this.id = id;
	}
	/**
	 * Reduce the time a person have left until they either die or got vaccinated.
	 */
	public void tick() {
		if(timeLeft != Integer.MAX_VALUE) timeLeft--;
	}
	/**
	 * Check the person to se if they are alive or dead.
	 * @return wheather they are dead or not
	 */
	public boolean isDead() {
		return timeLeft <= 0;
	}
	/**
	 * Getter if a person test result.
	 * @return the test resukt of a person
	 */
	public double getTestResult() {
		//Note: test result is created when person is created,
		//but shouldn't be accessed until the person has been
		//through the testing line in scenario 2
		return testResult;
	}
	/**
	 * Getter of a person ID.
	 * @return id 
	 */
	public int getId() {
		return id;
	}
	/**
	 * Creatubg a new Person.
	 * @return a new person so that Test and Treat and Treat all have the same person
	 */
	public Person clone() {
		return new Person(testResult, timeLeft, id);
	}
	/**
	 * Show full into of a person.
	 * The info a a person in string format.
	 * @return the string to display
	 */
	public String toString() {
		return "ID: " + id + ", " + ((timeLeft == Integer.MAX_VALUE) ? "Not Infected" : "Time Left: " + timeLeft + " minutes");
	}
}
