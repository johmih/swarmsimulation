package agentsystem.simulation.schwarm;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import agentsystem.common.simulation.AgentPoint;
import agentsystem.common.simulation.BaseEnvironment;
import agentsystem.common.simulation.IAgent;
import agentsystem.simulation.predator_prey.BaseContext;

public class SwarmEnvironment extends BaseEnvironment {
	
	public SwarmEnvironment() {
		super(800, 600);
		Random r = new Random();
				
		for (int i=0; i<600; ++i) {
			AgentPoint p = new AgentPoint(r.nextInt(this.getMax_X()), r.nextInt(this.getMax_Y()));
			IAgent a = (IAgent)new SwarmParticipant(p, this, new BaseContext());
			agents.add(a);
		}
		
//		for (int i=0; i<1000; ++i) {
//			int quarter = r.nextInt(4);
//			int x,y;
//			
//			int half_x = this.getMax_X() / 2;
//			int half_y = this.getMax_X() / 2;
//			
//			switch (quarter) {
//				case 0:
//					x = r.nextInt(half_x);
//					y = r.nextInt(half_y);			
//					break;
//				case 1:
//					x = r.nextInt(half_x) + half_x ;
//					y = r.nextInt(half_y);
//					break;
//				case 2:
//					x = r.nextInt(half_x);
//					y = r.nextInt(half_y) + half_y;
//					break;
//				default:
//					x = r.nextInt(half_x) + half_x;
//					y = r.nextInt(half_y) + half_y;
//					break;
//			}
//			
//			AgentPoint p = new AgentPoint(x, y);
//			IAgent a = (IAgent)new SwarmParticipant(p, this, new BaseContext());
//			agents.add(a);
//		}
	}
	
	@Override
	protected synchronized void nextTimeStep() {		
		try {
			// start agent calculations
			List<Future<Void>> futureAgents = new ArrayList<Future<Void>>();
			HashMap<IAgent, AgentPoint> prevPoints = new HashMap<IAgent, AgentPoint>(); 
			for (IAgent a : agents) {
				futureAgents.add(execService.submit(a));
				prevPoints.put(a, a.getPosition());
			}
			
			for (Future<Void> f : futureAgents) {
				f.get();
			}
									
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} 
	}
}
