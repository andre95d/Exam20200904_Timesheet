package timesheet;

public class Day implements Comparable<Day> {
	private boolean isHoliday;
	private Integer value;
	private int weekday;
	public Day(int value) {
		this.value=value;
	}
	public void setAsHoliday(boolean isHoliday) {
		this.isHoliday=isHoliday;
	}
	@Override
	public int compareTo(Day arg0) {
		// TODO Auto-generated method stub
		return this.value.compareTo(arg0.getValue());
	}
	public int getValue() {
		// TODO Auto-generated method stub
		return this.value;
	}
	public boolean isHoliday() {
		return this.isHoliday;
	}
	public void setWeekDay(int i) {
		this.weekday=i;
	}
	public int getWeekDay() {
		return this.weekday;
	}
}
