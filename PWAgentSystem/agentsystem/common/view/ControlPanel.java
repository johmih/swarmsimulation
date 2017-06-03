package agentsystem.common.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;

public class ControlPanel extends JDialog {
	private static final long serialVersionUID = 1413475298529732403L;
		
	public ControlPanel(List<JButton> btns) {
		setSize(new Dimension(150, 400));
		FlowLayout bly = new FlowLayout();
		bly.setVgap(10);
		this.setLayout(bly);
		for (JButton b : btns) {
			b.setSize(90, 50);
			bly.addLayoutComponent("", b);
			this.add(b);
		}
	}
	
	
}
