package agentsystem.common.simulation;

import java.awt.geom.Point2D;

public class AgentPoint extends Point2D.Double {
	private static final long serialVersionUID = 8587185790481460281L;

	public AgentPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public AgentPoint(AgentPoint p) {
		this.x = p.x;
		this.y= p.y;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;	
	}

	@Override
	public double distance(Point2D pt) {
		return this.sub((AgentPoint) pt).length();
	}
	
	public double squareDistance(Point2D pt) {
		return Math.pow(x-pt.getX(), 2) + Math.pow(y-pt.getY(), 2);
	}
	
	public double length () {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		
	}

	public AgentPoint sub(AgentPoint a) {
		return new AgentPoint(a.x - this.x, a.y - this.y);
	}	
	
	public AgentPoint add(AgentPoint a) {
		return new AgentPoint(a.x + this.x, a.y + this.y);
	}
	
	public AgentPoint normalize() {
		double l = length();
		this.x /= l;
		this.y /= l;
		return this;
	}
	
	
	public void scale(double s) {
		this.x *= s;
		this.y *= s;
	}
	
	public void translate(AgentPoint pt) {
		this.x += pt.x;
		this.y += pt.y;
	}
}
