package GUI;

import java.awt.Choice;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;

import java.awt.GridBagConstraints;
import javax.swing.BoxLayout;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import tenChannelflyWalkController.ChannelsStatusAndTimeStamp;
import tenChannelflyWalkController.ControllerThread;
import tenChannelflyWalkController.EditableTableModel;
import tenChannelflyWalkController.SerialComm;
import tenChannelflyWalkController.Setting;

import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTable;

public class VideoRecorderGUI {

	private JFrame frame;
	private JFrame frame_1;
	private JTextField txtCycles;
	private JTextField txtCommands;
	ControllerThread c;
	private Setting setting;


	private JProgressBar pBar;;
	private JTable settingTable;
	private int totalCycles;
	private int numberOfCommands;
	private int lengthPerTrial;
	private int iti;
	private JTextField txtLengthPerTrial;
	private JTextField txtIti;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VideoRecorderGUI window = new VideoRecorderGUI();
					window.frame_1.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public VideoRecorderGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		numberOfCommands = 5;
		c = new ControllerThread();

		final Runnable progressUpdater = new Runnable(){
			public void run(){
				SwingUtilities.invokeLater(new Runnable(){
					public void run(){
						updateProgressBar();
					}
				});	
			}
		};//ask STEPHEN if this is too crude
		c.addUpdaterAction(progressUpdater);

		frame = new JFrame();

		frame.setBounds(100, 100, 304, 556);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		frame_1 = new JFrame();
		frame_1.setBounds(100, 100, 450, 300);
		frame_1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frame_1.getContentPane().setLayout(gridBagLayout);

		JButton btnConnectToArduino = new JButton("Connect to Arduino");
		GridBagConstraints gbc_btnConnectToArduino = new GridBagConstraints();
		gbc_btnConnectToArduino.anchor = GridBagConstraints.EAST;
		gbc_btnConnectToArduino.gridwidth = 2;
		gbc_btnConnectToArduino.insets = new Insets(0, 0, 5, 0);
		gbc_btnConnectToArduino.gridx = 11;
		gbc_btnConnectToArduino.gridy = 0;

		final EditableTableModel etm = new EditableTableModel();
		settingTable = new JTable(etm);
		//	settingTable.get

		settingTable.setColumnSelectionAllowed(true);
		settingTable.setCellSelectionEnabled(true);
		settingTable.setGridColor(Color.BLACK);
		settingTable.setShowHorizontalLines(true);
		settingTable.setShowVerticalLines(true);
		GridBagConstraints gbc_settingTable = new GridBagConstraints();
		gbc_settingTable.insets = new Insets(0, 0, 5, 5);
		//gbc_settingTable.fill = GridBagConstraints.BOTH;
		gbc_settingTable.anchor = GridBagConstraints.CENTER;
		gbc_settingTable.gridwidth = 12;
		gbc_settingTable.gridheight = 12;
		gbc_settingTable.gridx = 0;
		gbc_settingTable.gridy = 1;
		etm.setColumnCount(11);
		etm.setRowCount(5);
		frame_1.getContentPane().add(settingTable, gbc_settingTable);
		frame_1.getContentPane().add(btnConnectToArduino, gbc_btnConnectToArduino);

		final Choice portMenu = new Choice();
		//	portMenu.setBounds(21, 32, 102, 29);
		GridBagConstraints gbc_portMenu = new GridBagConstraints();
		gbc_portMenu.insets = new Insets(0, 0, 5, 5);
		gbc_portMenu.fill = GridBagConstraints.HORIZONTAL;
		gbc_portMenu.gridwidth = 4;
		gbc_portMenu.gridx = 0;
		gbc_portMenu.gridy = 0;
		ArrayList<String> portList = SerialComm.getPortList();
		for (int i = 0; i < portList.size(); i++){
			portMenu.add((String) portList.get(i));
		}
		frame_1.getContentPane().add(portMenu, gbc_portMenu);

		btnConnectToArduino.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					System.out.println(portMenu.getSelectedItem());
					c.establishSerialConnection(portMenu.getSelectedItem());

