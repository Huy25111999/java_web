
var el;
var image;

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
        url:"/getListGpsDevice" ,
        method: "GET",
        dataType:"json",
        success:function(res)
        {
            console.log(res);
            var data= res.initMapMarkerList;

            for ( var i=0; i < data.length;i++)
            {
                select += '<option value="'+data[i].idName+'" data-select2-id="4">'+data[i].imei+'</option>';
            }
            select += '</select>';
            document.getElementById('selectDevice').innerHTML = select;
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
        //   center: [ 103.90462168031246, 20.668780521951536 ],
        center: [105.811197,21.067695 ],
        zoom: 8,
    });
    map.addControl(new mapboxgl.FullscreenControl(),'bottom-right');
    map.addControl(new mapboxgl.NavigationControl(),'bottom-right');

    // const marker1 = new mapboxgl.Marker()
    //     .setLngLat([105.811197,21.067695])
    //     .addTo(map);

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
    //table();
    renderMarker();
}
/*
function table()
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

    var table = '';
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
                table +=' <tr onclick="openModal(),render('+data[i].stt+')">';
                table += '<td>'+(i+1)+'</td>';
                table += '<td>'+Object.values( MARKER_TYPE ).find(t => t.type == data[i].type)?.name+'</td>';
                table += '<td>'+moment.unix(data[i].logDate).format("DD/MM/YYYY HH:mm:ss")+'</td>';
                table +='</tr>';
            }
            document.getElementById('table-map').innerHTML = table;
        },
        error:function (err){
            console.log("error");
        }
    })
}
*/
// Modal
function openModal()
{
    $('#modal').css('display','block');
    //   document.getElementById("modal").style.display='block';
}

//Close modal
$('#btn-close').click(function ()
{
    $('#modal').css('display','none');
})

function render(index) {
    var timeStart =$("#date_timepicker_start").val();
    var timeEnd =  $("#date_timepicker_end").val();
    var roles = $("#imeiDevice").val();

    var item = new FormData();
    item.id = roles;
    item.imei = $("#imeiDevice option:selected").text();
    item.start_time = timeStart;
    item.end_time = timeEnd;
    console.log(item);

    var modalContent = "";
    var dataTable = "";
    var table = "";
    $.ajax({
        url: "/loadLocationHistory",
        method: "POST",
        dataType: 'json',
        data:JSON.stringify(item),
        contentType: "application/json; charset=utf-8",
        success: function (res) {
            var data = res.content;
            for (var i =0; i<data.length;i++)
            {
                if ((data[i].stt) == index)
                {
                    modalContent += '<b>'+'Imei: '+data[i].imei+'</b>';
                    modalContent += '<div>'+'ID: '+data[i].bienso+'</div>';
                    modalContent += '<div>'+'Thời gian: '+moment.unix(data[i].logDate).format("DD/MM/YYYY HH:mm:ss")+'</div>';
                    modalContent += '<div>'+'Tọa độ: '+ convertDMS(data[i].lat,data[i].lon)+'</div>';
                    modalContent += '<div>'+'Trạng thái 1: '+data[i].status1 +'</div>';

                    dataTable += '<tr>';
                    dataTable +='<td>'+ data[i].ss+'</td>';
                    dataTable +='<td>'+ data[i].P+'</td>';
                    dataTable +='<td>'+ data[i].B+'</td>';
                    dataTable +='<td>'+ data[i].r+'</td>';
                    dataTable +='<td>'+ data[i].V+'</td>';
                    dataTable +='<td>'+ data[i].W+'</td>';
                    dataTable +='<td>'+ data[i].Cn+'</td>';
                    dataTable +='<td>'+ data[i].M+'</td>';
                    dataTable +='<td>'+( (data[i].N == 3)?"NB/2G":(data[i].N ==2 ? "2G": " "))+'</td>';
                    dataTable += '</tr>';
                }
            }
            document.getElementById('modal-id').innerHTML= modalContent ;
            document.getElementById('modal-table-content').innerHTML= dataTable ;
        },
        error: function (error) {
            alert(2);
        }
    })
}


