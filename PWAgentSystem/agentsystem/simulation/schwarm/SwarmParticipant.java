package agentsystem.simulation.schwarm;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.Random;

import agentsystem.common.simulation.AgentPoint;
import agentsystem.common.simulation.BaseAgent;
import agentsystem.common.simulation.IAgent;
import agentsystem.common.simulation.IAgentContext;
import agentsystem.common.simulation.IEnvironment;

public class SwarmParticipant extends BaseAgent {
	protected double collisionFactor = 0.2;
	protected double alignFactor = 0.2;
	protected double middleFactor = 0.1;
	protected int countNeig = 0;
	
	public SwarmParticipant(AgentPoint position, IEnvironment env, IAgentContext ctx) {
		super(position, env, ctx);
		Random r = new Random();
		double speed = 1;
		double angle = r.nextDouble()*Math.PI*2;
 		direction = new AgentPoint((Math.cos(angle)*speed), (Math.sin(angle)*speed));
	}
	
	@Override
	public Void call() throws Exception {
		List<IAgent> neighbours = env.getNeighbours(this); 
		countNeig = neighbours.size();
		if (neighbours.size() != 0) { 			
			align(neighbours);
			middle(neighbours);
			collision(neighbours);
		}
		move();
		
		return null;
	}
	
	protected void align(List<IAgent> neighbours) {
		if (neighbours.size() == 0) return;
				
		AgentPoint average = new AgentPoint(0, 0);
		int agentCount = 0;
		for (IAgent a : neighbours) {
				average.translate(a.getDirection());
				agentCount++;
		}
		
		if (agentCount== 0) return;
		
		double x = average.getX() / agentCount * alignFactor + direction.x*(1-alignFactor);
		double y = average.getY() / agentCount * alignFactor + direction.y*(1-alignFactor);
		direction.setLocation(x, y);
	}
	
	protected void collision(List<IAgent> n) {
		if (n.size() <= 1) return;
		
		AgentPoint average = new AgentPoint(0, 0);
		int privatenumber = 0;
		
		for (IAgent a : n) {
			if (!a.equals(this)) { 
				double dis = a.getPosition().distance(position);
				if (dis < privateSpace) {
					privatenumber++;
					AgentPoint b = a.getPosition().sub(position);
					b.scale(50./b.length()/b.length());
					average.translate(b);
				}
			}
		}
		
		if (privatenumber == 0) return;
		
		double x = average.getX() / privatenumber * collisionFactor + direction.x*(1-collisionFactor);
		double y = average.getY() / privatenumber * collisionFactor + direction.y*(1-collisionFactor);
		direction.setLocation(x, y);
	}
	
	protected void middle(List<IAgent> n) {
		if (n.size() <= 1) return;
		AgentPoint average = new AgentPoint(0, 0);

		for (IAgent a : n) {
			if (a.equals(this) ) 
				continue;
			
			AgentPoint b = a.getPosition();
			average.translate(b);			
		}
		
		average.scale(1/(n.size()-1.));
		AgentPoint p = position.sub(average).normalize();
		p.scale(2);
		
		double x = p.getX() * middleFactor + direction.x*(1-middleFactor);
		double y = p.getY() * middleFactor + direction.y*(1-middleFactor);
		direction.setLocation(x, y);
	}
	
	protected void move() {
		position.translate(direction);
		
		if (position.x < env.getMin_X()) {
			position.x = env.getMax_X();//-1 * position.x;
		}
			
		if (position.y < env.getMin_Y()) {
			position.y = env.getMax_Y();//-1 * position.y;
		}
		
		if (position.x > env.getMax_X()) {
			position.x = env.getMin_X();//(2*env.getMax_X() - position.x);
		}
		if (position.y > env.getMax_Y()) {
			position.y = env.getMin_Y();//(2*env.getMax_Y() - position.y);
		}
	}

	@Override
	public void paintAgent(Graphics g) {
		float f = Math.min(countNeig/((float)radius), 1);
		g.setColor(new Color(f , 1-f, 1-f));
		double radius = (privateSpace -1) /2;
		
		g.fillArc((int)(position.x - radius), (int)(position.y - radius), (int)(2*radius), (int)(2*radius), 0, 360);
		g.setColor(Color.black);
		g.drawArc((int)(position.x - radius), (int)(position.y - radius), (int)(2*radius), (int)(2*radius), 0, 360);
	}

}
