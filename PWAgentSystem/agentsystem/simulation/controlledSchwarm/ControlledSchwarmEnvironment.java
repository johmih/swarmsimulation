package agentsystem.simulation.controlledSchwarm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import agentsystem.common.simulation.AgentPoint;
import agentsystem.common.simulation.BaseEnvironment;
import agentsystem.common.simulation.IAgent;
import agentsystem.simulation.predator_prey.BaseContext;
import agentsystem.simulation.schwarm.SwarmParticipant;

public class ControlledSchwarmEnvironment extends BaseEnvironment {
	private List<LeaderAgent> leaders = new ArrayList<LeaderAgent>();
	
	protected ControlledSchwarmEnvironment(int width, int height) {
		super(width, height);
		
		for (int i=0;i<4; ++i) {
			leaders.add(new LeaderAgent(new AgentPoint(250 + i, 250 + i), this, new BaseContext()));
		}
		
		agents.addAll(leaders);
		for (int i=0;i<200; ++i) {
			Random r = new Random();
			IAgent a = (IAgent) new SwarmParticipant(
					new AgentPoint(r.nextInt(width), r.nextInt(height)), this, new BaseContext());
			agents.add(a);
		}	
	}
	
	@Override
	public void setMousePosition(int x, int y) {
		for (LeaderAgent a : leaders) {
			a.setMotivationPoint(x, y);
		}
	}

}
