package webcamCapture;

 import org.opencv.core.Mat;


public class MatAndTimeLong{
	private Mat m;
	private long time;
	public MatAndTimeLong(Mat m, long time){
		this.m = m;
		this.time = time;
	}
	public Mat getMat() {
		return m;
	}
	public void setMat(Mat m) {
		this.m = m;
	}
	public long getTimeLong() {
		return time;
	}
	public void setTimeLong(int time) {
		this.time = time;
	}
	
}
