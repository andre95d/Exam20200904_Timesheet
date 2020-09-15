package timesheet;

public class Activity {
	private boolean isOpen=true;
	private String name;
	private Project project;
	public Activity(String name, Project project) {
		this.name=name;
		this.project=project;
	}
	public boolean isOpen() {
		return this.isOpen;
	}
	public void setOpen(boolean value) {
		this.isOpen=value;
	}
	public void setProject(Project project2) {
		// TODO Auto-generated method stub
		this.project=project2;
	}
	public Project getProject() {
		return this.project;
	}
	@Override
	public String toString() {
		return this.name;
	}
}
