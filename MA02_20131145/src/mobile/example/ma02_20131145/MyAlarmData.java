package mobile.example.ma02_20131145;

public class MyAlarmData  {

	private String time;
	private String op;
	
	public MyAlarmData(String time, String op) {
		this.time = time;
		this.op = op;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	
}
