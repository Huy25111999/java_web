
document.getElementById("menu-toggle").onclick=function (){
    if (this.classList.contains('move-in'))
    {
        document.getElementById("sidebar-content").style.display="block";
        this.classList.add('move-out');
        this.classList.remove('move-in');
    }else
    {
        document.getElementById("sidebar-content").style.display="none";
        this.classList.add('move-in');
        this.classList.remove('move-out');
    }
}

// main
$(document).ready(function() {
    select();
    marker();
    timepicker();
})

//Time
function timepicker()
{
    flatpickr('#date_timepicker_end',{
        enableTime: true,
        dateFormat: 'd-m-Y,h:m',
    });
    flatpickr('#date_timepicker_start',{
        enableTime: true,
        dateFormat: 'd-m-Y,h:m',
    });

}


function select(){
    var select = "<select id=\"imeiDevice\" data-placeholder=\"Thiết bị\" class=\"single-select select2-hidden-accessible\" style=\"width: 100%\" data-select2-id=\"imeiDevice\" tabindex=\"-1\" aria-hidden=\"true\">";
    select += '<option value="" data-select2-id="4>">Thiết bị</option>';
    $.ajax({
        url:"/getAllImeiDevice" ,
        method: "GET",
        dataType:"json",
        success:function(res)
        {
            var data= res.content.data.data;
            for ( var i=0; i < data.length;i++)
            {
                select += '<option value="'+data[i].entity_id+'" data-select2-id="4">'+data[i].imei+'</option>';
            }
            select += '</select>';
            document.getElementById('selectDevice').innerHTML = select;
            console.log(res.content.data.data);
        },
        err:function (res)

        {
            alert(23)
        }
    })

}
// marker
function marker(){
    mapboxgl.accessToken = 'pk.eyJ1IjoiaHV5c3VwcG9ydCIsImEiOiJjbDB2dDV1d3kxYWF6M2lvMmo0Mml3MWJ2In0.sBwz__9G5aSTCROFnlKrYw';
    const map = new mapboxgl.Map({
        container: 'map',
        style: 'mapbox://styles/mapbox/streets-v11',
        center: [ 103.90462168031246, 20.668780521951536 ],
        zoom: 5,
    });
    const marker1 = new mapboxgl.Marker()
        .setLngLat([-74.5, 40])
        .addTo(map);
}



// Table
var MARKER_TYPE = {
    UNKNOWN: {value: -1, name: "Không xác định"},
    START: {value: 0, name: "Bắt đầu"},
    FINISH: {value: 1, name: "Kết thúc"},
    RUN: {value: 2, name: "Bắt đầu di chuyển", type : "AST"},
    PARK: {value: 3, name: "Dừng", type : "ASP"},
    WIFICELL: {value: 4, name: "Đang di chuyển", type : "WFC"},
    SOS: {value: 5, name: "Cảnh báo SOS", type : "SOS"},
    BL: {value: 6, name: "Pin yếu", type : "BL"}
};

document.getElementById("btn-seach").onclick=function ()
{
    table();
}

function table ()
{
    var timeStart =$("#date_timepicker_start").val();
    var timeEnd =  $("#date_timepicker_end").val();
    var roles = $("#imeiDevice").val();

    var item = new FormData();
    item.id = roles;
    item.imei = $("#imeiDevice option:selected").text();
    item.start_time = timeStart;
    item.end_time = timeEnd;
    console.log(item);
    var table = '<table class="table table-striped table-hover table-condensed table-export" id="table-history-view">\n' +
        '    <thead id="theadExport">\n' +
        '    <tr>\n' +
        '        <th className="text-center">STT</th>\n' +
        '        <th>Trạng thái</th>\n' +
        '        <th >Thời gian</th>\n' +
        '    </tr>\n' +
        '    </thead>\n' +
        '    <tbody>';

    $.ajax({
        url:"/loadLocationHistory",
        method:"POST",
        dataType: 'json',
        data:JSON.stringify(item),
        contentType: "application/json; charset=utf-8",
        success:function (res){
            console.log(res.content);
            var data = res.content ;
            console.log(data.length);

            for ( var i= 0; i<data.length ; i++)
            {

                table +=' <tr>';
                table += '<td>'+(i+1)+'</td>';
                table += '<td>'+Object.values( MARKER_TYPE ).find(t => t.type == data[i].type).name+'</td>';
                table += '<td>'+data[i].logDate+'</td>';
                table +='</tr>';
            }

            table +='</tbody>';
            table += '</table>';
            document.getElementById('table').innerHTML = table;

        },
        error:function (err){
            console.log("error");
        }
    })
}

