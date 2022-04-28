
// main
$(document).ready(function() {
    map();
    listID();
    register();
})
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

function map(){
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
}

function listID()
{
    var req = new FormData();
    req.request = "GetAttribute"

    var data = new FormData();
    data.id = "78639604-8276-480d-8962-65a54f3833d2"
    data.type = "device"
    data.attribute = ["location"]

    req.data = data

    $.ajax({
        url : "/getAttribute" ,
        method: "POST",
        dataType: 'json',
        /*
        data: JSON.stringify({
            "request": "GetAttribute",
            "data": {
                "id": "78639604-8276-480d-8962-65a54f3833d2",
                "type": "device",
                "attribute": ["location"]
            }
            }),

         */
        data: JSON.stringify(req),
        contentType: "application/json; charset = utf-8" ,
        success:function (res)
        {
            console.log(11);
            console.log(res.content.data.data.location);
            var data = res.content.data.data.location;
            console.log(data.length);
            var dataTable = '<table id="datatables" className="table">\n' +
                '                    <thead>\n' +
                '                    <tr>\n' +
                '                        <th>ID hiện tại</th>\n' +
                '                        <th>ID đăng ký</th>\n' +
                '                        <th>ID thiếu</th>\n' +
                '                        <th>ID lạ</th>\n' +
                '                    </tr>\n' +
                '                    </thead>\n' +
                '                    <tbody id="modal-table-content">';

            // for (var i =0 ; i <data.length; i++)
            // {
                dataTable += '<tr>';
                dataTable +='<td>'+data.missing_id+'</td>';
                dataTable +='<td>'+ data.registered_id+'</td>';
                dataTable +='<td>'+data.unknow_id+'</td>';
                dataTable +='<td>'+data.current_id+'</td>';
                dataTable += '</tr>';
            // }*/
            dataTable += '</tbody>';
            dataTable += '</table>';
            document.getElementById('table').innerHTML = dataTable;
        },
        error:function (err)
        {
            console.log(err);
        }
    })
}


function register()
{
    var req = new FormData();
    req.request = "Set-RF"
    var data = new FormData();
    data.id = "78639604-8276-480d-8962-65a54f3833d2"
    var rfid = ["E2000017260401982240321E",
        "E2000017690601070820C648",
        "E2000057690601710820C6C8",
        "E2000017260400752330279E",
        "E20000172604018823302886",
        "E20000172604008022802D4E",
        "E20000172604005523402954",
        "E200001726040110233027E7",
        "E20000172604014223302A27"]
    data.set_rf = {"rfid":rfid}
    req.data = data

    $.ajax({
        url:"/setRFID",
        method:"POST",
        dataType: 'json',
        data:JSON.stringify(req),
        contentType: "application/json; charset=utf-8",
        success:function (res){
            console.log(1);
            console.log(res)
        },
        error:function (ER)
        {
            console.log("error")
        }
    })
}