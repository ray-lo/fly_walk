package tenChannelflyWalkController;



import java.io.IOException;



public class ControllerThread extends Thread  {
	private SerialComm sc;
	private Setting setting;
	private int cycleCount;
	private int currentChannel;
	
	private int currentCommandIndex;
	private boolean isRunning;
	private long startTime;
	private long currentTimeAccumulation;

	private Runnable updater;

	public ControllerThread() {
		cycleCount = 0;
		isRunning = true;
		currentCommandIndex = 0;
		startTime = 0;
		currentTimeAccumulation = 0;
	}


	public void establishSerialConnection(String serialCommPath) throws Exception{
		sc = new SerialComm();
		sc.connect(serialCommPath); // Adjust this to be the right port for your machine
		//InputStream in = sc.getInputStream();
		//	OutputStream out = sc.getOutputStream();
		// InputStream and OutputStream are now avaiable for use
		// insert code below to use them

		//	BufferedInputStream bufIn= new BufferedInputStream(in);
		//		ViewInputStream viewIn = new ViewInputStream(bufIn);
		//		this.in = viewIn;
		//	ViewOutputStream viewOut = new ViewOutputStream(out);
		//	this.out = viewOut;//maybe fix me later
	}
	public void updateSetting(Setting setting){
		this.setting = setting;
	}
	public void run(){
		while (isRunning){
			ChannelsStatusAndTimeStamp currentCSTS = setting.getChannelsStatusAndTimeStamp()[currentCommandIndex];

			long currentTime = System.currentTimeMillis();
			if (currentTime >= startTime + currentTimeAccumulation + currentCSTS.getTimeStamp() ){
				try {
					short currentStatus = currentCSTS.getChannelsStatus();
					byte first8Channels = (byte)(currentStatus >> 8);
					byte remaining2Channels = (byte)(currentStatus & 0xFF);
					sc.out.write('@');
					sc.out.write(first8Channels);
					sc.out.write(remaining2Channels);
				} catch (IOException e) {
					e.printStackTrace();
				}
				currentTimeAccumulation = currentTimeAccumulation +
							(cycleCount* (+setting.getTotalLengthPerTrial()))
								+ currentCSTS.getTimeStamp();
				if (currentCommandIndex < setting.getChannelsStatusAndTimeStamp().length -1){
					currentCommandIndex ++;
				}
				else {
					currentCommandIndex = 0;
					//cycleCount++;
				}
				
			}			
			if (currentTime >= startTime 
					+ ((cycleCount + 1)* (setting.getTotalLengthPerTrial()))){
				cycleCount++;	
			}
			
			if (cycleCount >= setting.getTotalCycles()){
				isRunning = false;
			}
		}
	}
	public Setting getSetting() {
		return setting;
	}
	public short getCurrentChannelsStatus() {
		return setting.getChannelsStatusAndTimeStamp()[currentCommandIndex].getChannelsStatus();
	}
	public int getCurrentChannel() {
		return currentChannel;
	}
	public SerialComm getSerialComm(){
		return sc;
	}
	public synchronized void kill(){
		isRunning = false;
	}
	public synchronized boolean getIsRunning(){
		return isRunning;
	}
	public int getCycleCount() {
		return cycleCount;
	}
	
	public void progressUpdaterFirer(Runnable e){
		e.run();
	}
	public void addUpdaterAction(Runnable e){
		this.updater = e;
	}
}