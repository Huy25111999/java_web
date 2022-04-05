
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

$(document).ready(function() {
    marker();
});
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