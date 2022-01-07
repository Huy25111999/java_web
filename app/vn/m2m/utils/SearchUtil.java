package vn.m2m.utils;

import org.springframework.util.StringUtils;
import vn.m2m.common.models.forms.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchUtil {
    public static final String TIME_DATA_SEARCH = "dd/MM/yyyy";

    public static final String SEARCH_STRING_TYPE = "String";
    public static final String SEARCH_INT_TYPE = "Int";
    public static final String SEARCH_BOOLEAN_TYPE = "Boolean";
    public static final String SEARCH_DATE_TYPE = "Date";
    public static final String SEARCH_ACTION_SERVERLOG_TYPE = "Action";
    public static final String SEARCH_DOUBLE_TYPE = "Double";
    public static final String SEARCH_OBJECT_TYPE = "ObjName";

    public static final String SEARCH_EQUAL_OP_STRING = "=";
    public static final String SEARCH_STRONG_EQUAL_OP_STRING = "==";
    public static final String SEARCH_NOT_EQUAL_OP_STRING = "<>";
    public static final String SEARCH_GTE_OP_STRING = ">=";
    public static final String SEARCH_LTE_OP_STRING = "<=";
    public static final String SEARCH_BETWEEN_OP_STRING = "between";
    public static final String SEARCH_ANY_OP_STRING = "any";

    public static final String SEARCH_BOOLEAN_TRUE_VALUE_STRING = "true";
    public static final String SEARCH_BOOLEAN_FALSE_VALUE_STRING = "false";
    public static final String SEARCH_STRING_EMPTY_STRING = "";

    public static final int PAGE_SIZE_DEFAULD = 15;


    public static SearchFilter InitConditionListDataPOST(List<String> searchArray, SearchFilterForm searchFilterForm){

        List<SearchConditionForm> searchConditionFormList = searchFilterForm.getConditionList();
        List<String> filter = searchFilterForm.getFilter();
        String sortFieldName = searchFilterForm.getSortFieldName();
        boolean isDesc = searchFilterForm.isDesc();
        Integer page = searchFilterForm.getPage();
        Integer pageSize = searchFilterForm.getPageSize();
        boolean isResetPage = searchFilterForm.isResetPage();
        List<String> fieldSearchList = new ArrayList<String>();
        if(filter !=null) {
            if (filter.size() != 0) {
                for (String s : filter) {
                    if(searchArray.contains(s)){
                        fieldSearchList.add(s);
                    }
                }
            }
        }
        SearchFilter searchFilter = new SearchFilter();
        if(isResetPage) {
            page=1;
        }
        searchFilter.setPage(page);
        searchFilter.setPageSize(pageSize);

        List<SearchCondition> searchConditionList = new ArrayList<SearchCondition>();

        searchConditionList = ProcessSearchConditionString(fieldSearchList,searchConditionFormList); // processing condition string
        /*for(int i = 0;i < searchConditionList.size(); i++){
            logger.debug("searchConditionList getFieldName {}", searchConditionList.get(i).getFieldName());
            logger.debug("searchConditionList getCompQueryOp {}", searchConditionList.get(i).getCompQueryOp());
            logger.debug("searchConditionList getFieldValue {}", searchConditionList.get(i).getFieldValue());
        }*/
        searchFilter.setConditionList(searchConditionList);
        searchFilter.setSortFieldName(sortFieldName);
        searchFilter.setIsDesc(isDesc);
        return searchFilter;
    }

    public static SearchObjectData PagingDataAfterSearchPOST(SearchFilter searchFilter, SearchFilterForm searchFilterForm, long countTotal){
        SearchObjectData searchObjectData = new SearchObjectData();
        int pageSize = searchFilter.getPageSize();
        long pageTotal = 0;
        if(countTotal%pageSize !=0) {
            pageTotal = countTotal/pageSize +1;
        }else {
            pageTotal = countTotal/pageSize;
        }
        searchFilterForm.setPage(searchFilter.getPage());
        searchFilterForm.setTotalPage(pageTotal);
        searchFilterForm.setTotalResult(countTotal);
        searchFilterForm.setIsResetCondition(false);
        searchObjectData.setSearchFilterForm(searchFilterForm);
        return searchObjectData;

    }
    public static SearchObjectData PagingDataAfterSearch(SearchFilter searchFilter, long countTsmsl){

        SearchFilterForm searchFilterForm = new SearchFilterForm();
        SearchConditionForm searchConditionForm = new SearchConditionForm();
        List<SearchConditionForm> searchConditionFormList = new ArrayList<SearchConditionForm>();
        searchConditionFormList.add(searchConditionForm);
        searchFilterForm.setConditionList(searchConditionFormList);
        String sortFieldName = searchFilter.getSortFieldName();
        if(!StringUtils.isEmpty(sortFieldName)){
            searchFilterForm.setSortFieldName(sortFieldName);
        }
        searchFilterForm.setIsDesc(true);
        List<String> filterTemp = new ArrayList<String>();
        searchFilterForm.setFilter(filterTemp);
        long pageTsmsl = 0;
        if(countTsmsl%PAGE_SIZE_DEFAULD !=0) {
            pageTsmsl = countTsmsl/PAGE_SIZE_DEFAULD +1;
        }else {
            pageTsmsl = countTsmsl/PAGE_SIZE_DEFAULD;
        }
        searchFilterForm.setTotalPage(pageTsmsl);
        searchFilterForm.setTotalResult(countTsmsl);
        searchFilterForm.setIsResetCondition(true);
        SearchObjectData searchObjectData = new SearchObjectData();
        searchObjectData.setSearchFilterForm(searchFilterForm);
        return searchObjectData;
    }

    public static SearchFilter InitConditionListData(List<String> searchArray , String sortField){
        String sortFieldName ="";
        if(searchArray.contains(sortField)){
            sortFieldName = sortField;
        }
        SearchFilter searchFilter = new SearchFilter();
        List<SearchCondition> searchConditionList = new ArrayList<SearchCondition>();
        searchFilter.setConditionList(searchConditionList);
        if(!StringUtils.isEmpty(sortFieldName)){
            searchFilter.setSortFieldName(sortFieldName);
        }
        searchFilter.setIsDesc(true);
        return searchFilter;

    }

    public static List<SearchCondition> ProcessSearchConditionString(List<String> fieldSearchList , List<SearchConditionForm> searchConditionFormList){

        List<SearchCondition> searchConditionList = new ArrayList<SearchCondition>();
        Integer conditionItems = 0;

        while( conditionItems < searchConditionFormList.size()) {
            //System.out.println("conditionItems"+conditionItems);
            SearchConditionForm searchConditionForm = searchConditionFormList.get(conditionItems);
            if(!fieldSearchList.contains(searchConditionForm.getFieldName())){
                conditionItems++;
                continue;
            }
            switch (searchConditionForm.getFieldType()){
                case SEARCH_STRING_TYPE :
                    if(StringUtils.isEmpty(searchConditionForm.getFieldValue().trim())){
                        conditionItems++;
                        continue;
                    }
                    SearchCondition conditionString = new SearchCondition();
                    conditionString.setFieldName(searchConditionForm.getFieldName());
                    if (searchConditionForm.getCompQueryOp().equals(SEARCH_STRONG_EQUAL_OP_STRING)) {
                        conditionString.setCompQueryOp(SearchCondition.CompQueryOp.eq);
                    }else if (searchConditionForm.getCompQueryOp().equals(SEARCH_EQUAL_OP_STRING)) {
                        conditionString.setCompQueryOp(SearchCondition.CompQueryOp.like);
                    } else if (searchConditionForm.getCompQueryOp().equals(SEARCH_NOT_EQUAL_OP_STRING)) {
                        conditionString.setCompQueryOp(SearchCondition.CompQueryOp.nlike);
                    }
                    conditionString.setFieldValue(searchConditionForm.getFieldValue());
                    searchConditionList.add(conditionString);
                    conditionItems++;
                    break;
                case SEARCH_DATE_TYPE :
                    String dateHourfromString = searchConditionForm.getFieldValue() ;
                    conditionItems++;
                    searchConditionForm = searchConditionFormList.get(conditionItems);
                    String dateHourtoString  = searchConditionForm.getFieldValue();
                    conditionItems++;

                    if (StringUtils.isEmpty(dateHourfromString) && StringUtils.isEmpty(dateHourtoString)  ) {
//                    continue;
                    }else if( !StringUtils.isEmpty(dateHourfromString) && StringUtils.isEmpty(dateHourtoString) ){
                        Date datefrom = (DateUtil.convertStringtoDate(dateHourfromString, TIME_DATA_SEARCH));
                        SearchCondition conditionDate1 = new SearchCondition();
                        conditionDate1.setFieldName(searchConditionForm.getFieldName());
                        conditionDate1.setCompQueryOp(SearchCondition.CompQueryOp.gte); //greater than begin time
                        conditionDate1.setFieldValue(datefrom);
                        searchConditionList.add(conditionDate1);
                    }else if( StringUtils.isEmpty(dateHourfromString) && !StringUtils.isEmpty(dateHourtoString) ) {
                        Date dateto = (DateUtil.convertStringtoDate(dateHourtoString, TIME_DATA_SEARCH));
                        SearchCondition conditionDate2 = new SearchCondition();
                        conditionDate2.setFieldName(searchConditionForm.getFieldName());
                        conditionDate2.setCompQueryOp(SearchCondition.CompQueryOp.lte); //greater than begin time
                        conditionDate2.setFieldValue(dateto);
                        searchConditionList.add(conditionDate2);
                    }else if( !StringUtils.isEmpty(dateHourfromString) && !StringUtils.isEmpty(dateHourtoString) ) {
                        Date datefrom = (DateUtil.convertStringtoDate(dateHourfromString, TIME_DATA_SEARCH));
                        Date dateto = (DateUtil.convertStringtoDate(dateHourtoString, TIME_DATA_SEARCH));
                        SearchCondition conditionDate3 = new SearchCondition();
                        conditionDate3.setFieldName(searchConditionForm.getFieldName());
                        conditionDate3.setCompQueryOp(SearchCondition.CompQueryOp.gte); //greater than begin time
                        conditionDate3.setFieldValue(datefrom);
                        conditionDate3.setMulCondition(2);
                        searchConditionList.add(conditionDate3);
                        SearchCondition conditionTo = new SearchCondition();
                        conditionTo.setFieldName(searchConditionForm.getFieldName());
                        conditionTo.setCompQueryOp(SearchCondition.CompQueryOp.lte); // less than end time
                        conditionTo.setFieldValue(dateto);
                        searchConditionList.add(conditionTo);
                    }
                    break;
                case SEARCH_INT_TYPE :
                    String valueString = searchConditionForm.getFieldValue();
                    if(!StringUtils.isEmpty(valueString)){
                        try{
                            Integer valueInt = ParseUtil.parseInt(valueString);

                            if (searchConditionForm.getCompQueryOp().equals(SEARCH_EQUAL_OP_STRING)) {
                                SearchCondition condition1 = new SearchCondition();
                                condition1.setFieldName(searchConditionForm.getFieldName());
                                condition1.setCompQueryOp(SearchCondition.CompQueryOp.eq);
                                condition1.setFieldValue(valueInt);
                                searchConditionList.add(condition1);
                                conditionItems+=2;
                            } else if (searchConditionForm.getCompQueryOp().equals(SEARCH_NOT_EQUAL_OP_STRING)) {
                                SearchCondition condition2 = new SearchCondition();
                                condition2.setFieldName(searchConditionForm.getFieldName());
                                condition2.setCompQueryOp(SearchCondition.CompQueryOp.ne);
                                condition2.setFieldValue(valueInt);
                                searchConditionList.add(condition2);
                                conditionItems+=2;
                            } else if (searchConditionForm.getCompQueryOp().equals(SEARCH_GTE_OP_STRING)) {
                                SearchCondition condition3 = new SearchCondition();
                                condition3.setFieldName(searchConditionForm.getFieldName());
                                condition3.setCompQueryOp(SearchCondition.CompQueryOp.gte);
                                condition3.setFieldValue(valueInt);
                                searchConditionList.add(condition3);
                                conditionItems+=2;
                            } else if (searchConditionForm.getCompQueryOp().equals(SEARCH_LTE_OP_STRING)) {
                                SearchCondition condition4 = new SearchCondition();
                                condition4.setFieldName(searchConditionForm.getFieldName());
                                condition4.setCompQueryOp(SearchCondition.CompQueryOp.lte);
                                condition4.setFieldValue(valueInt);
                                searchConditionList.add(condition4);
                                conditionItems+=2;
                            } else if (searchConditionForm.getCompQueryOp().equals(SEARCH_BETWEEN_OP_STRING)) {

                                SearchCondition condition5 = new SearchCondition();
                                condition5.setFieldName(searchConditionForm.getFieldName());
                                condition5.setCompQueryOp(SearchCondition.CompQueryOp.gte); //greater than begin time
                                condition5.setFieldValue(valueInt);
                                condition5.setMulCondition(2);
                                searchConditionList.add(condition5);
                                conditionItems++;
                                searchConditionForm = searchConditionFormList.get(conditionItems);
                                SearchCondition conditionTo = new SearchCondition();
                                conditionTo.setFieldName(searchConditionForm.getFieldName());
                                conditionTo.setCompQueryOp(SearchCondition.CompQueryOp.lte); // less than end time

                                String valueStringNext = searchConditionForm.getFieldValue();
                                if(!StringUtils.isEmpty(valueStringNext)){
                                    try{
                                        Integer valueIntNext = ParseUtil.parseInt(valueStringNext);
                                        conditionTo.setFieldValue(valueIntNext);
                                        searchConditionList.add(conditionTo);
                                        conditionItems++;
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }else {
                                    conditionTo.setFieldValue(valueInt);
                                    searchConditionList.add(conditionTo);
                                    conditionItems++;
                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }else {
                        conditionItems+=2;
                    }
                    break;
                case SEARCH_BOOLEAN_TYPE :
                    if(searchConditionForm.getFieldValue().equals(SEARCH_ANY_OP_STRING)) {
                        conditionItems++;
                        continue;
                    }
                    SearchCondition conditionBoolean = new SearchCondition();
                    conditionBoolean.setFieldName(searchConditionForm.getFieldName());
                    conditionBoolean.setCompQueryOp(SearchCondition.CompQueryOp.eq);
                    if (searchConditionForm.getFieldValue().equals(SEARCH_BOOLEAN_TRUE_VALUE_STRING)) {
                        conditionBoolean.setFieldValue(true);
                    } else if (searchConditionForm.getFieldValue().equals(SEARCH_BOOLEAN_FALSE_VALUE_STRING)) {
                        conditionBoolean.setFieldValue(false);
                    }
                    searchConditionList.add(conditionBoolean);
                    conditionItems++;
                    break;

                case SEARCH_OBJECT_TYPE :
                    if(searchConditionForm.getFieldValue().equals(SEARCH_ANY_OP_STRING)) {
                        conditionItems++;
                        continue;
                    }
                    String valueObjectString = searchConditionForm.getFieldValue();
                    Integer valueObjectInt = ParseUtil.parseInt(valueObjectString);
                    SearchCondition conditionObject = new SearchCondition();
                    conditionObject.setFieldName(searchConditionForm.getFieldName());
                    conditionObject.setCompQueryOp(SearchCondition.CompQueryOp.eq);
                    conditionObject.setFieldValue(valueObjectInt);
                    searchConditionList.add(conditionObject);
                    conditionItems++;
                    break;

                default :
                    conditionItems++;
                    break;
            }

        }
        return searchConditionList;
    }
}
