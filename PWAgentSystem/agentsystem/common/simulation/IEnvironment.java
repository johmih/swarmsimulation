package agentsystem.common.simulation;

import java.util.List;

public interface IEnvironment extends Runnable {
	public void addAgent(IAgent a);
	public List<IAgent> getNeighbours(IAgent a);
	
    public int getMin_X();
    public int getMin_Y();
    public int getMax_X();
    public int getMax_Y();
    
    public IAgent[] getAgents();
    
    public void setMousePosition(int x, int y);
}
