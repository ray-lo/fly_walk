package webcamCapture.CaptureWithOpenCVMP4;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
//import org.opencv.imgcodecs.Imgcodecs;
//import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.*;

import webcamCapture.MatAndTimeLong;

import java.awt.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;


public class Consumer implements Runnable{



	private BlockingQueue<MatAndTimeLong> sharedQueue;
	private VideoWriter writer;
	private boolean recording;
	private boolean saving;
	private boolean endSaving;
	private Mat currentImage;
	private int currentFrame;
	private String folderPath;
	private String videoName;
	private Size size;
	private double fps;
	private int fourcc;
	private int timerTextY;
	private ObjectOutputStream oos;
	
	public Consumer(BlockingQueue<MatAndTimeLong> sharedQueue) throws IOException {
		this.sharedQueue = sharedQueue;
		recording = true;
		saving = false;
		endSaving = false;
		currentFrame = 0;

	}

	@Override
	public synchronized void  run() {
		while(recording){
			MatAndTimeLong bitl;			
			try {
				bitl = sharedQueue.take();
				if (bitl.getTimeLong()!= -1){
					currentImage = bitl.getMat();
					//	System.out.println(bitl.getTimeLong());
					if (saving){
						//	enc.encodeImage(currentImage);
					//	System.out.println(String.valueOf(bitl.getTimeLong()));
						Imgproc.putText(currentImage,
										String.valueOf(bitl.getTimeLong()) + " ms",
										new Point(0,this.timerTextY),
										Core.FONT_HERSHEY_PLAIN, //font face
										1.0, // font size
										new Scalar(0,255,50));
						Imgproc.putText(currentImage,
								String.valueOf(currentFrame) + " frame",
								new Point(300,this.timerTextY),
								Core.FONT_HERSHEY_PLAIN, //font face
								1.0, // font size
								new Scalar(0,0,255));
						writer.write(currentImage);
						//Imgcodecs.imwrite(folderPath+ "/" + videoName+ currentFrame +".jpg", currentImage);
						//	JOptionPane.showInputDialog(null, "SequenceEncoder is null!");									
//						if (endSaving){
//							writer.release();
//							endSaving = false;						
//						}	
						oos.writeLong(bitl.getTimeLong());			
						currentFrame++;
						System.out.println("current frame" + currentFrame);
					}
					
					
				}
				else {
					//enc.finish();
					saving = false;
					writer.release();
					currentFrame = 0;
					oos.close();
					System.out.println("Saving Complete");
				}	
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void kill() throws IOException{
		recording = false;
	}

	public String getFolderPath() {
		return folderPath;
	}
	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}
	public String getVideoName() {
		return videoName;
	}
	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}
	public void startSaving(){
		//	enc = new SequenceEncoder(f);
		
		saving = true;
	}
	public void prepareWriter() throws FileNotFoundException, IOException{
		startObjectOutputStream();
		writer = new VideoWriter(folderPath + "/" + videoName + ".avi",fourcc ,fps, size, true);
	}
	
//	public void finishSaving() throws IOException{
//		try{
//			saving = false;
//			endSaving = true;
//		}catch(NullPointerException e){
//			e.printStackTrace();
//			JOptionPane.showInputDialog(null, "SequenceEncoder is null!");
//		}
//	}
	
	private void startObjectOutputStream() throws FileNotFoundException, IOException{
		oos= new ObjectOutputStream(new FileOutputStream(folderPath + "/" + videoName + ".ts"));
	}
		
	
	public Mat getCurrentImage(){
		return currentImage;
	}
	public void setSize(Size size) {
		this.size = size;
		this.timerTextY = (int)size.height - 10;
	}
	public void setFps(double fps) {
		
		this.fps = fps;
	}
	public void setFourcc(int fourcc) {
		this.fourcc = fourcc;
	}
}
