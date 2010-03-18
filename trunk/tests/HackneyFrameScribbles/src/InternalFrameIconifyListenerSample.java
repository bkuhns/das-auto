import java.awt.BorderLayout;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

class InternalFrameIconifyListener extends InternalFrameAdapter {
  public void internalFrameIconified(InternalFrameEvent internalFrameEvent) {
    JInternalFrame source = (JInternalFrame) internalFrameEvent.getSource();
    System.out.println("Iconified: " + source.getTitle());
  }

  public void internalFrameDeiconified(InternalFrameEvent internalFrameEvent) {
    JInternalFrame source = (JInternalFrame) internalFrameEvent.getSource();
    System.out.println("Deiconified: " + source.getTitle());
  }
}
public class InternalFrameIconifyListenerSample {
	public static void main(final String[] args) {
	    JFrame frame = new JFrame();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    JDesktopPane desktop = new JDesktopPane();
	    JInternalFrame internalFrame = new JInternalFrame("Can Do All", true, true, true, true);

	    InternalFrameListener internalFrameListener = new InternalFrameIconifyListener();

//	     Add listener for iconification events
	    internalFrame.addInternalFrameListener(internalFrameListener);

	    desktop.add(internalFrame);

	    internalFrame.setBounds(25, 25, 200, 100);

	    JLabel label = new JLabel(internalFrame.getTitle(), JLabel.CENTER);
	    internalFrame.add(label, BorderLayout.CENTER);

	    internalFrame.setVisible(true);

	    frame.add(desktop, BorderLayout.CENTER);
	    frame.setSize(500, 300);
	    frame.setVisible(true);
	  }
}
