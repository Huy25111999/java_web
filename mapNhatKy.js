
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

// Time
var dateClickStart = document.getElementById("date_timepicker_start1");
var dateClickEnd = document.getElementById("date_timepicker_start2");

dateClickStart.onclick=function ()
{
    document.getElementById("datepickersStart").style.display='block';
}
dateClickStart.onmouseleave=function ()
{
    document.getElementById("datepickersStart").style.display='none';
}

dateClickEnd.onclick=function ()
{
    document.getElementById("datepickersEnd").style.display='block';
}
dateClickEnd.onmouseleave=function ()
{
    document.getElementById("datepickersEnd").style.display='none';
}

// main
$(document).ready(function() {
     select();
    marker();
    timepicker();

});
function timepicker()
{
    $("#date_timepicker_start1").datepicker();
    $("#date_timepicker_start2").datepicker();
    $(".datepicker>div").attr('id','datepickersStart');
    document.getElementById("datepickersStart").style.display='none';
    $("#date_timepicker_start2>.datepicker>div").attr('id','datepickersEnd');
    document.getElementById("datepickersEnd").style.display='none';
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
                 select += '<option value="'+data[i].id+'" data-select2-id="4">'+data[i].imei+'</option>';
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
        center: [-74.5, 40],
        zoom: 8,
    });
    const marker1 = new mapboxgl.Marker()
        .setLngLat([-74.5, 40])
        .addTo(map);
}

// Table
document.getElementById("btn-seach").onclick=function ()
{
    var timeStart =$("#date_timepicker_start").val();
    var timeEnd = $("#date_timepicker_end").val();
    var roles = $("#imeiDevice").val();
    console.log(timeStart);
    console.log(timeEnd);
    console.log(roles);

    var item = new FormData();
    item.id=roles;
    item.start_time = timeStart;
    item.end_time = timeEnd;
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
        success:function (res){
            alert(1);
            table +=' <tr></tr>';
            table +='</tbody>';
            table += '</table>';
            document.getElementById('table').innerHTML = table;
        },
        error:function (err){

        }
    })
}

