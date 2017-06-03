package agentsystem.simulation.att;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import agentsystem.common.simulation.AgentPoint;
import agentsystem.common.simulation.BaseEnvironment;
import agentsystem.common.simulation.IAgent;
import agentsystem.simulation.predator_prey.BaseContext;

public class SwarmEnvironment extends BaseEnvironment implements ActionListener{
	private List<Attractor> atts = new ArrayList<Attractor>();
		
	public SwarmEnvironment() {
		super(800, 600);
		Random r = new Random();
		
		for (int i=0; i<600; ++i) {
			AgentPoint ps = new AgentPoint(r.nextInt(this.getMax_X()), r.nextInt(this.getMax_Y()));
			IAgent a = (IAgent)new SwarmParticipant(ps, this, new BaseContext());
			agents.add(a);
		}
	}
	
	@Override
	protected synchronized void nextTimeStep() {		
		try {
			// start agent calculations
			List<Future<Void>> futureAgents = new ArrayList<Future<Void>>(); 
			for (IAgent a : agents) {
				futureAgents.add(execService.submit(a));
			}
			
			for (Future<Void> f : futureAgents) {
				f.get();
			}
												
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} 
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		agents.removeAll(atts);
		switch (e.getActionCommand()) {
			case "0":
				doRaute();
				break;
			case "1":
				doCircle();
				break;
			case "2":
				doLine();
				break;
			case "3":
				doRandom();
				break;
			default:
				clearAttPoints();
				break;
		}
	}
	
	@SuppressWarnings("unchecked")
	private void doRandom() {
		List<IAgent> bufferatts = new ArrayList<IAgent>();
		Random r = new Random();
		for (int i = 0; i < 10; i++) {
			AgentPoint p = new AgentPoint(r.nextInt(getMax_X()), r.nextInt(getMax_Y()));
			bufferatts.add((IAgent)new Attractor(p, this));
		}
		
		agents.addAll(bufferatts);
		atts.clear();
		atts.addAll((Collection<? extends Attractor>) bufferatts);
	}

	private void clearAttPoints() {
		agents.removeAll(atts);
		atts.clear();
	}
	
	@SuppressWarnings("unchecked")
	private void doRaute() {
		int padding = 100;
		AgentPoint p = new AgentPoint(this.getMax_X() / 2, padding);
		AgentPoint p1 = new AgentPoint(padding, this.getMax_Y() / 2);
		AgentPoint p2 = new AgentPoint(this.getMax_X() - padding, this.getMax_Y() / 2);
		AgentPoint p3 = new AgentPoint(this.getMax_X() / 2, this.getMax_Y() - padding);
		List<IAgent> bufferatts = new ArrayList<IAgent>();
		bufferatts.add((IAgent)new Attractor(p, this));
		bufferatts.add((IAgent)new Attractor(p1, this));
		bufferatts.add((IAgent)new Attractor(p2, this));
		bufferatts.add((IAgent)new Attractor(p3, this));
		agents.addAll(bufferatts);
		atts.clear();
		atts.addAll((Collection<? extends Attractor>) bufferatts);
	}
	
	@SuppressWarnings("unchecked")
	private void doCircle() {
		float myradius = 200;
		
		AgentPoint center = new AgentPoint(this.getMax_X() / 2, this.getMax_Y() / 2);

		int npoints = 20;
		List<IAgent> bufferatts = new ArrayList<IAgent>();
		
		float angle = 0;
		float angleinc = (float) (Math.PI*2./npoints);
		for (int i = 0; i < npoints; i++) {
			AgentPoint p = new AgentPoint(Math.cos(angle), Math.sin(angle));
			p.scale(myradius);
			p.translate(center);
			angle += angleinc;
			bufferatts.add((IAgent)new Attractor(p, this));
		}
		
		agents.addAll(bufferatts);
		atts.clear();
		atts.addAll((Collection<? extends Attractor>) bufferatts);
	}
	
	@SuppressWarnings("unchecked")
	private void doLine() {
		int half_x = this.getMax_X() / 2;
		int half_y = this.getMax_Y() / 2;
		int space = 10;
		int padding_x = this.getMax_X() / space;
		int padding_y = this.getMax_X() / space;

		List<IAgent> bufferatts = new ArrayList<IAgent>();
		for (int i=0; i< 10; ++i) {
			AgentPoint p = new AgentPoint(half_x, padding_x * i);
			bufferatts.add((IAgent)new Attractor(p, this));
		}
		
		for (int i=0; i< 10; ++i) {
			AgentPoint p = new AgentPoint(padding_y * i, half_y);
			bufferatts.add((IAgent)new Attractor(p, this));
		}
		agents.addAll(bufferatts);
		atts.clear();
		atts.addAll((Collection<? extends Attractor>) bufferatts);
	}
}
