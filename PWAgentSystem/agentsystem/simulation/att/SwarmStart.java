package agentsystem.simulation.att;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import agentsystem.SimulationFrame;
import agentsystem.common.view.ControlPanel;

public class SwarmStart extends SimulationFrame {
	private static final long serialVersionUID = 2680958672696307323L;
	
	private ControlPanel cp;
	private SwarmEnvironment newEnv;
	
	public SwarmStart() {
		super("Schwarmsimulation with attraction", new SwarmEnvironment(), 800, 600);
		sp.startSimulation();
		
		List<JButton> btns = new ArrayList<JButton>();
		
		newEnv = (SwarmEnvironment) env;
		
		JButton b0 = new JButton("Löschen");
		b0.addActionListener(newEnv);
		
		JButton b1 = new JButton("Raute");
		b1.setActionCommand("0");
		b1.addActionListener(newEnv);
		
		JButton b2 = new JButton("Circle");
		b2.setActionCommand("1");
		b2.addActionListener(newEnv);
		
		JButton b3 = new JButton("Line");
		b3.setActionCommand("2");
		b3.addActionListener(newEnv);
		
		JButton b4 = new JButton("Random");
		b4.setActionCommand("3");
		b4.addActionListener(newEnv);
		
		btns.add(b0);
		btns.add(b1);
		btns.add(b2);
		btns.add(b3);
		btns.add(b4);
		cp = new ControlPanel(btns);
		cp.setLocation(800, 0);
		cp.setVisible(true);
	}
	
	public static void main(String[] args) {
		new SwarmStart();
	}
}
