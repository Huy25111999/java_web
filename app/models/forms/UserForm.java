package models.forms;

public class UserForm extends AbstractFormModel {
    private String groupId;
    private String newGroupId;
    private String id;
    private String listUsrid;
    private String username;
    private String email;
    private String password;
    private String repeatpassword;
    private int role;
    private int roleVessel;
    private String phone;
    private String med;
    private boolean active;
    private String lastModified;

    private String apiKey;
    private String secretKey;





    public UserForm() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatpassword() {
        return repeatpassword;
    }

    public void setRepeatpassword(String repeatpassword) {
        this.repeatpassword = repeatpassword;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getListUsrid() {
        return listUsrid;
    }

    public void setListUsrid(String listUsrid) {
        this.listUsrid = listUsrid;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getNewGroupId() {
        return newGroupId;
    }

    public void setNewGroupId(String newGroupId) {
        this.newGroupId = newGroupId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRoleVessel() {
        return roleVessel;
    }

    public void setRoleVessel(int roleVessel) {
        this.roleVessel = roleVessel;
    }

    public String getMed() {
        return med;
    }

    public void setMed(String med) {
        this.med = med;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
