package agentsystem.simulation.controlledSchwarm;

import java.awt.Color;
import java.awt.Graphics;

import agentsystem.common.simulation.AgentPoint;
import agentsystem.common.simulation.IAgentContext;
import agentsystem.common.simulation.IEnvironment;
import agentsystem.simulation.schwarm.SwarmParticipant;

public class LeaderAgent extends SwarmParticipant {

	private AgentPoint mP = new AgentPoint(10, 10);
	
	public LeaderAgent(AgentPoint position, IEnvironment env, IAgentContext ctx) {
		super(position, env, ctx);
		this.collisionFactor = 0.2;
		this.alignFactor = 0.1;
		this.radius = 45;
		this.privateSpace = 10;
	}
	
	@Override
	public Void call() throws Exception {
		collision(env.getNeighbours(this));
		align(env.getNeighbours(this));
		this.direction = this.position.sub(mP).normalize();
		move();				
		return null;
	}

	
	public void setMotivationPoint(int x, int y) {
		mP.x = x;
		mP.y = y;		
	}
	
	@Override
	public void paintAgent(Graphics g) {
		g.setColor(Color.yellow);
		double radius = (privateSpace -1) /2;
		
		g.fillArc((int)(position.x - radius), (int)(position.y - radius), (int)(2*radius), (int)(2*radius), 0, 360);
		g.setColor(Color.black);
		g.drawArc((int)(position.x - radius), (int)(position.y - radius), (int)(2*radius), (int)(2*radius), 0, 360);
	}
	
}
