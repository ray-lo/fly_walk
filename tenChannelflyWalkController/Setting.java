package tenChannelflyWalkController;

public class Setting {
	private int totalCycles;
	private int lengthPerTrial;
	private int iti;
	private ChannelsStatusAndTimeStamp[] channelsStatusAndTimeStamp;
	
	public int getIti() {
		return iti;
	}
	public void setIti(int iti) {
		this.iti = iti;
	}
	
	public Setting() {

	}
	public int getTotalLengthPerTrial(){
		return lengthPerTrial+ iti;
	}
	public int getlengthPerTrial() {
		return lengthPerTrial;
	}
	public ChannelsStatusAndTimeStamp[] getChannelsStatusAndTimeStamp() {
		return channelsStatusAndTimeStamp;
	}
	public void setlengthPerTrial(int lengthPerTrial) {
		this.lengthPerTrial = lengthPerTrial;
	}
	public void setChannelsStatusAndTimeStamp(ChannelsStatusAndTimeStamp[] channelAndDuration) {
		this.channelsStatusAndTimeStamp = channelAndDuration;
	}
	public int getTotalCycles() {
		return totalCycles;
	}
	public void setTotalCycles(int totalCycles) {
		this.totalCycles = totalCycles;
	}
//	public double getTotalDuration(){ //THIS GUY HAS TO BE CHANGED SUMMER 2k16
//		long totalSeconds = 0;
//		for (int i = 0; i < channelsStatusAndTimeStamp.length; i++){
//			totalSeconds = totalSeconds+ channelsStatusAndTimeStamp[i].getDuration();
//		}
//		//FIX ME
//		return (double)totalSeconds ;
//	}
	public String toString(){
		String s = "";
		
		for (int i =0; i < channelsStatusAndTimeStamp.length; i++){
			s = s + "TimeStamp " + channelsStatusAndTimeStamp[i].getTimeStamp() +  " Status: " +channelsStatusAndTimeStamp[i].getChannelsStatus();
			s = s + '\r';
		}
		
		return s;
	}
}
