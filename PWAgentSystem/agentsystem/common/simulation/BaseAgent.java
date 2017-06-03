package agentsystem.common.simulation;

import agentsystem.simulation.predator_prey.BaseContext;


public abstract class BaseAgent implements IAgent {
	public AgentPoint position;
	protected AgentPoint direction = new AgentPoint(0,0);
	protected double radius = 100;
	protected double privateSpace = 7;
	protected IEnvironment env;
	
	protected IAgentContext ctx = new BaseContext();
	
	protected BaseAgent(AgentPoint position, IEnvironment env) {
		this.position = position;
		this.env = env;
	}
	
	protected BaseAgent(AgentPoint position, IEnvironment env, IAgentContext ctx) {
		this(position, env);
		this.ctx = ctx;
	}
	
	@Override
	public IAgentContext getAgentContext() {
		return ctx;
	}
	
	@Override
	public boolean isAlive() {
		return ((BaseContext)ctx).life!=0;
	}
	
	@Override
	public AgentPoint getPosition() {
		return position;
	}
	
	@Override
	public double getRadius() {
		return radius;
	}
	
	@Override
	public AgentPoint getDirection() {
		return direction;
	}
}
