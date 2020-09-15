package timesheet;

import com.sun.glass.ui.Clipboard;

import java.sql.Time;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Timesheet {
	
	private Collection<Day> days = new TreeSet<>();
	private Collection<Project> projects = new TreeSet<>(Comparator.comparing(a -> a.name));
	private Collection<Activity> activities = new ArrayList<>();
	private Collection<Worker> workers = new ArrayList<>();
	private Map<String, List<Integer>> profiles = new TreeMap<>();
	private Integer workerId=0;
	private Integer profileId=0;
	private Collection<Report> reports= new ArrayList<>();
	private boolean flag = true;
	enum Giorno{
		Sun, Mon, Tue, Wed, Thu, Fri, Sat
	}

	//	for(int i=0; i<=364 ; i++) {
//		days.add(new Day(i+1));
//	}
	public Timesheet() {
		for(int i=0; i<=364 ; i++) {
			this.days.add(new Day(i+1));
		}
	}
	// R1
	public void setHolidays(int... holidays) {
		for(Integer h : holidays) {
			for(Day d:days) {
				if(h.equals(d.getValue()) && flag) {
					d.setAsHoliday(true);
				}
			}
		}
		flag = false;
	}
	
	public boolean isHoliday(int day) {
		for(Day d : days) {
			if (d.getValue() == day) return d.isHoliday();
		}
		return false;
	}
	
	public void setFirstWeekDay(int weekDay) throws TimesheetException {
		if(weekDay < 0 || weekDay>6 ) {
			throw new TimesheetException("Weekday not valid");
		}
		for (Day d:days) {
			d.setWeekDay(weekDay++);
			if(weekDay>6) {
				weekDay=0;
//			}else {
//				weekDay++;
			}
		}
	}
	
	public int getWeekDay(int day) throws TimesheetException {
		if(day < 1 || day>365 ) {
			throw new TimesheetException("day of year not valid");
		}
		for(Day d: days) {
	    	if(day == d.getValue()) return d.getWeekDay();
	    }
		return -1;
	}
	
	// R2
	public void createProject(String projectName, int maxHours) throws TimesheetException {
		if(maxHours<0) throw new TimesheetException();
		Optional<Project> p = projects.stream().filter(x-> x.name.equals(projectName)).findAny();
		if(!p.isPresent()) projects.add(new Project(projectName, maxHours));
		else p.get().maxHours = maxHours;
	}
	
	public List<String> getProjects() {
        return projects.stream()
				.sorted((a,b) -> {
					if(a.getHours() == b.getHours()) return a.name.compareTo(b.name);
					else return b.getHours().compareTo(a.getHours());
				})
				.map(a-> a.toString()).collect(Collectors.toList());
	}
	
	public void createActivity(String projectName, String activityName) throws TimesheetException {
		Optional<Project> value = projects.stream()
				.filter(a-> projectName.equals(a.name)).findAny();
		if(!value.isPresent()) throw new TimesheetException();
		Activity a = new Activity(activityName, value.get());
		activities.add(a);
		a.setProject(value.get());
	}
	
	public void closeActivity(String projectName, String activityName) throws TimesheetException {
		Optional<Project> p = projects.stream()
				.filter(i-> projectName.equals(i.name)).findAny();
		if(!p.isPresent()) throw new TimesheetException();
		Optional<Activity> a = activities.stream()
				.filter(b-> activityName.equals(b.toString())).findAny();
		if(!a.isPresent()) throw new TimesheetException();
		a.get().setOpen(false);
//		activities.
	}
	
	public List<String> getOpenActivities(String projectName) throws TimesheetException {
		Optional<Project> p = projects.stream()
				.filter(i-> projectName.equals(i.name)).findAny();
		if(!p.isPresent()) throw new TimesheetException();
		List<String> a = activities.stream()
				.filter(b-> b.getProject().equals(p.get()))
				.filter(c-> c.isOpen())
				.map(Activity::toString).sorted(String::compareTo)
				.collect(Collectors.toList());
		return a;
	}

	// R3
	public String createProfile(int... workHours) throws TimesheetException {
		if(workHours.length > 7) throw new TimesheetException();
		String var = String.valueOf(profileId++);
		profiles.put(String.valueOf(var),
				Arrays.stream(workHours).boxed().collect(Collectors.toList()));
        return var;
	}
	
	public String getProfile(String profileID) throws TimesheetException {
		String s = ""; int j=0;
        Optional<List<Integer>> p = profiles.entrySet().stream()
				.filter(y-> y.getKey().equals(profileID)).map(x-> x.getValue()).findAny();
        if(!p.isPresent()){throw new TimesheetException();};
        for (Integer i : p.get()){
			s += Giorno.values()[j] + ": " + i;
			if(!(j==6)) s+= "; ";
			j++;
		}
        return s;
	}
	
	public String createWorker(String name, String surname, String profileID) throws TimesheetException {
		Optional<String> p = profiles.keySet().stream().filter(x-> x.equals(profileID)).findAny();
		if(!p.isPresent()) throw new TimesheetException();
		Worker w = new Worker(name, surname, Integer.toString(workerId++), profileID);
//				Arrays.stream(this.getProfile(profileID).split("[A-Za-z]:")).toArray(Integer[]::new));
		workers.add(w);
		return w.getID();
	}
	
	public String getWorker(String workerID) throws TimesheetException {
        Optional<Worker> w= workers.stream()
				.filter(a-> workerID.equals(a.getID())).findAny();
        if(!w.isPresent()) throw new TimesheetException();
        return w.get().toString() + " (" + this.getProfile(w.get().hourprofile) + ")";
	}
	
	// R4
	public void addReport(String workerID, String projectName, String activityName, int day, int workedHours) throws TimesheetException {
		int i;
		try{ i = this.getWeekDay(day);} catch (TimesheetException t){ throw t;}

		Optional<Activity> a = activities.stream().filter(x-> activityName.equals(x.toString())&& x.getProject().name.equals(projectName))
				.filter(Activity::isOpen).findAny();
		Optional<Worker> w = workers.stream().filter(y-> y.getID().equals(workerID))
				.findAny();

		if(!a.isPresent()||!w.isPresent() || this.isHoliday(day) || day < 1 || workedHours < 1
				|| profiles.entrySet().stream().filter(z-> z.getKey().equals(w.get().hourprofile)).findAny().get().getValue().get(i) > workedHours)
		{throw new TimesheetException();}
		reports.add(new Report(w.get(), a.get().getProject(), a.get(), day, workedHours));
	}
	public int getProjectHours(String projectName) throws TimesheetException {
		if(!projects.stream().anyMatch(x-> x.name.equals(projectName))){throw new TimesheetException();}
		return reports.stream()
				.filter(y -> projectName.equals(y.project.name)).collect(Collectors.summingInt(x-> x.workedHours));
	}
	
	public int getWorkedHoursPerDay(String workerID, int day) throws TimesheetException {
		Optional<Worker> w = workers.stream().filter(x-> x.getID().equals(workerID)).findAny();
		if(day <1 || !w.isPresent()){throw new TimesheetException();}
		return reports.stream().filter(y -> y.day == day).mapToInt(z -> z.workedHours).sum();
	}
	
	// R5
	public Map<String, Integer> countActivitiesPerWorker() {
        return reports.stream().collect(Collectors.groupingBy(Report::getWorker,TreeMap::new, Collectors.reducing(0, e-> 1, Integer::sum)));
	} //TODO: not complete
	//
	
	public Map<String, Integer> getRemainingHoursPerProject() {
        Map<String,List<Report>> l = reports.stream().collect(Collectors.groupingBy(x-> x.activity.getProject().name,Collectors.toList()));
		/*reports.stream().collect(Collectors.groupingBy(x-> x.activity.getProject().name,TreeMap::new,
				Collectors.reducing(y -> y.activity.getProject().maxhours,z-> -z.workedHours,Integer::sum)));
//				Collector.of(0,(a, b) -> ),)*/
		return null;
	}
	
	public Map<String, Map<String, Integer>> getHoursPerActivityPerProject() {
        return reports.stream().collect(
        		Collectors.groupingBy(x-> x.activity.getProject().name,TreeMap::new,
						Collectors.groupingBy(y->y.activity.toString(),TreeMap::new,
								Collectors.summingInt(z-> z.workedHours))));
	}
}
