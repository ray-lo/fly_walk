package tenChannelflyWalkController;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import gnu.io.*;

public class SerialComm {
	// if working on linux, remember to create softlink between /dev/ttyACM0 to /dev/ttyUSB1
	//otherwise rxtx library may not be able to find the port
	//type the following into command line
	//sudo ln -sf /dev/ttyACM0 /dev/ttyUSB1
	//
	
	//being of trial
	
//END of TRIAL
	SerialPort serialPort;
	public InputStream in;
	public OutputStream out;

	public SerialComm()
	{	
		super();
		
	}
	public boolean connect ( String portName ) throws Exception
	{
		boolean success;

		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		if ( portIdentifier.isCurrentlyOwned() )
		{
			System.out.println("Error: Port is currently in use");
			success = false;
		}
		else
		{
			CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);

			if ( commPort instanceof SerialPort )
			{
				serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE); // First parameter is baud rate for the port

				in = serialPort.getInputStream();
				out = serialPort.getOutputStream();
				success = true;
			}
			else
			{
				System.out.println("Error: Only serial ports are handled by this example.");
				success = false;
			}
		}
		return(success);
	}

	public void disconnect() {
		serialPort.close();
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public InputStream getInputStream() {
		return(in);
	}

	public OutputStream getOutputStream() {
		return(out);
	}
	static void listPorts()
	{
		java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
		while ( portEnum.hasMoreElements() ) 
		{
			CommPortIdentifier portIdentifier = portEnum.nextElement();
			System.out.println(portIdentifier.getName()  +  " - " +  getPortTypeName(portIdentifier.getPortType()) );
		}        
	}
	

	public static ArrayList<String> getPortList()	{
		ArrayList<String> portList = new ArrayList<String>();
		java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();

		while ( portEnum.hasMoreElements() ) 
		{
			CommPortIdentifier portIdentifier = portEnum.nextElement();
			portList.add(portIdentifier.getName());	
		}   
	
		return portList;
	}

	static String getPortTypeName ( int portType )
	{
		switch ( portType )
		{
		case CommPortIdentifier.PORT_I2C:
			return "I2C";
		case CommPortIdentifier.PORT_PARALLEL:
			return "Parallel";
		case CommPortIdentifier.PORT_RAW:
			return "Raw";
		case CommPortIdentifier.PORT_RS485:
			return "RS485";
		case CommPortIdentifier.PORT_SERIAL:
			return "Serial";
		default:
			return "unknown type";
		}
	}

}

