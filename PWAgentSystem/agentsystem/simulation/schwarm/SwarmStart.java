/*
 * 
 */

package agentsystem.simulation.schwarm;

import agentsystem.SimulationFrame;

public class SwarmStart extends SimulationFrame {
	private static final long serialVersionUID = 2680958672696307323L;
	
	public SwarmStart() {
		super("Schwarmsimulation", new SwarmEnvironment(), 800, 600);
		sp.startSimulation();
	}
	
	public static void main(String[] args) {
		new SwarmStart();
	}
}
