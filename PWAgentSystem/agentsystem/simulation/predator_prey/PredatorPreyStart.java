package agentsystem.simulation.predator_prey;

import agentsystem.SimulationFrame;

public class PredatorPreyStart extends SimulationFrame {
	private static final long serialVersionUID = 1995228066371967763L;

	public PredatorPreyStart() {
		super("Predator prey agentsimulation", new PredatorPreyEnvironment(), 800, 500);
		sp.startSimulation();
	}
	
	public static void main(String[] args) {
		new PredatorPreyStart();
	}

}
