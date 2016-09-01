package webcamCapture.CaptureWithOpenCVMP4;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
//import org.opencv.highgui.VideoCapture;

import webcamCapture.MatAndTimeLong;

import java.util.concurrent.BlockingQueue;


public class Producer implements Runnable {
	
	private BlockingQueue<MatAndTimeLong> sharedQueue;
	private VideoCapture webcam;
	private boolean capturing;
	private boolean firstTime;
	private Mat m;
	
	
	public Producer(BlockingQueue<MatAndTimeLong> sharedQueue) {
		this.sharedQueue = sharedQueue;
		capturing = false;//see if this breaks anything eh. used to be false and started by start camera
		firstTime = true;
		m = new Mat();
	}
	
	public void setUpCamera(VideoCapture wb){
		webcam = wb;
	
	}
	
	public void startCamera(){
		//webcam.open();
		capturing = true;
	}
	

	@Override
	public synchronized void run() {
		while(capturing){//eh?
			try{
				if (firstTime){
					System.out.println("First frame grabbed!");
					firstTime = false;
				}
				webcam.read(m);
		sharedQueue.add(new MatAndTimeLong(m, System.currentTimeMillis()));
				}
			catch(NullPointerException e){
				e.printStackTrace();
				System.out.println("Camera was not opened");
			}
			System.out.println(sharedQueue.size());
		}
		if (!capturing){
			sharedQueue.add(null);
		}
	}

	public synchronized void kill() {
		sharedQueue.add(null);
		capturing = false;
		
	}
	public void sendSentinel(){
		sharedQueue.add(new MatAndTimeLong(null, -1));
	}
}
