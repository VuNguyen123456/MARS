//TO DO: Nothing required here.

//******************************************************
//*******  DO NOT EDIT ANYTHING BELOW THIS LINE  *******
//******************************************************

import java.util.Map;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.RenderingHints;
import java.awt.Component;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
//import javax.swing.JFileChooser;


/**
 *  The graphical user interface for the simulation.
 *  
 *  @author K. Raven Russell
 */
public final class SimGUI {
	
	/**
	 *  The simulation controlled by this GUI.
	 */
	private final Simulation sim;
	
	/**
	 *  The frame that holds everything.
	 */
	private final JFrame frame;
	
	/**
	 *  The actual image/graphic being displayed.
	 */
	private final BufferedImage image;
	
	/**
	 * Text area for scenario output.
	 */
	private final JLabel scenarioOutput;
	
	/**
	 *  Whether or not a simulation is currently playing with
	 *  the play button (i.e. automatically playing).
	 */
	private boolean playing = false;
	
	/**
	 *  The height of one row.
	 */
	private static final int ROW_HEIGHT = 60;
	
	/**
	 *  The height of the image.
	 */
	private static final int SIM_HEIGHT = 6*ROW_HEIGHT;
	
	/**
	 *  The width of the image.
	 */
	private static final int SIM_WIDTH = 10*ROW_HEIGHT;
	
