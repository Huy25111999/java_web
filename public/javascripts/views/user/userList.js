function submitAddUser() {
    var detail = new FormData();
    detail.username = $("#usernameNew").val();
    detail.password = $("#password").val();
    detail.passwordRepeat = $("#passwordRepeat").val();
    detail.role = $("#roleNew").val();
    console.log(detail);

    StartProcess();
    var r = jsRoutes.controllers.UserController.addUser();
    $.ajax({
        url: r.url,
        data: JSON.stringify(detail),
        dataType: "json",
        type: r.type,
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            console.log(data);
            if(data.success){
                showNotification('top','center','success',data.resultString,'notifications');
                setTimeout(function () {
                    window.location.reload();
                },1000);
            }else{
                showNotification('top','center','danger',data.errorString,'notifications');
            }
        },
        error: function (xhr) {
            console.log(xhr);
            showNotification('top','center','danger',Messages("connection.failed"),'notifications');
        },
        complete: function (jqXHR, textStatus) {
            FinishProcess(); //Stop display loading...
        }
    })
}

function editUserJs(username) {
    console.log(username);
    $("#usernameEdit").val(username);
}

function setdelid(id) {
    $("#idDelete").val(id);
}

function exportToExcel() {
    // StartProcess();
    var r = jsRoutes.controllers.UserController.exportUserList();
    $.ajax({
        type: r.type,
        url: r.url,
        data: $('#advanced-search-bar #search-filter-body #search-filter-form').serialize(),
        success: function (data) {
            // console.log(data);
            if(data && data.length > 0){
                createTable(data);
                outExcel("Export User List");
            } else {
                showNotification('top', 'center', 'danger', Messages("Export false"), 'pe-7s-close-circle');
            }

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            showNotification('top', 'center', 'danger', Messages("request.error"), 'pe-7s-close-circle');
        },
        complete: function (jqXHR, textStatus) {
            // FinishProcess(); //Stop display loading...
        }
    });
}

function createTable(data) {
    var columnTitles = ["STT",
        Messages("user")
    ];

    var table = document.createElement("table");
    table.setAttribute("class", "table");
    table.setAttribute("id", idExportTable);
    document.body.appendChild(table);

    // create table head
    var tableThead = document.createElement("thead");
    var tableTheadTr = document.createElement("tr");

    for (var i = 0; i < columnTitles.length; i++) {
        var th = document.createElement("th");
        var value = document.createTextNode(columnTitles[i]);
        th.appendChild(value);
        tableTheadTr.appendChild(th);
    }
    tableThead.appendChild(tableTheadTr);
    table.appendChild(tableThead);

    // create table rows
    var tableBody = document.createElement("tbody");
    for (var i = 0; i < data.length; i++) {
        var tableTbodyTr = document.createElement("tr");
        for (var col = 0; col < columnTitles.length; col++) {
            var td = document.createElement("td");
            var valueTextNode = '';
            switch (col) {
                case 0:
                    valueTextNode = i+1;
                    break;
                case 1:
                    valueTextNode = "'"+data[i].username;
                    break;
            }
            var valueTd = document.createTextNode(valueTextNode);
            td.appendChild(valueTd);
            tableTbodyTr.appendChild(td);
        }
        tableBody.appendChild(tableTbodyTr);
    }
    table.appendChild(tableBody);
}
const idExportTable = 'ExportTableUser';
function outExcel(name) {
    var tableEx = $('#' + idExportTable);
    var table2excel = new Table2Excel({defaultFileName: name, tableNameDataAttribute: "myTableExport"});
    table2excel.export(tableEx);
    $('#' + idExportTable).remove();
}