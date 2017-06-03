package agentsystem.simulation.predator_prey;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import agentsystem.common.simulation.AgentPoint;
import agentsystem.common.simulation.BaseEnvironment;
import agentsystem.common.simulation.IAgent;
import agentsystem.simulation.att.Attractor;

public class PredatorPreyEnvironment extends BaseEnvironment {
	public static Random r = new Random();
	public final static int width = 800;
	public final static int height = 600;
	
	List<IAgent> buffer = new ArrayList<IAgent>();
	List<Attractor> atts = new ArrayList<Attractor>();
	
	private int maxAttCount = 25;
	//private AgentPoint newPredatorPosition = null;
	
	
	public PredatorPreyEnvironment() {
		super(width, height);
				
		// create predators
		for (int i=0; i<8; ++i) {
			IAgent a = (IAgent)new Predator(new AgentPoint(r.nextInt(width), r.nextInt(height)), this, new PredatorContext());
			agents.add(a);
		}
		
		// create Dopfers
		for (int i=0; i<600; ++i) {
			IAgent a = (IAgent)new Dopfer(new AgentPoint(r.nextInt(width), r.nextInt(height)), this, new DopferContext());
			agents.add(a);
		}

		// create attractors aka food
		int padding = 100;
		AgentPoint p = new AgentPoint(this.getMax_X() / 2, padding);
		AgentPoint p1 = new AgentPoint(padding, this.getMax_Y() / 2);
		AgentPoint p2 = new AgentPoint(this.getMax_X() - padding, this.getMax_Y() / 2);
		AgentPoint p3 = new AgentPoint(this.getMax_X() / 2, this.getMax_Y() - padding);
		List<IAgent> bufferatts = new ArrayList<IAgent>();
		bufferatts.add((IAgent)new Attractor(p, this, new AttractorContext()));
		bufferatts.add((IAgent)new Attractor(p1, this, new AttractorContext()));
		bufferatts.add((IAgent)new Attractor(p2, this, new AttractorContext()));
		bufferatts.add((IAgent)new Attractor(p3, this, new AttractorContext()));
		agents.addAll(bufferatts);
		atts.clear();
		atts.addAll((Collection<? extends Attractor>) bufferatts);
		
	}
	
	@Override
	protected synchronized void nextTimeStep() {
		super.nextTimeStep();
		
		int attCount = 0;
		try {
			if (agents.size() < 2000) {
				// reproduction / spawning new agents
				
				
				for (IAgent a : agents) {
					if (a.getClass() == Predator.class) {
						PredatorContext pctx = (PredatorContext)a.getAgentContext();
						if (pctx.isPregnant) {
							double x = a.getPosition().x + 2;
							double y = a.getPosition().y + 2;
							buffer.add(new Predator(new AgentPoint(x,y), this, new PredatorContext()));
						}
						pctx.isPregnant = false;
					}				
					else if (a.getClass() == DopferContext.class){
						DopferContext dctx = (DopferContext)a.getAgentContext();
						if (dctx.isPregnant) {
							double x = a.getPosition().x + 1;
							double y = a.getPosition().y + 1;
							buffer.add(new Dopfer(new AgentPoint(x, y), this, new DopferContext()));
							dctx.isPregnant = false;
						}
					}
					if (a.getClass() == Attractor.class) {
						attCount++;
						// do we have a new child?
						Attractor child = ((Attractor) a).retrieveChild();
						if (child != null) {
							buffer.add(child);
							attCount++;
						}
					}
				}
				//if (attCount < maxAttCount) {
				
					for (int i=0; i< maxAttCount - attCount; i++ ) {
						int x = r.nextInt(width);
						int y = r.nextInt(height);
						buffer.add(new Attractor(new AgentPoint(x,y), this, new AttractorContext()));
					}
				//}
				if (agents.size() + buffer.size() < 2000) agents.addAll(buffer);
				buffer.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void setMousePosition(int x, int y) {
		userClickedPoint.x = x;
		userClickedPoint.x = y;
		//newPredatorPosition = new AgentPoint(x, y);
		addAgent(new Predator(new AgentPoint(x, y), this, new PredatorContext()));
		System.out.println("Updated user point:" + x + ", " + y);
	}

}
