package webcamCapture;

import java.io.File;
import java.io.IOException;

import org.jcodec.api.SequenceEncoder;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;

public class NOPC {
	public static void main(String[] args) throws IOException{
		Webcam cam = Webcam.getDefault();
		cam.setViewSize(WebcamResolution.VGA.getSize());
		cam.open();
		
		File f = new File("/Users/ray/wow.mp4");
		SequenceEncoder enc = new SequenceEncoder(f);
		for (int i = 0 ; i <280; i++){
		enc.encodeImage(cam.getImage());
		}
		enc.finish();
		System.out.println("donezo");
	}
}
