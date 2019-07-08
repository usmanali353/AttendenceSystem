package microautomation.attendencesystem.Model;

import java.util.ArrayList;
import java.util.List;

public class Students {
    String name;
    String password;
    String id;
    String image_url;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    String email;
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



    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public Students(String name, String password, String id, List<String> subjects,String email,String role,String image_url) {
        this.name = name;
        this.password = password;
        this.id = id;
        this.subjects = subjects;
        this.email=email;
        this.role=role;
        this.image_url=image_url;
    }
}
