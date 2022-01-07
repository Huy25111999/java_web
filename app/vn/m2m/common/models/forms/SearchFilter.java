package vn.m2m.common.models.forms;

import java.util.ArrayList;
import java.util.List;

public class SearchFilter { //ho tro DAO
    private List<SearchCondition> conditionList=new ArrayList<SearchCondition>();
    private String sortFieldName;
    private boolean isDesc=true;
    private int page=1;
    private int pageSize=15;

    public List<SearchCondition> getConditionList() {
        return conditionList;
    }

    public void setConditionList(List<SearchCondition> conditionList) {
        this.conditionList = conditionList;
    }

    public String getSortFieldName() {
        return sortFieldName;
    }

    public void setSortFieldName(String sortFieldName) {
        this.sortFieldName = sortFieldName;
    }

    public boolean isDesc() {
        return isDesc;
    }

    public void setIsDesc(boolean isDesc) {
        this.isDesc = isDesc;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}

