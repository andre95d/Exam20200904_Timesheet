package timesheet;

public class Project {
	public String name;
	public int maxHours;
	public Project(String name, int hours) {
		this.name=name;
		this.maxHours=hours;
	}
	@Override
	public String toString() {
		return this.name + ": " + this.maxHours;
	}

	public Integer getHours(){return this.maxHours;}


}