	/**
	 * Creates a new user interface tied to a specific simulation.
	 * 
	 * @param sim the simulation to tie the interface to
	 * 
	 */
	public SimGUI(Simulation sim) {
		this.sim = sim;
		
		frame = new JFrame("MARS Simulation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));
		
		/**
		 * Image of memory.
		 */
		class ImagePanel extends JPanel {
			private BufferedImage image;
			public ImagePanel(BufferedImage image) { setImage(image); }
			
			/**
			 * Set the image to be displayed.
			 * @param image the image to display
			 */
			public void setImage(BufferedImage image) {
				this.image = image;
				this.setAlignmentY(Component.TOP_ALIGNMENT);
				this.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
			}

			/**
			 *  {@inheritDoc}
			 */
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(image, 0, 0, this);
			}

		}
		
		image = new BufferedImage(SIM_WIDTH, SIM_HEIGHT, BufferedImage.TYPE_INT_RGB);
		middlePanel.add(new ImagePanel(image));
		drawScenarioImage();
		
		scenarioOutput = new JLabel();
		scenarioOutput.setVerticalAlignment(JLabel.TOP);
		scenarioOutput.setAlignmentY(Component.TOP_ALIGNMENT);
		scenarioOutput.setPreferredSize(new Dimension(300, image.getHeight()));
		setOutputText();
		
		//JScrollPane sp = new JScrollPane(scenarioOutput);
		//sp.setAlignmentY(Component.TOP_ALIGNMENT);
		//sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		middlePanel.add(scenarioOutput);
		
		contentPane.add(middlePanel);
		
		
		//interactions
		
		JPanel actionsPanel = new JPanel();
		actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.X_AXIS));
		
		JButton stepButton = new JButton("Step");
		stepButton.setActionCommand("Step");
		stepButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				step();
			}
		});
		actionsPanel.add(stepButton);
		
		JButton playButton = new JButton("Play");
		playButton.setActionCommand("Play");
		playButton.addActionListener(new ActionListener() {
			private void toggle() {
				//toggle playing and not playing
				playing = !playing;
				actionsPanel.getComponent(0).setEnabled(!playing);
				((JButton)actionsPanel.getComponent(1)).setText((playing ? "Stop" : "Play"));
			}
			
			public void actionPerformed(ActionEvent event) {
				toggle();
				
				//if playing, kick off a timer
				if(playing) {
					new javax.swing.Timer(1000, new ActionListener() {
						public void actionPerformed(ActionEvent event) {
							//someone hit the stop button
							if(!playing) {
								((javax.swing.Timer)event.getSource()).stop();
								return;
							}
							else {
								step();
							}
						}
					}).start();
				}
			}
		});
		actionsPanel.add(playButton);
		
		contentPane.add(actionsPanel);


		//pack everything up
		
		frame.setContentPane(contentPane);
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 *  Resets the image.
	 */
	public void repaint() {
		frame.getContentPane().repaint();
	}
	
	/**
	 *  Calls the step button on the simulation and updates
	 *  the GUI to display the result.
	 *  
	 * 
	 */
	public void step() {
		//step the simulation itself
		this.sim.step();
		
		//update output
		setOutputText();
		
		//draw it out!
		drawScenarioImage();
	}
	
	/**
	 *  Draws a visualization of both scenarios on the image.
	 */
	public void drawScenarioImage() {
		//setup graphics for drawing
		Graphics2D g = image.createGraphics();
		
		Stroke oldStroke = g.getStroke();
		g.setStroke(new BasicStroke(2));
		g.setColor(Color.BLACK);
		
		g.setFont(new Font("SansSerif", Font.BOLD, 12));
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		
		//make the image white to start with
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, image.getWidth(), image.getHeight());
		
		//figure out where each line will go
		int rowLine1 = ROW_HEIGHT;
		int rowLine2a = ROW_HEIGHT*3;
		int rowLine2b = ROW_HEIGHT*4+ (ROW_HEIGHT/4); // adds spacing between two lines
		
		//draw the lines (gray with black box around)
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, rowLine1, SIM_WIDTH, ROW_HEIGHT);
		g.fillRect(0, rowLine2a, SIM_WIDTH, ROW_HEIGHT);
		g.fillRect(0, rowLine2b, SIM_WIDTH, ROW_HEIGHT);
		
		g.setColor(Color.BLACK);
		g.drawRect(0, rowLine1, SIM_WIDTH, ROW_HEIGHT);
		g.drawRect(0, rowLine2a, SIM_WIDTH, ROW_HEIGHT);
		g.drawRect(0, rowLine2b, SIM_WIDTH, ROW_HEIGHT);
		
		//label them
		g.drawString(sim.getScenario1().toString(), 4, rowLine1 - 16);
		g.drawString(sim.getScenario2().toString(), 4, rowLine2a - 16);
		
		//get the lines from the simulation
		Line<Person> scenario1treatment = this.sim.getScenario1().getTreatmentLine();
		Line<Person> scenario2testing = this.sim.getScenario2().getTestingLine();
		Line<Person> scenario2treatment = this.sim.getScenario2().getTreatmentLine();
		
		//determine the longest line (drawing squeezes for long lines)
		int maxLineLength = 0;
		if(scenario1treatment != null && scenario1treatment.getSize() > maxLineLength) maxLineLength = scenario1treatment.getSize();
		if(scenario2testing != null && scenario2testing.getSize() > maxLineLength) maxLineLength = scenario2testing.getSize();
		if(scenario2treatment != null && scenario2treatment.getSize() > maxLineLength) maxLineLength = scenario2treatment.getSize();
		
		//stop if there is no one in any line
		if(maxLineLength == 0) {
			g.setStroke(oldStroke);
			return;
		}
		
		//how much space each "person" box takes up in line
		int colWidth = SIM_WIDTH / maxLineLength;
		if(colWidth > ROW_HEIGHT) colWidth = ROW_HEIGHT; //max = a square
		
		for(int currCol = 0; currCol < maxLineLength; currCol++) {
			//draw scenario 1 line
			if(scenario1treatment.getSize() > currCol) {
				g.setColor(Color.WHITE);
				g.fillRect(currCol * colWidth, rowLine1, colWidth, ROW_HEIGHT);
				g.setColor(Color.BLACK);
				g.drawRect(currCol * colWidth, rowLine1, colWidth, ROW_HEIGHT);
				g.drawString(""+scenario1treatment.get(currCol).getId(), currCol * colWidth + 4, rowLine1 + (ROW_HEIGHT/2) + 6);
			}
			
			//draw scenario 2 testing line
			if(scenario2testing.getSize() > currCol) {
				g.setColor(Color.WHITE);
				g.fillRect(currCol * colWidth, rowLine2a, colWidth, ROW_HEIGHT);
				g.setColor(Color.BLACK);
				g.drawRect(currCol * colWidth, rowLine2a, colWidth, ROW_HEIGHT);
				g.drawString(""+scenario2testing.get(currCol).getId(), currCol * colWidth + 4, rowLine2a + (ROW_HEIGHT/2) + 6);
			}
			
			//draw scenario 2 treatment line
			if(scenario2treatment.getSize() > currCol) {
				Person p = scenario2treatment.get(currCol);
				g.setColor(Color.WHITE);
				g.fillRect(currCol * colWidth, rowLine2b, colWidth, ROW_HEIGHT);
				g.setColor(new Color(1f, 0f, 0f, (float) ((p.getTestResult()-.4)/.6)));
				g.fillRect(currCol * colWidth, rowLine2b, colWidth, ROW_HEIGHT);
				g.setColor(Color.BLACK);
				g.drawRect(currCol * colWidth, rowLine2b, colWidth, ROW_HEIGHT);
				g.drawString(""+p.getId(), currCol * colWidth + 4, rowLine2b + (ROW_HEIGHT/2) + 6);
			}
		}
		
		g.setStroke(oldStroke);
		repaint();
	}
	
	/**
	 * Sets the variables to display all the program variables
	 * and their current settings.
	 */
	public void setOutputText() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<html>");
		sb.append("<br />");
		sb.append("<center>Simulation Outcome");
		sb.append("<br />");
		sb.append("--------------------------------------------------------------------------");
		sb.append("</center>");
		sb.append("<table border=\"0\">");
		sb.append("<tr><td>Scenario</td><td>Pending</td><td>Safe</td><td>Deaths</td></tr>");
		
		Scenario senario1 = sim.getScenario1();
		sb.append("<tr><td>1:</td><td>");
		sb.append(senario1.getPending());
		sb.append("</td><td>");
		sb.append(senario1.getSafe());
		sb.append("</td><td>");
		sb.append(senario1.getDeaths());
		
		Scenario senario2 = sim.getScenario2();
		sb.append("</td></tr><td>2:</td><td>");
		sb.append(senario2.getPending());
		sb.append("</td><td>");
		sb.append(senario2.getSafe());
		sb.append("</td><td>");
		sb.append(senario2.getDeaths());
		
		sb.append("</td></tr>");
		sb.append("</table>");
		sb.append("</html>");
		
		scenarioOutput.setText(sb.toString());
	}
	
	/**
	 * Main method that kicks off the simulator.
	 * @param args command line args for number of rows, columns, and an optional program file
	 */
	public static void main(String[] args) {
		String usageMsg = "Usage: java SimGUI [arrivalProb] [infectedProb] [survivalTime] [survivalStd] [testingTime] [treatmentTime] [seed]";
		
		//defaults
		double arrivalProb = 0.4;
		double infectedProb = 0.4;
		int survivalTime = 10;
		int survivalStd = 5;
		int testingTime = 2;
		int treatmentTime = 5;
		int seed = 0;
		
		try {
			if(args.length >= 1) arrivalProb = Double.parseDouble(args[0]);
			if(args.length >= 2) infectedProb = Double.parseDouble(args[1]);
			if(args.length >= 3) survivalTime = Integer.parseInt(args[2]);
			if(args.length >= 4) survivalStd = Integer.parseInt(args[3]);
			if(args.length >= 5) testingTime = Integer.parseInt(args[4]);
			if(args.length >= 6) treatmentTime = Integer.parseInt(args[5]);
			if(args.length >= 7) seed = Integer.parseInt(args[6]);
			
			if(args.length >= 8) {
				System.out.println(usageMsg);
				return;
			}
			
			new SimGUI(new Simulation(seed, arrivalProb, infectedProb, survivalTime, survivalStd, testingTime, treatmentTime));
		}
		catch(RuntimeException e) {
			e.printStackTrace();
			System.err.println(usageMsg);
		}
	}
}