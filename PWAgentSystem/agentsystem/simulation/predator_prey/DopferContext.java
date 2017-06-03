package agentsystem.simulation.predator_prey;

import java.awt.Color;


public class DopferContext extends BaseContext {
	public int timeToReproduct = 1000;
	public double reproductionRate = 0.2;
	
	public int fear = 0;
	public boolean isPregnant = false;
	public int hungerTimer = 100;
	
	public DopferContext() {
		stateColor = Color.cyan;
		life = 1;
	}
}
