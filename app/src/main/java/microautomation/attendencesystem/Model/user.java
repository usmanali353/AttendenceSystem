package microautomation.attendencesystem.Model;

public class user {
    public user(String name, String password, String id, String email, String role,String image_url) {
        this.name = name;
        this.password = password;
        this.id = id;
        this.email = email;
        this.role = role;
        this.image_url=image_url;
    }
    public user(){

    }

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

    String email;
    String role;

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


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
