package tenChannelflyWalkController;

import java.io.BufferedOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class ViewOutputStream extends FilterOutputStream {
	public ViewOutputStream(OutputStream output){
		super(output);
	//	this.ps=ps;
	}
	
	public ViewOutputStream(BufferedOutputStream output){
		super(output);
	}
	private PrintStream ps;
	
	public void write(byte[] x) throws IOException{
		super.write(x);
		for (int i=0; i< x.length; i++){
		System.out.println(Integer.toHexString((int)x[i])+" ");
		}
	}
	public void write(int x) throws IOException{
		super.write(x);
		
		System.out.println(Integer.toBinaryString(x));
	}
}
