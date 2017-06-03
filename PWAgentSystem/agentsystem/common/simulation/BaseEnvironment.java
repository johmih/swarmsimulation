package agentsystem.common.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class BaseEnvironment implements IEnvironment {
	protected List<IAgent> agents = new ArrayList<IAgent>();
	protected List<IAgent> agentsBuffer = new ArrayList<IAgent>();
	protected List<IAgent> deadAgents = new ArrayList<IAgent>();
	protected ExecutorService execService;
	
	private boolean isRunning = true;
	private int width, height; 
	protected AgentPoint userClickedPoint = new AgentPoint(0,0);
	
	protected void nextTimeStep() {
		
		// remove dead agents
		try {
			List<Future<Void>> futureAgents = new ArrayList<Future<Void>>();
			for (IAgent a : agents) {
				if (!a.isAlive()) {
					deadAgents.add(a);
					continue;
				}
				futureAgents.add(execService.submit(a));
			}
			
			for (Future<Void> f : futureAgents) {
				f.get();
			}
			
			for (IAgent a : deadAgents) {
				agents.remove(a);
			}
			deadAgents.clear();
			agents.addAll(agentsBuffer);
									
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} 
	}
	
	protected BaseEnvironment(int width, int height) {
		execService = Executors.newSingleThreadExecutor();
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void run() {
		while (isRunning) {
			nextTimeStep();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public synchronized void addAgent(IAgent a) {
		try {
			agentsBuffer.add(a); //change these two lines for superman behaviour
			//agents.add(a);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
			
	@Override
	public final List<IAgent> getNeighbours(IAgent a) {
		List<IAgent> neighbours = new ArrayList<IAgent>();
		for (IAgent n : agents) {
			double dis = getDistance(a, n);
			if (dis <= a.getRadius()) {
				neighbours.add(n);
			}
		}
		
		return neighbours;
//		return agents.getNeighbours(a);
	}
	
	// remember that stuff can be seen through the border
	protected final double getDistance(IAgent a1, IAgent a2) {
		/* works but too f* slow
		ArrayList<AgentPoint> mirrors = new ArrayList<AgentPoint>(); 
		AgentPoint o = a2.getPosition();
		
		// use all mirrors
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				mirrors.add(o.add(new AgentPoint(i*getMax_X(), j*getMax_Y())));	
			}			
		}

		double minDist = getMax_X()+getMax_Y();
		
		for (AgentPoint p : mirrors) {
			minDist = Math.min(minDist, p.squareDistance(a1.getPosition()));
		}
		
		return Math.pow(minDist, 0.5);
		*/
		return a1.getPosition().distance(a2.getPosition());
	}
	
	@Override
	public int getMin_X() {
		return 0;
	}

	@Override
	public int getMin_Y() {
		return 0;
	}

	@Override
	public int getMax_X() {
		return width;
	}

	@Override
	public int getMax_Y() {
		return height;
	}
	
	@Override
	public IAgent[] getAgents() {
		IAgent[] copyAgents = new IAgent[agents.size()];
		for (int i = 0; i < agents.size(); i++) copyAgents[i] = agents.get(i); 
		
		return copyAgents;
	}

	@Override
	public void setMousePosition(int x, int y) {
		userClickedPoint.x = x;
		userClickedPoint.x = y;
		System.out.println("Updated user point:" + x + ", " + y);
	}
}
