package agentsystem;

import java.awt.Color;

import javax.swing.JFrame;

import agentsystem.common.simulation.IEnvironment;
import agentsystem.common.view.SimulationPanel;

public class SimulationFrame extends JFrame {
	private static final long serialVersionUID = -8679898061461406689L;

	protected IEnvironment env;
	protected SimulationPanel sp;
	
	public SimulationFrame(String title, IEnvironment env, int width, int height) {
		super(title);
		this.env = env;
		sp = new SimulationPanel(this.env, width, height);
		setSize(width, height);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		add(sp);
		sp.setBackground(Color.white);
		setVisible(true);
		
		new Thread(env).start();
	}
}
