package agentsystem.simulation.predator_prey;

import java.awt.Color;


public class PredatorContext extends BaseContext{
	public int maxDigestionTime = 100;
	public int maxTimeWithoutFeed = 140;
	public double reproductionRate = 0.12;
	
	public boolean hasEaten = false;
	public boolean isPregnant = false;
	
	public PredatorContext() {
		stateColor = Color.red;
		life = 5;
	}
}
