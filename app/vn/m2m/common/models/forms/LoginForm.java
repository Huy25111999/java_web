package vn.m2m.common.models.forms;

public class LoginForm {
    private String username;
    private String password;
    private String passwordRepeat;
    private boolean isRegisterForm=false;

    private String recaptcharespone;

    public LoginForm(){

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

    public boolean isRegisterForm() {
        return isRegisterForm;
    }

    public void setIsRegisterForm(boolean isRegisterForm) {
        this.isRegisterForm = isRegisterForm;
    }

    public String getRecaptcharespone() {
        return recaptcharespone;
    }

    public void setRecaptcharespone(String recaptcharespone) {
        this.recaptcharespone = recaptcharespone;
    }
}
