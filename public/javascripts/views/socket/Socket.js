/**
 * Created by vtk-anhlt166 on 7/27/21.
 */
var isWSClosed = true;
var missed_reconnects = 0;

function isSecure()
{
    return window.location.protocol == 'https:';
}

function startSocket(urlWS, minuteMode) {
    var socket = new WS(urlWS);
    var heartbeat_ping_msg = '{"type":"ping", "minuteMode" : ' + minuteMode + '}';

    var openSocketEvent = function(event) {
        showNotification('Noti Success','success',Messages("Socket Connect"));
        isWSClosed = false;
        missed_reconnects = 0;
        // clientPingHeartbeat(socket);
        socket.send(heartbeat_ping_msg);
    };
    var receiveSocketEvent = function(event) {
        // console.log(event);
        var datajson = event.data;

        if(datajson=== null||datajson===""||typeof datajson !== 'string'){
            return;
        }

        updateData(JSON.parse(datajson));
        // start Web Socket load device position after 20s
        setTimeout(function() { socket.send(heartbeat_ping_msg) }, minuteMode ? 60000 : 60000);

    };
    var closeSocketEvent = function(event) {
        showNotification('Noti False','danger',Messages("Socket not Connect"));
        isWSClosed = true;
    };
    var errorSocketEvent = function(event) {
        showNotification('Noti False','danger',Messages("Socket error Connect"));
    };

    socket.onopen = openSocketEvent;
    socket.onmessage = receiveSocketEvent;
    socket.onclose = closeSocketEvent;
    socket.onerror = errorSocketEvent;
    return socket;
}

function updateData(datajson) {
    console.log(datajson);

}