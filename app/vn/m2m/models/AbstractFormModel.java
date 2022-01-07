package vn.m2m.models;

import play.mvc.Http;

public class AbstractFormModel {
    protected Http.MultipartFormData.FilePart fileData;
    protected String contentType;
    protected String fileName;
    protected String fileClientPath;
    protected String fileServerPath;
    private String searchFilterForm;

    public Http.MultipartFormData.FilePart getFileData() {
        return fileData;
    }

    public void setFileData(Http.MultipartFormData.FilePart fileData) {
        this.fileData = fileData;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileClientPath() {
        return fileClientPath;
    }

    public void setFileClientPath(String fileClientPath) {
        this.fileClientPath = fileClientPath;
    }

    public String getFileServerPath() {
        return fileServerPath;
    }

    public void setFileServerPath(String fileServerPath) {
        this.fileServerPath = fileServerPath;
    }

    public String getSearchFilterForm() {
        return searchFilterForm;
    }

    public void setSearchFilterForm(String searchFilterForm) {
        this.searchFilterForm = searchFilterForm;
    }
}
