package agentsystem.simulation.predator_prey;

import java.awt.Color;

import agentsystem.common.simulation.IAgentContext;

public class BaseContext implements IAgentContext {
	public int life = 1;
	public Color stateColor;
	public double maxLifePoints;
	public double hungerRate;

}
