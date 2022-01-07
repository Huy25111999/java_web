package vn.m2m.common.models.forms;

import java.util.List;

public class SearchObjectData {
    protected List<Object> objectList;
    private SearchFilterForm searchFilterForm;

    public List<Object> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<Object> objectList) {
        this.objectList = objectList;
    }

    public SearchFilterForm getSearchFilterForm() {
        return searchFilterForm;
    }

    public void setSearchFilterForm(SearchFilterForm searchFilterForm) {
        this.searchFilterForm = searchFilterForm;
    }
}
