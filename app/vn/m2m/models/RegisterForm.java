package vn.m2m.models;

/**
 * Created by giangbb on 26/08/2015.
 */
public class RegisterForm {
    private String username;
    private String password;
    private String passwordRepeat;
    private boolean isRegisterForm=false;
    private String maCapTren;
    private String email;

    private String recaptcharespone;

    public RegisterForm(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    public void setRegisterForm(boolean registerForm) {
        isRegisterForm = registerForm;
    }

    public boolean isRegisterForm() {
        return isRegisterForm;
    }

    public String getRecaptcharespone() {
        return recaptcharespone;
    }

    public void setRecaptcharespone(String recaptcharespone) {
        this.recaptcharespone = recaptcharespone;
    }

    public String getMaCapTren() {
        return maCapTren;
    }

    public void setMaCapTren(String maCapTren) {
        this.maCapTren = maCapTren;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
