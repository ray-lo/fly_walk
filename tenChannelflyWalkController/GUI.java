package tenChannelflyWalkController;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Choice;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.table.TableModel;

public class GUI {

	JFrame frame;
	ControllerThread c;
	private Setting setting;
	private JTable table;
	private JProgressBar pBar;
	private JProgressBar etaBar;

	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		c = new ControllerThread();

		this.table = new JTable();
		this.table.setShowVerticalLines(true);
		this.table.setShowGrid(true);
		this.table.setBounds(30, 40, 300, 150);
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

		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		final Button startButton = new Button("START");
		startButton.setBounds(323, 243, 117, 29);
		frame.getContentPane().add(startButton);
		final JButton stopButton = new JButton("STOP");
		stopButton.setBounds(200, 243, 117, 29);
		frame.getContentPane().add(stopButton);
		stopButton.setEnabled(false);

		final Choice choice = new Choice();
		choice.setBounds(200, 10, 250, 27);
		ArrayList<String> portList = SerialComm.getPortList();
		for (int i = 0; i < portList.size(); i++){
			choice.add((String) portList.get(i));
		}
		frame.getContentPane().add(choice);

		JButton updateSettingButton = new JButton("Update Setting");
		updateSettingButton.setBounds(6, 6, 117, 29);
		updateSettingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String userAns = JOptionPane.showInputDialog(null, "How Long Is Your Sequence?");
				String userAns2 = JOptionPane.showInputDialog(null, "How many cycles?");
				try {

					int sequenceLength = Integer.parseInt(userAns);
					ChannelAndDuration[] cdTable = new ChannelAndDuration[sequenceLength];
					for(int i = 0; i < sequenceLength; i++){
						int channel  = Integer.parseInt(JOptionPane.showInputDialog(null, "Sequence " + (i+1) + ", open channel..."));
						double durationSec = Double.parseDouble(JOptionPane.showInputDialog("Seconds that channel "+ channel + " is open for: "));

						long durationNano = (long)((durationSec) * 1000000000L);

						ChannelAndDuration cdEntry = new ChannelAndDuration(channel, durationNano);
						cdTable[i] = cdEntry;

					}
					int totalCycles = Integer.parseInt(userAns2);
					setting = new Setting(cdTable, totalCycles);
					c.updateSetting(setting);
					System.out.println("Setting Updated");
					pBar.setMaximum(c.getSetting().getTotalCycles());
					//etaBar.setMaximum(c.getSetting().getTotalCycles()*);
					updateTableDisplay();
				}
				catch(Exception NumbersFormatException){
					JOptionPane.showMessageDialog(null, "Please type in a valid intgeger");
				}

			}
		});
		frame.getContentPane().add(updateSettingButton);

		JButton connectToArduinoButton = new JButton("Connect To Arduino");
		connectToArduinoButton.setBounds(200, 35, 250, 29);
		connectToArduinoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					System.out.println(choice.getSelectedItem());
					c.establishSerialConnection(choice.getSelectedItem());

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
		frame.getContentPane().add(connectToArduinoButton);

		pBar = new JProgressBar();
		pBar.setBounds(18, 217, 161, 44);
		//progressBar.setMaximum(c.getSetting().getTotalCycles());
		pBar.setMaximum(100);
		pBar.setMinimum(0);
		pBar.setValue(0);
		frame.getContentPane().add(pBar);

		etaBar = new JProgressBar();
		etaBar.setBounds(18, 252, 161, 20);
		frame.getContentPane().add(etaBar);


		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(c.getSerialComm()==null){
					JOptionPane.showMessageDialog(null, "Connection not yet established");
				}
				else if (c.getSetting() == null){
					JOptionPane.showMessageDialog(null, "No Setting entered yet!");
				}
				else{
					c.start();
					updateProgressBar();
					updateTableDisplay();
					startButton.setEnabled(false);
					stopButton.setEnabled(true);
				}
			}
		});

		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c.kill();
				while(c.getCurrentChannel()!= -1){
					//wait
					System.out.println("Waiting");
				}
				System.out.println("Is Running? " + c.getIsRunning());
				System.out.println(c.getCurrentChannel());
				c.getSerialComm().disconnect();
				c = new ControllerThread();
				JOptionPane.showMessageDialog(null, "Controller Terminated. Remember to re-establish connection.");
				c.updateSetting(setting);
				c.addUpdaterAction(progressUpdater);
				startButton.setEnabled(true);
				stopButton.setEnabled(false);
			}
		});
	}
	
	private void updateProgressBar(){
		pBar.setValue(c.getCycleCount());
		pBar.setString(c.getCycleCount() + "/" + c.getSetting().getTotalCycles());
		pBar.setStringPainted(true);
		//p

	}
	private void updateTableDisplay(){
		frame.remove(this.table);

		String[] columnNames = {"Channel (0 to 16)", "Duration (sec)"};
		Object[][] table = new Object[setting.getLengthOfSequence()+1][2];
		table[0] = columnNames;
		for (int i = 0; i < setting.getLengthOfSequence(); i++){
			table[i+1][0] = setting.getChannelAndDuration()[i].getChannel();
			table[i+1][1] = setting.getChannelAndDuration()[i].getDuration()/1000000000.00;
		}

		this.table = new JTable(table, columnNames);
		this.table.setShowVerticalLines(true);
		this.table.setBounds(30, 40, 160, 150);
		this.table.setEnabled(false);
		frame.getContentPane().add(this.table);

	}
}
