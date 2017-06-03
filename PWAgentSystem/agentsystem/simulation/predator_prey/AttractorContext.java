/**
 * 
 */
package agentsystem.simulation.predator_prey;

/**
 * @author Johannes
 *
 */
public class AttractorContext extends BaseContext {

	public AttractorContext(int mylife) {
		this.life = mylife;
	}
	public AttractorContext() {
		this.life = 500;
	}

	
	public synchronized boolean beEaten() {
		if (life>0) {
			this.life--;
			return true;
		} 
		return false;		
	}
}
