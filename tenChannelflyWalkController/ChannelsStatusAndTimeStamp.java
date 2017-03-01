package tenChannelflyWalkController;

public class ChannelsStatusAndTimeStamp {
	private short channelsStatus;
	private long timeStamp;
	public ChannelsStatusAndTimeStamp(short channelsStatus, long timeStamp) {	
		this.channelsStatus = channelsStatus;
		this.timeStamp = timeStamp;		
	}
	public ChannelsStatusAndTimeStamp(){
		this.channelsStatus = 0;
		this.timeStamp = 0;
	}
	public void setChannelsStatus(short channelsStatus) {
		this.channelsStatus = channelsStatus;
	}
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	public short getChannelsStatus() {
		return channelsStatus;
	}
	public long getTimeStamp() {
		return timeStamp;
	}
}
