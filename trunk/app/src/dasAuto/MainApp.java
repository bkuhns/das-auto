package dasAuto;


public class MainApp {
	
	
	public MainApp() {
		initializeGui();
	}
	
	
	private void initializeGui() {
		MainFrame mainFrame = new MainFrame();
		mainFrame.setVisible(true);
	}
	

	public static void main(String[] args) {
		new MainApp(); // Start the app.
	}
	

}
