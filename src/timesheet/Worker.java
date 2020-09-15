package timesheet;

import java.util.Arrays;

public class Worker {

	private String name;
	private String surname;
	private String id;
	public String hourprofile;
	public Activity activity;

	public Worker(String name, String surname, String profileID, String hrprofile) {
		// TODO Auto-generated constructor stub
		this.name=name;
		this.surname=surname;
		this.id=profileID;
		this.hourprofile=hrprofile;
	}


	public String getID() {
		// TODO Auto-generated method stub
		return this.id;
	}
	
	@Override
	public String toString() {
		return this.name + " " + this.surname;
	}

	public Project setActivity(Activity a){
		this.activity = a;
		return a.getProject();
	}
}
