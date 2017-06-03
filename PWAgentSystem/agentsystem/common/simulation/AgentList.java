package agentsystem.common.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class AgentList extends Thread {
	private Vector<IAgent> agents;
	
	public AgentList() {
		agents = new Vector<IAgent>();
		this.start();
	}
	
	public AgentList(List<IAgent> agents) {
		this();
		agents.addAll(agents);
	}
	
	public synchronized void add(IAgent a) {
		agents.add(a);
	}
	
	public synchronized void remove(IAgent a) {
		agents.remove(a);
	}
	
	public synchronized List<IAgent> getNeighbours(IAgent a) {
		List<IAgent> neighbours = new ArrayList<IAgent>();
		for (IAgent n : agents) {
			double dis = getDistance(a, n);
			if (dis <= a.getRadius()) {
				neighbours.add(n);
			}
		}
		return neighbours;
	}
	
	protected final double getDistance(IAgent a1, IAgent a2) {
		return a1.getPosition().distance(a2.getPosition());
	}
	
	public synchronized IAgent[] getAgents() {
		IAgent[] copyAgents = new IAgent[agents.size()];
		for (int i = 0; i < agents.size(); i++) copyAgents[i] = agents.get(i); 
		
		return copyAgents;
	}
}
