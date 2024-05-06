package models;
import com.google.gson.Gson;

public class Patient {
    private Integer id;
    private String email;
    private String roles;
    private String password;
    private String firstname;
    private String lastname;
    private String sexe;
    private Integer age;
    private String number;
    private String img_path;
    private String address;
    private Boolean is_verified;
    private String reset_token;


    public Patient(Integer id, String email, String roles, String password, String firstname, String lastname,
                   String sexe, Integer age, String number, String img_path, String address, Boolean is_verified,
                   String resetToken) {
        this.id = id;
        this.email = email;
        this.roles = roles;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.sexe = sexe;
        this.age = age;
        this.number = number;
        this.img_path = img_path;
        this.address = address;
        this.is_verified = is_verified;
        this.reset_token = resetToken;
    }

    public Patient() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(Boolean is_verified) {
        this.is_verified = is_verified;
    }

    public String getReset_token() {
        return reset_token;
    }

    public void setResetToken(String resetToken) {
        this.reset_token = resetToken;
    }

    public boolean hasRole(String role) {
        Gson gson = new Gson();
        String[] rolesArray = gson.fromJson(roles, String[].class);
        for (String r : rolesArray) {
            if (r.equals(role)) {
                return true;
            }
        }
        return false;
    }
}
