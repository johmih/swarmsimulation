package agentsystem.simulation.att;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import agentsystem.common.simulation.AgentPoint;
import agentsystem.common.simulation.BaseAgent;
import agentsystem.common.simulation.IEnvironment;
import agentsystem.simulation.predator_prey.AttractorContext;
import agentsystem.simulation.predator_prey.BaseContext;

/*
 * Special object for agents - source of food
 */
public class Attractor extends BaseAgent {
	protected float reproductionPropability = 0.004f;
	protected float reproductionRange = 75;
	protected Attractor child = null;
	
	public Attractor(AgentPoint position, IEnvironment env) {
		super(position, env, new BaseContext());
	}
	
	public Attractor(AgentPoint position, IEnvironment env, BaseContext ctx) {
		super(position, env, ctx);
	}
	
	private AttractorContext getCtx() {
		return (AttractorContext)ctx;
	}
	
	@Override
	public void paintAgent(Graphics g) {
		if (getCtx().life <= 0) return;
		double radius = 2.;
		Color c = new Color(0.1f , .5f, .2f, getCtx().life/500f);
		g.setColor(c);
		g.fillArc((int)(position.x - radius), (int)(position.y - radius), (int)(2*radius), (int)(2*radius), 0, 360);		
		g.drawArc((int)(position.x - radius), (int)(position.y - radius), (int)(2*radius), (int)(2*radius), 0, 360);

	}

	@Override
	public Void call() throws Exception {		
		return null;
	}
	
	protected void age() {
		getCtx().life--;
	}
	
	/** vegetation has a certain propability to grow 
	 * NOT DOING THIS ATM
	 */
	protected void spread() {
		Random r = new Random();
		
		if (r.nextFloat() > reproductionPropability) return;
		// we do reproduce
		// but where?
		float distance = reproductionRange*r.nextFloat();
		float angle = (float) (r.nextFloat()*Math.PI*2); 
 		AgentPoint p = new AgentPoint((Math.cos(angle)*distance), (Math.sin(angle)*distance));
		p = p.add(position);
		
		this.child = new Attractor(p, env, new AttractorContext());		
		// birth takes energy
		getCtx().life -= 10;;
	}
	
	// delivers a child to the enviroment
	public Attractor retrieveChild() {
		Attractor returnChild = this.child;
		this.child = null;
		return returnChild;
	}

}
