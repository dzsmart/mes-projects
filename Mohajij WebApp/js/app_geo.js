var Hadj = new Firebase('https://mohajijapp.firebaseio.com/HaajInfo');

function findFromList(event) {
        for (var i = 0; i < list.length; i++) {
            
            const A = list[i].lat;
            const B = list[i].alt;
            var myLatLng = {lat: A, lng: B};
            //var myLatLng = {lat: 21.422545072852396, lng: 39.82602510132481};
            var marker = new google.maps.Marker({
                            position: myLatLng,
                            title: list[i].name,
                            map:map
                        });
                        
        }
}

function findMotawif(idMotawif) {
    for (var i = 0; i < list.length; i++) {
       // alert(list[i].id+"   "+idMotawif+"  "+(list[i].id == idMotawif));
        if(list[i].id == idMotawif) {
            return list[i].name;
            break;
        }
    }
    
    return "/";
};

function refreshUI(list) {
    var lis = '';
    for (var i = 0; i < list.length; i++) {
        lis += '<li data-key="' + list[i].key + '">('+ list[i].id+') '+ list[i].name+'  '+ list[i].alt+'  '+ list[i].lat+' مطوفه : '+ findMotawif(list[i].tp)+ '</li>';
    };
    document.getElementById('Hadj').innerHTML = lis;
    //document.getElementById("Hadj").style.visibility = "hidden"; 
};
var list = [];
// this will get fired on inital load as well as when ever there is a change in the data
Hadj.on("value", function(snapshot) {
    var data = snapshot.val();
    
    for (var key in data) {
        if (data.hasOwnProperty(key)) {
            id = data[key].id ? data[key].id : '';
            name = data[key].name ? data[key].name : '';
            alt = data[key].alt ? data[key].alt : '';
            lat = data[key].lat ? data[key].lat : '';
            tp = data[key].tp ? data[key].tp : '';
            if (name.trim().length > 0) {
                list.push({
                    name: name,
                    key: key,
                    id:id,
                    alt: alt,
                    lat: lat,
                    tp:tp,
                })
            }
        }
    }
    // refresh the UI
    refreshUI(list);
});