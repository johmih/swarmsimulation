package agentsystem.simulation.predator_prey;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.Random;

import agentsystem.common.simulation.AgentPoint;
import agentsystem.common.simulation.IAgent;
import agentsystem.common.simulation.IAgentContext;
import agentsystem.common.simulation.IEnvironment;
import agentsystem.simulation.att.Attractor;
import agentsystem.simulation.att.SwarmParticipant;

public class Dopfer extends SwarmParticipant {
 	private int reproductionRate = 0;
	//private int eatenTimer = 0;
	//private boolean hasEaten = false;
	protected boolean inAfterLife = false; //whether we died or not
 	protected int timeInAfterLife = 0; // how many timesteps we will remain in afterlife
 	protected int maxTimeInAfterLife = 20; // how many timesteps we will remain in afterlife
	
	public Dopfer(AgentPoint position, IEnvironment env, IAgentContext ctx) {
		super(position, env, ctx);
	}
	
	@Override
	public Void call() throws Exception {
		if (inAfterLife) {
			timeInAfterLife--;
			if (timeInAfterLife < 0) getCtx().life = 0;
			return null;
		}
		
		List<IAgent> neighbours = env.getNeighbours(this);
		List<IAgent> reducedNeighbours = getReduceNeighbours(neighbours, neighbourRadius);
		countNeig = reducedNeighbours.size();
		
		if (neighbours.size() != 0) {
			escape(reducedNeighbours);
			heedWarning(reducedNeighbours);
			align(reducedNeighbours);
			middle(reducedNeighbours);
			collision(reducedNeighbours);
			gotoFood(neighbours);
			eat(reducedNeighbours);
//			sex();
		}
		
		chill();		
		colorize();
		move();
		return null;
	}
	
	/**
	 * choose our color
	 */
	protected void colorize() {
		if (getCtx().fear >= 1) return;
		float maxn = 3* (float) ((float) Math.pow(neighbourRadius, 2) / privateSpace / privateSpace ); 
		float f = Math.min(countNeig/maxn, 1);
		float h = 0.7f;
		getCtx().stateColor = new Color(0.55f , f*h + (1-h), h*(1-f)+(1-h));
	}
	
	// --------------- Behaviour
		
	/**
	 * makes them look for other sheep who are afraid and run themselves
	 * @param n
	 */
	protected void heedWarning(List<IAgent> n) {
		if (n.size() <= 1) return;
		if (getCtx().fear > 0) return; // new line but seems to work just as well
		
		AgentPoint average = new AgentPoint(0, 0);
		int countn = 0;
		
		for (IAgent a : n) {
			if (a.equals(this)) continue;
			if (a.getClass() == Predator.class || a.getClass() == Attractor.class) continue;
			if (((DopferContext) a.getAgentContext()).fear <= 0) continue;
			countn++;
			AgentPoint b = a.getPosition().sub(position);
			b.scale(5F/b.length());
			average.translate(b);
		}
		
		if (countn == 0) return;
		
		double x = average.getX() / countn * collisionFactor + direction.x*(1-collisionFactor);
		double y = average.getY() / countn * collisionFactor + direction.y*(1-collisionFactor);
		direction.setLocation(x, y);
		getCtx().stateColor = new Color(120,0,200);
	}	
	
	/**
	 * makes them slow down, less so when they are afraid
	 */
	private void chill() {
		double l = direction.length();
		if (getCtx().fear > 0) {
			if (l < 2) return;
			direction.normalize().scale(2);			
		} else {
			if (l < 1) return;
			direction.scale(0.8);			
		}
	}
		
	
	/**
	 * makes them look for enemies, run from enemies and get afraid by seeing enemies
	 * @param n
	 */
	private void escape(List<IAgent> n) {
		if (n.size() <= 1) {
			getCtx().fear = 0;
			return;
		}
		AgentPoint average = new AgentPoint(0, 0);

		int countn = 0;
		for (IAgent a : n) {
			
			if (a.equals(this) || !(a.getClass() == Predator.class ) ) 
				continue;
			
			AgentPoint b = a.getPosition();
			average.translate(b);			
			countn++;
		}
		
		// get new color if not afraid before
		if (countn > 0 && getCtx().fear == 0) {
//			Random r = new Random();
			getCtx().stateColor = new Color(120, 0, 200);			
		}
		
		getCtx().fear = countn;
		if (countn==0) return;
		
		average.scale(1F/countn);
		AgentPoint p = position.sub(average).normalize();
		p.scale(-8);
		
		double x = p.getX() * middleFactor + direction.x*(1-middleFactor);
		double y = p.getY() * middleFactor + direction.y*(1-middleFactor);
		direction.setLocation(x, y);
	}
	
	private void sex() {
		DopferContext c = getCtx();
		if (reproductionRate > getCtx().timeToReproduct && c.fear == 0 && countNeig > 1) {
			boolean proEnv = (PredatorPreyEnvironment.r.nextInt() < getCtx().reproductionRate);
			
			if (proEnv) {
				reproductionRate = 0;
				c.isPregnant = true;
			}
		} else if (getCtx().fear == 0){
			reproductionRate++;
		}
	}
	
	/**
	 * eat vegetation
	 * @param neighbours
	 */
	private void eat(List<IAgent> neighbours) {
		if (neighbours.size() == 0) return;
		
		for (IAgent a : neighbours) {
			if (a.getClass() == Attractor.class) {
				Attractor attractor = (Attractor) a;
				AttractorContext attCtx = (AttractorContext)attractor.getAgentContext();
				
				//if (hasEaten) {
					//eatenTimer++;
					//getCtx().stateColor = Color.orange;
				
					//if (eatenTimer > getCtx().hungerTimer) {
						//hasEaten = false;
					//}
				//}
				//else {
					//hasEaten = attCtx.beEaten();
				attCtx.beEaten();
				//}
			}
		}
	}
	
	public void die() {
		inAfterLife = true;
		timeInAfterLife = maxTimeInAfterLife;
	}
	
	// ---------------- Ctx & paint
	private DopferContext getCtx() {
		return (DopferContext)ctx;
	}
			
	@Override
	public void paintAgent(Graphics g) {
		if (inAfterLife) paintDeadAgent(g);
		else paintLiveAgent(g);
	}
	protected void paintLiveAgent(Graphics g) {
		g.setColor(getCtx().stateColor);
		double radius = (privateSpace -1) /2;
		
		g.fillArc((int)(position.x - radius), (int)(position.y - radius), (int)(2*radius), (int)(2*radius), 0, 360);
		g.drawArc((int)(position.x - radius), (int)(position.y - radius), (int)(2*radius), (int)(2*radius), 0, 360);		
	}
	protected void paintDeadAgent(Graphics g) {
		g.setColor(Color.BLACK);
		double radius = (privateSpace -1) /2;
		
		g.drawLine((int) (position.x - radius), (int) (position.y - radius), (int) (position.x + radius), (int) (position.y + radius));
		g.drawLine((int) (position.x + radius), (int) (position.y - radius), (int) (position.x - radius), (int) (position.y + radius));
	}

}
