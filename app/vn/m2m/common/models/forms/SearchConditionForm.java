package vn.m2m.common.models.forms;

public class SearchConditionForm {      //ho tro view
    private String fieldName;
    private String fieldType; // Date, Int, String
    private String fieldValue;
    private String compQueryOp; // eq, ne, gt, lt, gte , lte, like, nlike

    public SearchConditionForm() {
        this.fieldName = "";
        this.fieldType = "";
        this.fieldValue = "";
        this.compQueryOp = "";
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getCompQueryOp() {
        return compQueryOp;
    }

    public void setCompQueryOp(String compQueryOp) {
        this.compQueryOp = compQueryOp;
    }

}
