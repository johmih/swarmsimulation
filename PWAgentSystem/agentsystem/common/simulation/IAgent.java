package agentsystem.common.simulation;

import java.awt.Graphics;
import java.util.concurrent.Callable;

public interface IAgent extends Callable<Void>{
	// normal methods
	public void paintAgent(Graphics g);
	
	public IAgentContext getAgentContext();
	
	// alle Informationen zum organisieren
	public AgentPoint getPosition();
	public AgentPoint getDirection();
	
	// interne Agenteninformationen	
	public double getRadius();
	public boolean isAlive();
}
