package tenChannelflyWalkController;



import java.io.IOException;



public class Controller implements Runnable  {
	private SerialComm sc;
	private Setting setting;
	private int cycleCount;
	private int currentCommandIndex;
	private boolean isRunning;
	private long startTime;
	private long currentTimeAccumulation;


	private Runnable updater;

	public Controller() {
		cycleCount = 0;
		isRunning = true;
		currentCommandIndex = 0;
		startTime = 0;
		currentTimeAccumulation = 0;


	}
	public Controller(long startTime) {
		cycleCount = 0;
		this.startTime = startTime;
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

	/*	
	public void run(){
		while (isRunning){

			int lastChannel = setting.getChannelAndDuration()[0].getChannel();
			System.out.println("Last Channel: " + lastChannel);
			try {
				sc.out.write(lastChannel);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			long lastExecuteTime = System.nanoTime();
			int iterationCount = 0;
			if (setting != null){
//				boolean localIsRunning;
//				synchronized (this) {
//					localIsRunning = isRunning;
//				}
				while (cycleCount < setting.getTotalCycles() && isRunning){
					ChannelAndDuration currentCD = setting.getChannelAndDuration()[iterationCount % setting.getLengthOfSequence()];
					long currentExecuteTime = System.nanoTime();
					if (currentExecuteTime-lastExecuteTime <= currentCD.getDuration()){
						currentChannel = currentCD.getChannel();
					}
					else {
						iterationCount++;
						lastExecuteTime = currentExecuteTime;
						if (iterationCount % setting.getLengthOfSequence() == 0){
							cycleCount++;
							progressUpdaterFirer(updater);
							//newCycleOccured = true;
						}
					}
					if (lastChannel != currentChannel){
						try {
							sc.out.write(currentChannel);
							//System.out.println(currentChannel);
							lastChannel = currentChannel;
							System.out.println("Last Channel: " + lastChannel);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			if (cycleCount >= setting.getTotalCycles()){
				try {
					sc.out.write(0);
					currentChannel = -1;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				synchronized (this) {
					isRunning = false;
				}
				System.out.println(this + "ended");
				return;
			}

		}

		try {
			sc.out.write(0);
			currentChannel = -1;
			System.out.println(this + "killed");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	 */
	@Override
	public void run() {
		
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
							(cycleCount* (+setting.getLengthOfSequence()))
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
					+ ((cycleCount + 1)* (setting.getLengthOfSequence()))){
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

	public void setStartTime(long st){
		startTime = st;
	}

	public int getCurrentChannelsStatus() {
		return currentCommandIndex;
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