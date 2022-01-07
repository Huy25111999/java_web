/**
 * Created by minhtb11 on 13-Nov-20.
 */
$(document).ready(function () {
    oTableNap = $('#myTableNap').DataTable({
        "pagingType": "full_numbers",
        "lengthMenu": [
            [10,20, 50, -1],
            ["10","20", "50", "All"]
        ],
        language: {
            search: "_INPUT_",
            searchPlaceholder: "Search records",
        },
        bSort: true,
        responsive: true
    });
    oTableNap.on('order.dt search.dt', function () {
        oTableNap.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
            cell.innerHTML = i + 1;
        });
    }).draw(); // them stt vao table

    oTableRut = $('#myTableRut').DataTable({
        "pagingType": "full_numbers",
        "lengthMenu": [
            [10,20, 50, -1],
            ["10","20", "50", "All"]
        ],
        language: {
            search: "_INPUT_",
            searchPlaceholder: "Search records",
        },
        bSort: true,
        responsive: true
    });
    oTableRut.on('order.dt search.dt', function () {
        oTableRut.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
            cell.innerHTML = i + 1;
        });
    }).draw(); // them stt vao table
});

function getHistoryNap(id, noidung, status, username) {
    $("#idNap").val(id);
    $("#noiDungNap").val(noidung);
    $("#statusNap").val(status).trigger("change");
    $("#userNap").val(username);
    // document.getElementById('imageHistoryNap').src="/assets/images/avatar/" + filePath;
    // document.getElementById('imageHistoryNap').src="/data/content/image/" + filePath;

    // StartProcess();
    $.ajax({
        type: 'GET',
        dataType: 'json',
        data: JSON.stringify(""),
        contentType: "application/json; charset=utf-8",
        url: '/getHistoryNapId/'+id,
        success: function (data) {
            console.log(data);
            if(data.success){
                document.getElementById('imageHistoryNap').src=data.content.baseImg;
            } else {
                showNotification('top','center','danger',data.resultString,'notifications');
            }
        },
        complete: function (jqXHR, textStatus) {
            // FinishProcess();
        }
    })
}
function submitEditNap() {
    var dataModal = new FormData;
    dataModal.id = $("#idNap").val();
    dataModal.status = $("#statusNap").val();

    // StartProcess();
    var r = jsRoutes.controllers.UserController.editHistoryNap();
    $.ajax({
        type: r.type,
        dataType: 'json',
        data: JSON.stringify(dataModal),
        contentType: "application/json; charset=utf-8",
        url: r.url,
        success: function (data) {
            // console.log(data);
            if(data.success){
                showNotification('top','center','success',data.resultString,'notifications');
                setTimeout(function () {
                    window.location.reload();
                },1500);
            } else {
                showNotification('top','center','danger',data.resultString,'notifications');
            }
        },
        complete: function (jqXHR, textStatus) {
            // FinishProcess();
        }
    })
}

function getHistoryRut(id, noidung, status, username, stk, ctk, bank, money) {
    $("#idRut").val(id);
    $("#noiDungRut").val(noidung);
    $("#statusRut").val(status).trigger("change");
    $("#userRut").val(username);

    $("#stkRut").val(stk + " - " + bank + " - " + ctk);
    $("#tienRut").val(formatter.format(money*0.98));
}
function submitEditRut() {
    var dataModal = new FormData;
    dataModal.id = $("#idRut").val();
    dataModal.status = $("#statusRut").val();

    // StartProcess();
    var r = jsRoutes.controllers.UserController.editHistoryRut();
    $.ajax({
        type: r.type,
        dataType: 'json',
        data: JSON.stringify(dataModal),
        contentType: "application/json; charset=utf-8",
        url: r.url,
        success: function (data) {
            // console.log(data);
            if(data.success){
                showNotification('top','center','success',data.resultString,'notifications');
                setTimeout(function () {
                    window.location.reload();
                },1500);
            } else {
                showNotification('top','center','danger',data.resultString,'notifications');
            }
        },
        complete: function (jqXHR, textStatus) {
            // FinishProcess();
        }
    })
}