function renderMarker()
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

    $.ajax({
        url:"/loadLocationHistory",
        method:"POST",
        dataType: 'json',
        data:JSON.stringify(item),
        contentType: "application/json; charset=utf-8",
        success:function (res){
            console.log(11);
            var data = res.content;
            mapboxgl.accessToken = 'pk.eyJ1IjoiaHV5c3VwcG9ydCIsImEiOiJjbDB2dDV1d3kxYWF6M2lvMmo0Mml3MWJ2In0.sBwz__9G5aSTCROFnlKrYw';

            const map = new mapboxgl.Map({
                container: 'map',
                style: 'mapbox://styles/mapbox/streets-v11',
                //   center: [ 103.90462168031246, 20.668780521951536 ],
                center: [105.811197,21.067695 ],
                zoom: 8,
            });
            map.addControl(new mapboxgl.FullscreenControl(),'bottom-right');
            map.addControl(new mapboxgl.NavigationControl(),'bottom-right');

            var startRun = "http://203.113.138.18:4444/assets/img/car/greenVtag.png";
            var alarm = "http://203.113.138.18:4444/assets/img/car/redVtag.png";
            var stop = "http://203.113.138.18:4444/assets/img/car/orangeVtag.png";
            var point = "http://203.113.138.18:4444/assets/img/car/blueVtag.png";
            var table = "";
            for (var i =0; i<data.length;i++)
            {
                if(data[i].type == MARKER_TYPE.SOS.type || data[i] .type == MARKER_TYPE.BL.type ){
                    image = alarm;
                } else if(data[i].type == MARKER_TYPE.RUN.type){
                    image = startRun;
                } else if(data[i].type == MARKER_TYPE.PARK.type){
                    image = stop;
                } else {
                    image = point;
                }
                el = document.createElement('div');
                el.style.background='url("'+image+'")';
                el.style.width='32px';
                el.style.height='32px';
                el.style.cursor='pointer';
                el.id=data[i].stt;

                var infoHistory = "";
                infoHistory += '<b>'+'Imei: '+data[i].imei+'</b>';
                infoHistory += '<div>'+'ID: '+data[i].bienso+'</div>';
                infoHistory += '<div>'+'Thời gian: '+moment.unix(data[i].logDate).format("DD/MM/YYYY HH:mm:ss")+'</div>';
                infoHistory += '<div>'+'Tọa độ: '+ convertDMS(data[i].lat,data[i].lon)+'</div>';
                infoHistory += '<div>'+'Trạng thái 1: '+data[i].status1 +'</div>';

                var dataTable = '<table id="datatables" className="table">\n' +
                    '                    <thead>\n' +
                    '                    <tr>\n' +
                    '                        <th>RSRP</th>\n' +
                    '                        <th>Period</th>\n' +
                    '                        <th>Battery</th>\n' +
                    '                        <th>RSRQ</th>\n' +
                    '                        <th>Version</th>\n' +
                    '                        <th>Wifi</th>\n' +
                    '                        <th>Connect</th>\n' +
                    '                        <th>Mode</th>\n' +
                    '                        <th>NB/2G</th>\n' +
                    '                    </tr>\n' +
                    '                    </thead>\n' +
                    '                    <tbody id="modal-table-content">';
                dataTable += '<tr>';
                dataTable +='<td>'+ data[i].ss+'</td>';
                dataTable +='<td>'+ data[i].P+'</td>';
                dataTable +='<td>'+ data[i].B+'</td>';
                dataTable +='<td>'+ data[i].r+'</td>';
                dataTable +='<td>'+ data[i].V+'</td>';
                dataTable +='<td>'+ data[i].W+'</td>';
                dataTable +='<td>'+ data[i].Cn+'</td>';
                dataTable +='<td>'+ data[i].M+'</td>';
                dataTable +='<td>'+( (data[i].N == 3)?"NB/2G":(data[i].N ==2 ? "2G": " "))+'</td>';
                dataTable += '</tr>';
                dataTable += '</tbody>';
                dataTable += '</table>';


                const popup = new mapboxgl.Popup()
                    .setHTML( infoHistory + dataTable);

                const marker = new mapboxgl.Marker({
                    element: el,
                })
                    .setLngLat([data[i].lon,data[i].lat])
                    .setPopup(popup)
                    .addTo(map);

                table +='<tr onclick=\'marker\'>';
                table += '<td>'+data[i].stt+'</td>';
                table += '<td>'+Object.values( MARKER_TYPE ).find(t => t.type == data[i].type)?.name+'</td>';
                table += '<td>'+moment.unix(data[i].logDate).format("DD/MM/YYYY HH:mm:ss")+'</td>';
                table +='</tr>';
                document.getElementById('table-map').innerHTML = table;

            }

        },
        error:function (err){
            alert(21);
            console.log("error");
        }
    })
}

// Convert
function toDegreesMinutesAndSeconds(coordinate) {
    var absolute = Math.abs(coordinate);
    var degrees = Math.floor(absolute);
    var minutesNotTruncated = (absolute - degrees) * 60;
    var minutes = Math.floor(minutesNotTruncated);
    // var seconds = Math.floor((minutesNotTruncated - minutes) * 60);
    var seconds = ((minutesNotTruncated - minutes) * 60).toFixed(2);
    return degrees + "°" + minutes + "'" + seconds +"''";
}

function convertDMS(lat, lng) {
    var latitude = toDegreesMinutesAndSeconds(lat);
    var latitudeCardinal = Math.sign(lat) >= 0 ? "N" : "S";

    var longitude = toDegreesMinutesAndSeconds(lng);
    var longitudeCardinal = Math.sign(lng) >= 0 ? "E" : "W";

    return latitude + " " + latitudeCardinal + " - " + longitude + " " + longitudeCardinal;
}