package dasAuto;

import javax.swing.UIManager;

import dasAuto.logData.samples.AccelSample;


public class MainApp {
	
	
	public MainApp() {
		initializeGui();
	}
	
	
	private void initializeGui() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception ex) {
			System.err.println(ex.getMessage());
		}
		
		MainFrame mainFrame = new MainFrame();
		mainFrame.setVisible(true);
	}
	

	public static void main(String[] args) {
		new MainApp(); // Start the app.
	}
	

}
