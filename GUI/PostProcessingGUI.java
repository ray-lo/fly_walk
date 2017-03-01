package GUI;

import java.awt.EventQueue;
import java.awt.FileDialog;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.opencv.core.Core;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import flyWalkCV.CustomMatOperator;
import flyWalkCV.FlyDetector;
import flyWalkCV.FrameAccessHelper;
import flyWalkCV.FrameSlider;
import flyWalkCV.VideoCaptureWithName;
import flyWalkCV.VideoSorter;

import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.awt.BorderLayout;
import java.awt.Choice;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFileChooser;


public class PostProcessingGUI {
	private JFrame frame;
	private VideoSorter vs;
	private VideoCapture currentVC;
	private FrameAccessHelper frameAccessHelper;
	private CustomMatOperator customMatOperator;
	private int currentFrameNumber;
	private FlyDetector flyDetector;
	private JPanel panel;
	private JLabel videoImageLabel;
	private FrameSlider slider;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); //REMEMBER TO ALWAYS LOAD THE NATIVE LIBRARY
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PostProcessingGUI window = new PostProcessingGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PostProcessingGUI() {
		vs = new VideoSorter();
		frameAccessHelper = new FrameAccessHelper();
		flyDetector = new FlyDetector();
		customMatOperator = new CustomMatOperator();
		initialize();
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		/*
		 * place holder inb4 propert folder and vid selection UI comes in
		 */

		//		currentVC = vs.getVidCapWithNameArrayList().get(0).getVideoCapture();
		currentFrameNumber = 2000;

		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		videoImageLabel = new JLabel("Video");
		//frame.setBounds(100, 100, (int)currentVC.get(Videoio.CAP_PROP_FRAME_WIDTH)+50, (int)currentVC.get(Videoio.CAP_PROP_FRAME_HEIGHT)+50);	
		panel.add(videoImageLabel);
		//Mat currentMat = frameAccessHelper.returnNthMat(currentFrameNumber, currentVC);

		//customMatOperator.drawSquareAroundDetectedPoints(currentMat,flyDetector.returnPointArray(currentMat),20);


		//	ImageIcon i = new ImageIcon(frameAccessHelper.Mat2BufferedImage(currentMat));
		//videoImageLabel.setIcon(i);


		JPanel listPanel = new JPanel();
		Choice vcChoice = new Choice();


		JButton btnChooseFolder = new JButton("Choose Folder");
		listPanel.add(btnChooseFolder);
		listPanel.add(vcChoice);
		frame.getContentPane().add(listPanel,BorderLayout.NORTH);
		btnChooseFolder.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				vcChoice.removeAll();
				JFileChooser folderChooser = new JFileChooser("Choose Folder containing videos");
				folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				folderChooser.showOpenDialog(frame);
				vs.populateListFromFolder(folderChooser.getSelectedFile().getAbsolutePath());
				ArrayList<VideoCaptureWithName> vcList = vs.getVidCapWithNameArrayList();
				for (int j = 0; j < vcList.size(); j++){
					vcChoice.add(vcList.get(j).getName());
				}
			}		
		});
		vcChoice.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				for (VideoCaptureWithName vcwn: vs.getVidCapWithNameArrayList()){
					if (vcChoice.getSelectedItem().equals(vcwn.getName())){
					currentVC = vcwn.getVideoCapture();
					slider.setMaximum((int)currentVC.get(Videoio.CAP_PROP_FRAME_COUNT)-1);
					}
				}
			}
		});
		slider = new FrameSlider(1);
		JPanel sliderPanel = new JPanel();
		frame.getContentPane().add(sliderPanel, BorderLayout.SOUTH);
		sliderPanel.add(slider, BorderLayout.SOUTH);
		slider.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {	
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				System.out.println(slider.getValue());
				updateFrameDisplay(frameAccessHelper.returnNthMat(slider.getValue(), currentVC));
			
			}
			@Override
			public void mouseEntered(MouseEvent e) {			
			}
			@Override
			public void mouseExited(MouseEvent e) {	
			}
		});
	}
	private void updateFrameDisplay(Mat m){
		customMatOperator.drawSquareAroundDetectedPoints(m, flyDetector.returnPointArray(m), 10);
		videoImageLabel.setIcon(new ImageIcon(frameAccessHelper.Mat2BufferedImage(m)));
		System.out.println("DONE");
	}
}
