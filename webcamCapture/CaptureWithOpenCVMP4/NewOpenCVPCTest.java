package webcamCapture.CaptureWithOpenCVMP4;


import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import webcamCapture.CaptureWithOpenCVMP4.*;

import com.github.sarxos.webcam.WebcamResolution;

import org.opencv.imgcodecs.*;
 
public class NewOpenCVPCTest {
	public static void main(String[] args) throws InterruptedException{
		try {
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			WebcamCaptureToMP4 wpc = new WebcamCaptureToMP4();
			VideoCapture cam = new VideoCapture(0);
			//cam.open(0);
			//cam.set(videoio., value)
		//	cam.setViewSize(WebcamResolution.HD720.getSize());
			
			wpc.setFolderPathAndVideoName("/home/ray/WebcamTest", "sadLife5");
			System.out.println(cam.get(Videoio.CAP_PROP_FPS));
			wpc.start(cam);
			wpc.prepareWriter("X264");
			wpc.startSaving();
			Thread.sleep(10000);
			wpc.finishSaving();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}