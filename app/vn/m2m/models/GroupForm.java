package vn.m2m.models;

import models.forms.AbstractFormModel;
import play.data.validation.ValidationError;
import play.mvc.Http;
import vn.m2m.utils.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static play.mvc.Controller.request;

/**
 * Created by minhnt31 on 20/11/2017.
 */
public class GroupForm extends AbstractFormModel {
    private String id;
    private String name;
    private String[] path;
    private String address;
    private String phone;
    private String email;
    private String orgId;
    private Date createDate;
    private Date lastModified;
    private boolean active = true;
    private String med;
    private boolean groupTest;
    private String groupId;
    private String newGroupId;

    public GroupForm() {
        Date now = DateUtil.now();
        this.createDate = now;
        this.lastModified = now;
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        Http.MultipartFormData data = request().body().asMultipartFormData();
        return errors.isEmpty() ? null : errors;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getPath() {
        return path;
    }

    public void setPath(String[] path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getMed() {
        return med;
    }

    public void setMed(String med) {
        this.med = med;
    }

    public boolean isGroupTest() {
        return groupTest;
    }

    public void setGroupTest(boolean groupTest) {
        this.groupTest = groupTest;
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
}
