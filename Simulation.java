//TO DO: Nothing required here.

//******************************************************
//*******  DO NOT EDIT ANYTHING BELOW THIS LINE  *******
//******* EXCEPT TO ADD CONSOLE OUTPUT IF NEEDED *******
//******************************************************

import java.util.Random;

/**
 *  The actual simulation.
 *  
 *  @author K. Raven Russell
 */
public final class Simulation {
	/**
	 *  The random number generator.
	 */
	private Random rng;
	
	/**
	 *  The arrival probability.
	 */
	private double arrivalProb;
	
	/**
	 *  The infected probability.
	 */
	private double infectedProb;
	
	/**
	 *  The average survival time.
	 */
	private int survivalTime;
	
	/**
	 *  The survival time standard deviation.
	 */
	private int survivalStd;
	
	/**
	 *  The current treatment scenario.
	 */
	private TreatAll scenario1;
	
	/**
	 *  The test-and-treat scenario.
	 */
	private TestAndTreat scenario2;
	
	/**
	 * Creates a simulation of the proper size and optionally runs some number
	 * of commands to run.
	 * 
	 * @param seed the random number generator seed
	 * @param arrivalProb the arrival probability
	 * @param infectedProb the infected probability
	 * @param survivalTime the average survival time
	 * @param survivalStd the survival time standard deviation
	 * @param testingTime the time to administer a test
	 * @param treatmentTime the time to administer a treatment regimen
	 */
	public Simulation(int seed, double arrivalProb, double infectedProb, int survivalTime, int survivalStd, int testingTime, int treatmentTime) {
		this.rng = new Random(seed);
		this.arrivalProb = arrivalProb;
		this.infectedProb = infectedProb;
		this.survivalTime = survivalTime;
		this.survivalStd = survivalStd;
		
		this.scenario1 = new TreatAll(treatmentTime);
		this.scenario2 = new TestAndTreat(testingTime, treatmentTime);
	}
	
	/**
	 *  Moves the simulation forward one step (1 step = 1 minute).
	 */
	public void step() {
		System.out.println("Step");
		
		getScenario1().tick();
		getScenario2().tick();
		
		double arrival = rng.nextDouble();
		if(arrival < arrivalProb) {
			boolean infected = (rng.nextDouble() < infectedProb);
			double test = rng.nextDouble();
			double testResult =  (infected) ? ((0.6*test)+.4) : (0.8*test);
			int timeLeft = (infected) ? (int)((rng.nextGaussian()*survivalStd)+survivalTime) : Integer.MAX_VALUE;
			
			Person p = new Person(testResult, timeLeft);
			System.out.println("Person arrived: " + p);
			
			getScenario1().addPerson(p);
			getScenario2().addPerson(p.clone());
		}
	}
	
	/**
	 *  Allows the GUI access to scenario 1.
	 *  
	 *  @return scenario 1
	 */
	public TreatAll getScenario1() {
		return scenario1;
	}
	
	/**
	 *  Allows the GUI access to scenario 2.
	 *  
	 *  @return scenario 2
	 */
	public TestAndTreat getScenario2() {
		return scenario2;
	}
}