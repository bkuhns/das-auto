/*
 * DAS_GUIView.java
 */

package das_gui;

import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.*;
import org.jfree.data.io.*;
import org.jfree.layout.*;

import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JDesktopPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.InternalFrameEvent;

/**
 * The application's main frame.
 */
public class DAS_GUIView extends FrameView {

    public DAS_GUIView(SingleFrameApplication app) {
        super(app);

        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = DAS_GUIApp.getApplication().getMainFrame();
            aboutBox = new DAS_GUIAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        DAS_GUIApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        jDesktopPane2 = new javax.swing.JDesktopPane();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jFileChooser1 = new javax.swing.JFileChooser();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        mainPanel.setName("mainPanel"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(das_gui.DAS_GUIApp.class).getContext().getResourceMap(DAS_GUIView.class);
        jDesktopPane1.setBackground(resourceMap.getColor("jDesktopPane1.background")); // NOI18N
        jDesktopPane1.setName("jDesktopPane1"); // NOI18N

        jInternalFrame1.setName("jInternalFrame1"); // NOI18N
        jInternalFrame1.setVisible(true);

        jDesktopPane2.setBackground(resourceMap.getColor("jDesktopPane2.background")); // NOI18N
        jDesktopPane2.setName("jDesktopPane2"); // NOI18N

        jCheckBox1.setBackground(resourceMap.getColor("jCheckBox1.background")); // NOI18N
        jCheckBox1.setText(resourceMap.getString("jCheckBox1.text")); // NOI18N
        jCheckBox1.setName("jCheckBox1"); // NOI18N
        jCheckBox1.setBounds(0, 20, 180, 30);
        jDesktopPane2.add(jCheckBox1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jCheckBox2.setBackground(resourceMap.getColor("jCheckBox2.background")); // NOI18N
        jCheckBox2.setText(resourceMap.getString("jCheckBox2.text")); // NOI18N
        jCheckBox2.setName("jCheckBox2"); // NOI18N
        jCheckBox2.setBounds(180, 20, 200, 30);
        jDesktopPane2.add(jCheckBox2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jCheckBox3.setBackground(resourceMap.getColor("jCheckBox3.background")); // NOI18N
        jCheckBox3.setText(resourceMap.getString("jCheckBox3.text")); // NOI18N
        jCheckBox3.setName("jCheckBox3"); // NOI18N
        jCheckBox3.setBounds(0, 50, 180, 30);
        jDesktopPane2.add(jCheckBox3, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jCheckBox4.setBackground(resourceMap.getColor("jCheckBox4.background")); // NOI18N
        jCheckBox4.setText(resourceMap.getString("jCheckBox4.text")); // NOI18N
        jCheckBox4.setName("jCheckBox4"); // NOI18N
        jCheckBox4.setBounds(180, 50, 200, 30);
        jDesktopPane2.add(jCheckBox4, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jScrollPane1.setBorder(null);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTextArea1.setBackground(resourceMap.getColor("jTextArea1.background")); // NOI18N
        jTextArea1.setColumns(20);
        jTextArea1.setFont(resourceMap.getFont("jTextArea1.font")); // NOI18N
        jTextArea1.setRows(5);
        jTextArea1.setText(resourceMap.getString("jTextArea1.text")); // NOI18N
        jTextArea1.setBorder(null);
        jTextArea1.setName("jTextArea1"); // NOI18N
        jScrollPane1.setViewportView(jTextArea1);

        jScrollPane1.setBounds(0, 0, 380, 20);
        jDesktopPane2.add(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.setBounds(200, 100, -1, -1);
        jDesktopPane2.add(jButton1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N
        jButton2.setBounds(280, 100, -1, -1);
        jDesktopPane2.add(jButton2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
        );

        jInternalFrame1.setBounds(60, 90, 390, 160);
        jDesktopPane1.add(jInternalFrame1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jFileChooser1.setName("jFileChooser1"); // NOI18N
        jFileChooser1.setBounds(240, 140, 582, 397);
        jDesktopPane1.add(jFileChooser1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 951, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(das_gui.DAS_GUIApp.class).getContext().getActionMap(DAS_GUIView.class, this);
        jMenuItem1.setAction(actionMap.get("jMenuItem1ActionPerformed")); // NOI18N
        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        fileMenu.add(jMenuItem1);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        jMenu3.setText(resourceMap.getString("jMenu3.text")); // NOI18N
        jMenu3.setName("jMenu3"); // NOI18N
        menuBar.add(jMenu3);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 951, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 781, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        jMenuBar1.setName("jMenuBar1"); // NOI18N

        jMenu1.setText(resourceMap.getString("jMenu1.text")); // NOI18N
        jMenu1.setName("jMenu1"); // NOI18N
        jMenuBar1.add(jMenu1);

        jMenu2.setText(resourceMap.getString("jMenu2.text")); // NOI18N
        jMenu2.setName("jMenu2"); // NOI18N
        jMenuBar1.add(jMenu2);

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    //Custom Programmer's Code: DAS-AUTO Team
public JInternalFrame createJInternalFrame(int x, int y, int w, int h, String frameName)
{
     //Creates New JInternalFrame for data

        JInternalFrame newInternalFrame = new JInternalFrame();

        newInternalFrame.setClosable(true);
        newInternalFrame.setIconifiable(true);
        newInternalFrame.setMaximizable(true);
        newInternalFrame.setResizable(true);
        newInternalFrame.setName(frameName); // NOI18N
        newInternalFrame.setTitle(frameName);
        newInternalFrame.setVisible(true);
        //++internalFrameNumber;

        InternalFrameListener internalFrameListener = new InternalFrameIconifyListener();
        newInternalFrame.addInternalFrameListener(internalFrameListener);

        newInternalFrame.setBounds(x,y,w,h);

        return newInternalFrame;
}

public File raceDataFileSelection()
{
    JFileChooser dataFileChooser = new JFileChooser();
    dataFileChooser.setName("jFileChooser1"); // NOI18N
    dataFileChooser.setBounds(0, 0, 582, 397);
    File dataFile = null;

   //Attempt to Close the frame if the Usner Closes the File Browser
   int fileBrowserResult = dataFileChooser.showOpenDialog(null);
   if(fileBrowserResult == dataFileChooser.APPROVE_OPTION){
        dataFile = dataFileChooser.getSelectedFile();
   }
   else if(fileBrowserResult == dataFileChooser.CANCEL_OPTION){
       System.out.println("Dialog was Canceled.");
   }
   return dataFile;
}

public boolean[] raceDataTypeSelection()
{
    boolean xyDataSelect = false, gpsDataSelect = false, tractDataSelect = false, racyDataSelect = false;
    boolean[] raceDataTypesSelected =  new boolean[boolSelectArraySize];

    JInternalFrame typeSelectionjIntFrame = new JInternalFrame();
    typeSelectionjIntFrame.setVisible(true);
    typeSelectionjIntFrame.setBounds(60, 90, 390, 160);

    JDesktopPane typeSelectionjDesktopPane = new JDesktopPane();
    JCheckBox xyCheckBox = new JCheckBox();
    JCheckBox gpsCheckBox = new JCheckBox();
    JCheckBox tractCheckBox = new JCheckBox();
    JCheckBox racyCheckBox = new JCheckBox();   //...shocking!
    JScrollPane selectScrollPane = new JScrollPane();
    JTextArea selectTextArea = new JTextArea();
    JButton selectionOKButton = new JButton();
    JButton selectionNoBuenoButton = new JButton();


    


    //After everything has been added to the jInternalFrame, place it inside the permanent jDesktopPanel
    jDesktopPane1.add(typeSelectionjIntFrame, javax.swing.JLayeredPane.DEFAULT_LAYER);
    
    raceDataTypesSelected[0] = xyDataSelect;
    raceDataTypesSelected[1] = gpsDataSelect;
    raceDataTypesSelected[2] = tractDataSelect;
    raceDataTypesSelected[3] = racyDataSelect;

    return raceDataTypesSelected;
}

public void raceDataFramesAddToDesktopPane()
{
    
}

@Action
@SuppressWarnings({"static-access", "static-access"})
//method for adding new data windows to users to view. TODO: break into bite-sized chunks, instead of 1 huge mess.
public void jMenuItem1ActionPerformed() {
     XYSeries xAxisData = new XYSeries("X direction"), yAxisData = new XYSeries("Y direction"), timeAxisData = new XYSeries("Time");
     File raceDataFile;

    //link to new method for selection the race data file
    raceDataFile = raceDataFileSelection();
    System.out.println("File was Choosen: " + raceDataFile.getName());
    raceDataTypeSelection();
    /*
    JFileChooser dataFileChooser = new JFileChooser();
    dataFileChooser.setName("jFileChooser1"); // NOI18N
    dataFileChooser.setBounds(0, 0, 582, 397);
    String fileName = "";

   //Attempt to Close the frame if the Usner Closes the File Browser
   int fileBrowserResult = dataFileChooser.showOpenDialog(null);
   if(fileBrowserResult == dataFileChooser.APPROVE_OPTION)
   {
       

       //Old method of parsing CSV files
       try{
            File dataFile = dataFileChooser.getSelectedFile();
            System.out.println("File was Choosen: " + dataFile.getName());
            fileName = dataFile.getName();
            Scanner csvScanner = new Scanner(dataFile);
            //csvScanner.nextLine();
            Double timeInput = 0.0, lateralInput = 0.0, accelInput = 0.0;

            while(csvScanner.hasNextLine())
            {
                    StringTokenizer csvLine = new StringTokenizer(csvScanner.nextLine());
                    timeInput = Double.valueOf(csvLine.nextToken(",").toString());
                    lateralInput = Double.valueOf(csvLine.nextToken(",").toString());
                    accelInput = Double.valueOf(csvLine.nextToken().toString());
                    xAxisData.add(timeInput, lateralInput);
                    yAxisData.add(timeInput, accelInput);
            }
            csvScanner.close();
        }
        catch(FileNotFoundException e){
                System.out.println(e.toString());
        }
       */


        //Old method of testing adding only some test data to a panel for sample output
        /*
        JInternalFrame graphNormAccelFrame = createJInternalFrame(200, 100, 700, 450, fileName);

        XYSeriesCollection accelSeries = new XYSeriesCollection();
        XYSeriesCollection lateralSeries = new XYSeriesCollection();
        accelSeries.addSeries(yAxisData);
        lateralSeries.addSeries(xAxisData);
        XYDataset accelData = (XYDataset)accelSeries;
        XYDataset lateralData = (XYDataset)lateralSeries;
        JFreeChart accelChart = ChartFactory.createXYLineChart("DAS Auto Chart: Accel/Time", "Time(ms)", "AccelerationG(unk-nown)",
                           accelData, PlotOrientation.VERTICAL, false, false, false);
        JFreeChart lateralChart = ChartFactory.createXYLineChart("DAS Auto Chart: Accel/Time", "Time(ms)", "LateralG(unk-nown)",
                           lateralData, PlotOrientation.VERTICAL, false, false, false);
        ChartPanel accelPanel = new ChartPanel(accelChart);
        ChartPanel lateralPanel = new ChartPanel(lateralChart);

        JPanel jPanel1 = new JPanel();
        JPanel jPanel2 = new JPanel();
        jPanel1.add(accelPanel);
        jPanel2.add(lateralPanel);

        graphNormAccelFrame.add(jPanel1);
        graphNormAccelFrame.getContentPane();

        jDesktopPane1.add(graphNormAccelFrame, javax.swing.JLayeredPane.DEFAULT_LAYER);
        graphNormAccelFrame.moveToFront();
        
   }
   else if(fileBrowserResult == dataFileChooser.CANCEL_OPTION)
   {
       System.out.println("Dialog was Canceled.");
   }
   */
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JDesktopPane jDesktopPane2;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0, internalFrameNumber = 1;
    private final int boolSelectArraySize = 4;
    private JDialog aboutBox;
}

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