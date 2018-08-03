function findFromList(list) {
var lis = "function initMap() {"+
        "var myLatLng = {lat: 21.4228714, lng: 39.823546};"+

        "var map = new google.maps.Map(document.getElementById('map'), {"+
          "zoom: 17,"+
          "center: myLatLng"+
        "});";
        for (var i = 0; i < list.length; i++) {
            lis += 'var marker_'+list[i].id+' = new google.maps.Marker({'+
                        'position: {lat: 21.4208714, lng: 39.823446},'+
                        'map: map,'+
                        'title: "Hello World!"'+
                      '});';
            
        };
        
        lis += "});}";
        alert(lis);
        document.getElementById('sc').innerHTML = lis;
        return false;
};