package agentsystem.simulation.predator_prey;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.Random;

import agentsystem.common.simulation.AgentPoint;
import agentsystem.common.simulation.IAgent;
import agentsystem.common.simulation.IEnvironment;
import agentsystem.simulation.att.Attractor;
import agentsystem.simulation.att.SwarmParticipant;


public class Predator extends SwarmParticipant {
//	public boolean hasEaten = false; // whether we have hunted something down
	public int digestionTime;
	public int timeHasHunger = 0;
	protected boolean hunting = false; // whether this predator is in his hunting phase
	protected float relaxedSpeed = 0.4f; // max speed while not hunting
	protected int minEnergy = 120; // energy needed to start hunting
	protected int energy = minEnergy; // decreases while hunting
	protected int energyRegeneration = 2;
	
	public Predator(AgentPoint position, IEnvironment env, PredatorContext ctx) {
		super(position, env, ctx);
		this.radius = 45;
		this.collisionFactor = 0.1;
		this.middleFactor = 0.08;
		digestionTime = getCtx().maxDigestionTime;
		double speed = 0.1;
		Random r = new Random();
		double angle = r.nextDouble()*Math.PI*2; 
 		direction = new AgentPoint((Math.cos(angle)*speed), (Math.sin(angle)*speed));
	}
	
	@Override
	public Void call() throws Exception {
		List<IAgent> neighbours = env.getNeighbours(this);
		//checkNearHunger(neighbours);
		
		collision(neighbours);
		middle(neighbours);
		hunt(neighbours);
		relax();
		move();

			/*
		if (!getCtx().hasEaten) {
			if (direction.x == 0 && direction.y == 0) {
				double speed = 0.2;
				double angle = PredatorPreyEnvironment.r.nextDouble()*Math.PI*2;
		 		direction = new AgentPoint((Math.cos(angle)*speed), (Math.sin(angle)*speed));
			}
			collision(neighbours);
			middle(neighbours);
			move();
		}
		else {
			direction = new AgentPoint(0,0);
		}*/
		return null;
	}
	
	/**
	 * after a hunt, slow down
	 */
	protected void relax() {
		if (hunting) return;
		energy += energyRegeneration; // we are regaining our strength
		if (this.direction.length() < relaxedSpeed ) return;
		//this.direction.normalize().scale(relaxedSpeed);
		this.direction.normalize().scale(0.95);
	}
		
	protected void middle(List<IAgent> n) {
		if (n.size() <= 1) return;
		AgentPoint average = new AgentPoint(0, 0);

		int countn = 0;
		
		for (IAgent a : n) {
			if (a.equals(this)) continue;
			if (a.getClass() == Predator.class || a.getClass() == Attractor.class) continue;
					 
			AgentPoint b = a.getPosition();
			average.translate(b);	
			countn++;
		}
		
		if (countn == 0) return;
		
		if (energy >= minEnergy) hunting = true;
		
		average.scale(1F/countn);
		AgentPoint p = position.sub(average).normalize();
		p.scale(3);
		
		double x = p.getX() * middleFactor + direction.x*(1-middleFactor);
		double y = p.getY() * middleFactor + direction.y*(1-middleFactor);
		direction.setLocation(x, y);
	}
	
	/**
	 * kill prey if we are close enough, also we get tired and may stop hunting
	 * @param n
	 */
	private void hunt(List<IAgent> n) {
		if (!hunting) return;
		
		for (IAgent a : n) {
			if (a.equals(this) || a.getClass() != Dopfer.class) continue;
			
			// successful hunt?
			if (a.getPosition().distance(position) < privateSpace) {
				//((BaseContext) a.getAgentContext()).life = 0;
				((Dopfer) a).die();
				this.hunting = false;
				this.energy = 0;

				return;
			} 
		}
		
		// we are getting tired of this
		energy--;
		if (energy > 0) return;
		// lets stop hunting
		this.hunting = false;
	}
	
	private void checkNearHunger(List<IAgent> n) {
		if (getCtx().hasEaten) {
			if (digestionTime < 0) {
				getCtx().hasEaten = false;
				getCtx().stateColor = Color.red;
				timeHasHunger = 0;
				getCtx().life = 5;
//				boolean proEnv = (PredatorPreyEnvironment.r.nextInt() < getCtx().reproductionRate);
//				if (proEnv) getCtx().isPregnant = true;
			} 			
			else {
				digestionTime--;
			}
		} else {
			if (getCtx().maxTimeWithoutFeed >= timeHasHunger) {
				timeHasHunger++;
			} else {
//				getCtx().life--;
				if (getCtx().life < 3) getCtx().stateColor = Color.pink;
				timeHasHunger = 0;
			}
		}
		
		for (IAgent a : n) {
			if (a.equals(this) || a.getClass() == Predator.class || a.getClass() == Attractor.class) continue;
			
			if (a.getPosition().distance(position) < privateSpace) {
				((BaseContext)a.getAgentContext()).life = 0;
				getCtx().hasEaten = true;
				digestionTime = getCtx().maxDigestionTime;
				getCtx().stateColor = Color.lightGray;
				return;
			} 
		}
	}
	
	private PredatorContext getCtx() {
		return (PredatorContext)ctx;
	}
	
	@Override
	public void paintAgent(Graphics g) {
		g.setColor(getCtx().stateColor);
				
		double radius = (privateSpace -1) /2;		
		g.fillArc((int)(position.x - radius), (int)(position.y - radius), (int)(2*radius), (int)(2*radius), 0, 360);
//		g.setColor(Color.black);
		g.drawArc((int)(position.x - radius), (int)(position.y - radius), (int)(2*radius), (int)(2*radius), 0, 360);
		
	}
}

