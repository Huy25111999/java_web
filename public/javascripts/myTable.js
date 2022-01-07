
$(document).ready(function () {
    oTable = $('#myTable').DataTable({
        "pagingType": "full_numbers",
        "lengthMenu": [
            [10,20, 50, -1],
            ["10","20", "50", "ALL"]
        ],
        language: {
            search: "_INPUT_",
            searchPlaceholder: "Search records",
        },
        bSort: true,
        "order": [[ 0, "desc" ]],
        responsive: true
    });
    // oTable.on('order.dt search.dt', function () {
    //     oTable.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
    //         cell.innerHTML = i + 1;
    //     });
    // }).draw(); // them stt vao table

    oTable = $('#tableStatitical1').DataTable({
        "pagingType": "full_numbers",
        "lengthMenu": [
            [10,20, 50, -1],
            ["10","20", "50", "ALL"]
        ],
        language: {
            search: "_INPUT_",
            searchPlaceholder: "Search records",
        },
        bSort: true,
        "order": [[ 2, "desc" ]],
        responsive: true
    });
    oTable = $('#tableStatitical2').DataTable({
        "pagingType": "full_numbers",
        "lengthMenu": [
            [10,20, 50, -1],
            ["10","20", "50", "ALL"]
        ],
        language: {
            search: "_INPUT_",
            searchPlaceholder: "Search records",
        },
        bSort: true,
        "order": [[ 2, "desc" ]],
        responsive: true
    });
});