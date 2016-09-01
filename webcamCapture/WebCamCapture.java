package webcamCapture;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.*;
import org.opencv.videoio.VideoCapture;

import flyWalkCV.FrameAccessHelper;
import flyWalkCV.SimpleImageDisplay;

public class WebCamCapture extends VideoCapture{
	public static void main(String[] args){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		FrameAccessHelper helper = new FrameAccessHelper();
		VideoCapture camCapture = new VideoCapture(0);
		Mat currentMat = new Mat();
		System.out.println("WebCam open? " + camCapture.isOpened());
		System.out.println(camCapture.toString());
		SimpleImageDisplay display = new SimpleImageDisplay();
		display.setVisible(true);
		while(true){

			if (camCapture.read(currentMat)){
				display.updateImage(currentMat);
			}
			//System.out.println(i++);
		}

	}
}
