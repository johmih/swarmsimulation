package agentsystem.simulation.controlledSchwarm;

import agentsystem.SimulationFrame;

public class ControlledSchwarmStart extends SimulationFrame {
	private static final long serialVersionUID = -4315267337280210622L;
	
	private static int width = 800, height = 600;

	public ControlledSchwarmStart() {
		super("Controlled Swarm", new ControlledSchwarmEnvironment(width, height), width, height);
		sp.startSimulation();
	}
	
	public static void main(String[] args) {
		new ControlledSchwarmStart();

	}

}
