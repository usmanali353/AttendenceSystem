package microautomation.attendencesystem.Model;

import java.util.ArrayList;
import java.util.List;

public class Students {
    String name,password,roll_no;
    List<String> subjects;

    public Students() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoll_no() {
        return roll_no;
    }

    public void setRoll_no(String roll_no) {
        this.roll_no = roll_no;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public Students(String name, String password, String roll_no, List<String> subjects) {
        this.name = name;
        this.password = password;
        this.roll_no = roll_no;
        this.subjects = subjects;
    }
}
