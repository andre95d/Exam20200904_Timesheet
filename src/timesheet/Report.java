package timesheet;

public class Report {
    public Worker worker;
    public Project project;
    public Activity activity;
    public int day;
    public int workedHours;

    public Report(Worker worker, Project project, Activity activity, int day, int workedHours) {
        this.worker=worker;
        this.project=project;
        this.activity=activity;
        this.day=day;
        this.workedHours=workedHours;
    }
    public String getWorker(){return this.worker.getID();}
    public Integer projectMaxHours(){ return new Integer(this.activity.getProject().getHours()); }
}
