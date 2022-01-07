

var current_array_list=[];
$().ready(function(){

});
function InitValue(){

    for(i=0; i<array_list.length ; i++){
        var element = $('#' + array_list[i]);
        if(current_array_list !=null){
            //console.log(current_array_list.indexOf(array_list[i]));
            if(current_array_list.indexOf(array_list[i]) != -1){
                if( element.is(':hidden')) {
                    element.show();
                }
            }else{
                element.hideAndReset();
            }
            //console.log(current_array_list.indexOf( array_list[i]));
        }else{
            element.hideAndReset();
        }
    }

    //current_array_list=[];

}

//Add filter event handler
function  ChangeFilterValue(value){
    console.log("ChangeFilterValue");
    // console.log("value: "+value);

    var element = $('#' + value);
    var indexOfField =current_array_list.indexOf(value);
    // console.log( "current_array_list: "+current_array_list);
    // console.log( "indexOfField: "+indexOfField);

    if( indexOfField != -1){
        current_array_list.splice(indexOfField,1);
        element.hide();
    }else{
        current_array_list.push(value);
        element.show();
    }

    $('#ChooseFilter').val(current_array_list).change();
    // console.log( $('#ChooseFilter').val());
}

function  onAdvancedSearch(isChecked){
    console.log('onAdvancedSearch');
    if (isChecked){
        $('.advancedSearchFrom').show();
    }else {
        $('.advancedSearchFrom').hide();
    }
}

function showBetween(rightId,isshow) {
    // console.log("showBetween "+rightId);
    if (isshow){
        $("#"+rightId).show();
    }else{
        $("#"+rightId).hide();
    }
}



//Click on tag a to hide field div
// $('.filedHide').click(function() {
//
//     var searchFieldId =(($(this).parent().parent()).attr('id'));
//     console.log($(this).parent().parent());
//     console.log(($(this).parent().parent()).attr('id'));
//     var indexOfField =current_array_list.indexOf(searchFieldId);
//     //console.log(current_array_list);
//     if( indexOfField !=-1){
//         current_array_list.splice(indexOfField,1);
//     }
//     //console.log(current_array_list);
//     //$('#ChooseFilter').val(current_array_list).change();
//     $(this).parent().parent().hideAndReset();
//     $('#ChooseFilter').val(current_array_list).change();
//     //console.log( $('#ChooseFilter').val());
// });

//Display date to div when option is between
//$('.DateOption').change(function() {
//    if($(this).val() == "between") {
//       if( ($(this).closest('.search-field').find('.search-field-date-to')).is(':hidden')) {
//            $(this).closest('.search-field').find('.search-field-date-to').show();
//       }
//    }else{
//        if( !($(this).closest('.search-field').find('.search-field-date-to')).is(':hidden')) {
//            $(this).closest('.search-field').find('.search-field-date-to').hideAndReset();
//        }
//    }
//});

//Awesome function bind hide event to customer event
(function($) {

    $.fn.resetValue = function() {
        this.each( function() {
            $(this).find('.conditionTextValue').val('');
            $(this).find('.conditionIntValue').val('');
            $(this).find('.conditionDoubleValue').val('');
            $(this).find('.conditionBooleanValue').val("any").change();
            $(this).find('.conditionHourValue').val('00:').change();
            $(this).find('.conditionMinuteValue').val('00').change();
            //$(this).find('.conditionDateValue').val(nowString);
            $(this).find('.conditionDateValue').val('');
            $(this).find('.conditionOptionValue').val("=").change();
            $(this).find('.conditionDateOptionValue').val("between").change();
            $(this).find('.conditionObjNameServerLogValue').val("any").change();
            $(this).find('.conditionActionServerLogValue').val("any").change();
        });
    };

    $.fn.hideAndReset = function() {
        this.each( function() {
            $(this).resetValue();
            $(this).hide();
        });
    };
}(jQuery));
//Awesome function bind hide event to customer event- end

function hidePopupReloadPage() {
    window.location.reload();
}

