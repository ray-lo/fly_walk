package tenChannelflyWalkController;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;


public class ViewInputStream extends FilterInputStream {
	public ViewInputStream(InputStream arg0) {
		super(arg0);
	}
	
	public int read() throws IOException{
		int tempRead = super.read();
		System.out.println("0x" + Integer.toHexString(tempRead)+" ");
		return tempRead;
	}
}
