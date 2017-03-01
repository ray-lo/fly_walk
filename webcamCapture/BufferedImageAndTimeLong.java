package webcamCapture;

import java.awt.image.BufferedImage;

public class BufferedImageAndTimeLong{
	private BufferedImage bi;
	private long time;
	public BufferedImageAndTimeLong(BufferedImage bi, long time){
		this.bi = bi;
		this.time = time;
	}
	public BufferedImage getBufferedImage() {
		return bi;
	}
	public void setBufferedImage(BufferedImage bi) {
		this.bi = bi;
	}
	public long getTimeLong() {
		return time;
	}
	public void setTimeLong(int time) {
		this.time = time;
	}
	
}