					boolean handShakeComplete = false;
					Thread.sleep(2000);
					c.getSerialComm().out.write((byte)33);
					long initialTime = System.nanoTime();
					while (!handShakeComplete && System.nanoTime()-initialTime < 2000000000){
						if (c.getSerialComm().in.available()> 0 && c.getSerialComm().in.read()==33){
							handShakeComplete = true;
							JOptionPane.showMessageDialog(null, "Arduino connection established!");
						}
					}
					if (System.nanoTime() - initialTime >= 2000000000L && !handShakeComplete){
						JOptionPane.showMessageDialog(null, "Device did not respond!");
					}
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Connection failed!");
					e1.printStackTrace();
				}
			}
		});


		txtLengthPerTrial = new JTextField();
		txtLengthPerTrial.setText("Length Per Trial");
		GridBagConstraints gbc_txtLengthPerTrial = new GridBagConstraints();
		gbc_txtLengthPerTrial.insets = new Insets(0, 0, 5, 0);
		gbc_txtLengthPerTrial.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtLengthPerTrial.gridx = 12;
		gbc_txtLengthPerTrial.gridy = 4;
		frame_1.getContentPane().add(txtLengthPerTrial, gbc_txtLengthPerTrial);
		txtLengthPerTrial.setColumns(10);



		txtIti = new JTextField();
		txtIti.setText("ITI");
		GridBagConstraints gbc_txtIti = new GridBagConstraints();
		gbc_txtIti.insets = new Insets(0, 0, 5, 0);
		gbc_txtIti.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtIti.gridx = 12;
		gbc_txtIti.gridy = 5;
		frame_1.getContentPane().add(txtIti, gbc_txtIti);
		txtIti.setColumns(10);

		txtLengthPerTrial.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {//when Enter is hit
				try {
					lengthPerTrial = Integer.parseInt(txtLengthPerTrial.getText());
				} catch (Exception NumbersFormatException){
					//do nothing
					System.out.println("Not a Number");
				}
			}	
		});
		txtLengthPerTrial.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {

			}

			@Override
			public void focusLost(FocusEvent e) {
				try {

					lengthPerTrial = Integer.parseInt(txtLengthPerTrial.getText());
				} catch (Exception NumbersFormatException){
					//do nothing
					System.out.println("Not a Number");
				}
			}

		});

		txtIti.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {//when Enter is hit
				try {
					iti = Integer.parseInt(txtIti.getText());
				} catch (Exception NumbersFormatException){
					//do nothing
					System.out.println("Not a Number");
				}
			}	
		});
		txtIti.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {

			}

			@Override
			public void focusLost(FocusEvent e) {
				try {

					iti = Integer.parseInt(txtIti.getText());
				} catch (Exception NumbersFormatException){
					//do nothing
					System.out.println("Not a Number");
				}
			}

		});

		txtCommands = new JTextField();
		txtCommands.setText("Commands");
		GridBagConstraints gbc_txtCommands = new GridBagConstraints();
		gbc_txtCommands.insets = new Insets(0, 0, 5, 0);
		gbc_txtCommands.fill = GridBagConstraints.BOTH;
		gbc_txtCommands.gridx = 12;
		gbc_txtCommands.gridy = 7;
		frame_1.getContentPane().add(txtCommands, gbc_txtCommands);
		txtCommands.setColumns(10);

		txtCycles = new JTextField();

		txtCycles.setText("Cycles");
		GridBagConstraints gbc_txtCycles = new GridBagConstraints();
		gbc_txtCycles.insets = new Insets(0, 0, 5, 0);
		gbc_txtCycles.fill = GridBagConstraints.BOTH;
		gbc_txtCycles.gridx = 12;
		gbc_txtCycles.gridy = 8;
		frame_1.getContentPane().add(txtCycles, gbc_txtCycles);
		txtCycles.setColumns(10);

		txtCommands.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {//when Enter is hit
				try {
					numberOfCommands = Integer.parseInt(txtCommands.getText());
					etm.setRowCount(numberOfCommands);
				} catch (Exception NumbersFormatException){
					//do nothing
					System.out.println("Not a Number");
				}
			}	
		});
		txtCommands.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {

			}

			@Override
			public void focusLost(FocusEvent e) {
				try {
					numberOfCommands = Integer.parseInt(txtCommands.getText());
					etm.setRowCount(numberOfCommands);


				} catch (Exception NumbersFormatException){
					//do nothing
					System.out.println("Not a Number");
				}
			}

		});
		txtCycles.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {//when Enter is hit
				try {
					totalCycles = Integer.parseInt(txtCycles.getText());

				} catch (Exception NumbersFormatException){
					//do nothing
					System.out.println("Not a Number");
				}
			}	
		});
		txtCycles.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {

			}

			@Override
			public void focusLost(FocusEvent e) {
				try {
					totalCycles = Integer.parseInt(txtCycles.getText());

				} catch (Exception NumbersFormatException){
					//do nothing
					System.out.println("Not a Number");
				}
			}

		});
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c.run();
			}
		});
		
		JButton btnUpdateSetting = new JButton("Update Setting");
		GridBagConstraints gbc_btnUpdateSetting = new GridBagConstraints();
		gbc_btnUpdateSetting.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnUpdateSetting.insets = new Insets(0, 0, 5, 0);
		gbc_btnUpdateSetting.gridx = 12;
		gbc_btnUpdateSetting.gridy = 9;
		frame_1.getContentPane().add(btnUpdateSetting, gbc_btnUpdateSetting);

		btnUpdateSetting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				//REMEMBER TO CHECK IF NUMBER OF STAGES AINT NULL


				ChannelsStatusAndTimeStamp[] ctTable = new ChannelsStatusAndTimeStamp[numberOfCommands];
				for(int i = 0; i < settingTable.getRowCount(); i++){
					//boolean[] channelStatus = new boolean[10];
					System.out.println(settingTable.getRowCount());
					short temp = 0;
					ChannelsStatusAndTimeStamp cts = new ChannelsStatusAndTimeStamp();
				
					for (int j = 0; j < settingTable.getColumnCount(); j++){
						if (j == 0){
				
							cts.setTimeStamp((long)settingTable.getValueAt(i, 0));
			
						}
						//channelStatus[j] = (boolean) settingTable.getValueAt(i, j);
						else{
							if (settingTable.getValueAt(i,j) != null){
								if ((boolean)settingTable.getValueAt(i, j) == true){
									temp = (short) (temp + Math.pow(2, 16-j));//check this EHHH
								}
							}
						}

					}
					cts.setChannelsStatus(temp);
					ctTable[i] = cts;
				}


				setting = new Setting();
				setting.setChannelsStatusAndTimeStamp(ctTable);
				setting.setTotalCycles(totalCycles);
				setting.setIti(iti);
				setting.setlengthPerTrial(lengthPerTrial);
				//setting.setLengthOfSequence(lengthOfSequence);
				c.updateSetting(setting);
				System.out.println("Setting Updated");
				pBar.setMaximum(c.getSetting().getTotalCycles());
				//etaBar.setMaximum(c.getSetting().getTotalCycles()*);

				System.out.println(c.getSetting().toString());

				/*
				catch(Exception nullPointerException){

					JOptionPane.showMessageDialog(null, "Please fill up the setting table completely");
				}
				 */

			}
		});



		GridBagConstraints gbc_btnStart = new GridBagConstraints();
		gbc_btnStart.fill = GridBagConstraints.BOTH;
		gbc_btnStart.insets = new Insets(0, 0, 5, 0);
		gbc_btnStart.gridx = 12;
		gbc_btnStart.gridy = 10;
		frame_1.getContentPane().add(btnStart, gbc_btnStart);



		JButton btnStop = new JButton("Stop");
		GridBagConstraints gbc_btnStop = new GridBagConstraints();
		gbc_btnStop.insets = new Insets(0, 0, 5, 0);
		gbc_btnStop.fill = GridBagConstraints.BOTH;
		gbc_btnStop.gridwidth = 1;
		gbc_btnStop.gridx = 12;
		gbc_btnStop.gridy = 11;
		frame_1.getContentPane().add(btnStop, gbc_btnStop);


		pBar = new JProgressBar();
		//progressBar.setMaximum(c.getSetting().getTotalCycles());
		pBar.setMaximum(100);
		pBar.setMinimum(0);
		pBar.setValue(0);
		frame_1.getContentPane().add(pBar);


		GridBagConstraints gbc_pBar = new GridBagConstraints();
		gbc_pBar.insets = new Insets(0, 0, 5, 5);
		gbc_pBar.fill = GridBagConstraints.BOTH;
		gbc_pBar.gridwidth = 3;
		gbc_pBar.gridx = 0;
		gbc_pBar.gridy = 13;
		frame_1.getContentPane().add(pBar, gbc_pBar);


	}
	private void updateProgressBar(){
		pBar.setValue(c.getCycleCount());
		pBar.setString(c.getCycleCount() + "/" + c.getSetting().getTotalCycles());
		pBar.setStringPainted(true);
		//p

	}

}
