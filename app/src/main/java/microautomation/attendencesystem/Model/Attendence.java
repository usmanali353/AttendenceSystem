package microautomation.attendencesystem.Model;

public class Attendence {
    String date,subject;
    Boolean attendence;

    public Attendence() {
    }

    public Attendence(String date, String subject, Boolean attendence) {
        this.date = date;
        this.subject = subject;
        this.attendence = attendence;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Boolean getAttendence() {
        return attendence;
    }

    public void setAttendence(Boolean attendence) {
        this.attendence = attendence;
    }
}
