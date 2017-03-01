package webcamCapture.CaptureWithOpenCVMP4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
//import org.opencv.highgui.VideoCapture;
import org.opencv.videoio.Videoio;

import webcamCapture.MatAndTimeLong;

public class WebcamCaptureToMP4 {
	//
	private Producer p;
	private Consumer c;
	private BlockingQueue<MatAndTimeLong> sharedQueue;
	private String folderPath;
	private String videoName;
	private VideoCapture cam;
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
	public void setFolderPathAndVideoName(String folderPath, String videoName){
		setFolderPath(folderPath);
		setVideoName(videoName);
	}
	public WebcamCaptureToMP4() throws IOException {
		sharedQueue = new LinkedBlockingQueue<MatAndTimeLong>();
		p = new Producer(sharedQueue);
		c = new Consumer(sharedQueue);
	}
	public void start(VideoCapture cam){
		this.cam = cam;
		File folder = new File(folderPath);
	
			System.out.println("Cam set up");
			p.setUpCamera(this.cam);
			p.startCamera();
			c.setFolderPath(folderPath);
			c.setVideoName(videoName);
			Thread pThread = new Thread(p);
			Thread cThread = new Thread(c);
			//cam.get() 
			pThread.start();
			cThread.start();
			System.out.println("P and C running");
	}

	public void prepareWriter(String codec) throws FileNotFoundException, IOException{
		char c1 = codec.charAt(0);
		char c2 = codec.charAt(1);
		char c3 = codec.charAt(2);
		char c4 = codec.charAt(3);
		c.setFourcc(VideoWriter.fourcc(c1, c2, c3, c4));
		c.setFps(cam.get(Videoio.CAP_PROP_FPS));
		c.setSize(new Size(cam.get(Videoio.CAP_PROP_FRAME_WIDTH),cam.get(Videoio.CAP_PROP_FRAME_HEIGHT)));
		c.prepareWriter();
	}

	public void startSaving(){
			c.startSaving();
	
	}
	public void finishSaving() throws IOException{
		p.sendSentinel();
		System.out.println("Sentinel Sent");
	}
	public void closeWebcamProducerConsumer() throws IOException{
		p.kill();
		c.kill();
	}

}
