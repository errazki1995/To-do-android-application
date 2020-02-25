package upem.tasksAnd.start.models;

import java.util.Date;

public class Timer {
    private int timerid;
    private int timerdate;
    private Date startDate;
    private int taskid;
    private Date endDate;
    private int hour;
    private int min;
    private boolean activated;
    private int sec;

    public Timer(int timerid,int taskid, int timerdate, Date startDate, Date endDate, boolean activated,int hour, int min, int sec) {
        this.timerid = timerid;
        this.timerdate = timerdate;
        this.taskid=taskid;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hour = hour;
        this.min = min;
        this.sec = sec;
        this.activated=activated;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public int getTimerid() {
        return timerid;
    }

    public void setTimerid(int timerid) {
        this.timerid = timerid;
    }

    public int getTimerdate() {
        return timerdate;
    }

    public void setTimerdate(int timerdate) {
        this.timerdate = timerdate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getSec() {
        return sec;
    }

    public void setSec(int sec) {
        this.sec = sec;
    }
}
