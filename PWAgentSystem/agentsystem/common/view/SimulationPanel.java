/**
 * 
 */
package agentsystem.common.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import agentsystem.common.simulation.IAgent;
import agentsystem.common.simulation.IEnvironment;

/**
 * @author Johannes
 *
 */
public class SimulationPanel extends JPanel implements Runnable, MouseListener {
	private static final long serialVersionUID = -6723562612399857506L;
	
	private IEnvironment env;
	private Thread simThread;
	
	public SimulationPanel(IEnvironment env, int height, int width) {
		setPreferredSize(new Dimension(width, height));
		setDoubleBuffered(true);
		setName("SimulationPanel");
		this.env = env;
		this.simThread = new Thread(this);
		this.addMouseListener(this);
	}
		
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		IAgent[] as = env.getAgents();
		
		for (IAgent a : as) {
			a.paintAgent(g);
		}
	}
	
	public void startSimulation() {
		simThread.start();
	}
	
	public void stopSimulation() {
		simThread.interrupt();
	}

	@Override
	public void run() {
		while ( simThread == Thread.currentThread() ) {
	        try {
	              Thread.sleep(5);
	        } catch ( InterruptedException ie ) {
	             System.out.println( ie.getMessage() );
	        }
	        repaint();
	    }
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		env.setMousePosition(e.getX(), e.getY());
		System.out.println("Panel clicked:" + e.getX() + ", " + e.getY());
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// do nothing
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// do nothing
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// do nothing
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// do nothing
	}
}
