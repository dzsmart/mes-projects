var Hadj = new Firebase('https://mohajijapp.firebaseio.com/HaajInfo');

function findFromList(event) {
    if (event.which == 13 || event.keyCode == 13) { // as the user presses the enter key, we will attempt to save the data
        var n = document.getElementById('search').value.trim();
        var lis = '';
        var found = "";
        for (var i = 0; i < list.length; i++) {
            if(list[i].name.indexOf(n) !== -1) {
                found += '<li data-key="' + list[i].key + '">('+ list[i].id+') '+ list[i].name+ '</li>';
            }
            
        };
        document.getElementById('Hadj2').innerHTML = found;
        return false;
    }
};

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

function saveToList(event) {
    //if (event.which == 13 || event.keyCode == 13) { // as the user presses the enter key, we will attempt to save the data
        var _id = document.getElementById('_id').value.trim();
        var name = document.getElementById('name').value.trim();
        var alt = document.getElementById('alt').value.trim();
        var lat = document.getElementById('lat').value.trim();
        var tp = document.getElementById('tp').value.trim();
        if (name.length > 0) {
            saveToFB(_id, name, alt, lat, tp);
        }
        document.getElementById('_id').value = '';
        document.getElementById('name').value = '';
        document.getElementById('alt').value = '';
        document.getElementById('lat').value = '';
        document.getElementById('tp').value = '';
        return false;
    //}
};



function saveToFB(id, name, alt, lat, tp) {
    // this will save data to Firebase
    Hadj.push({
        id : id,
        name: name,
        alt : alt,
        lat : lat,
        tp : tp,
    });
};

function refreshUI(list) {
    var lis = '';
    for (var i = 0; i < list.length; i++) {
        lis += '<li data-key="' + list[i].key + '">('+ list[i].id+') '+ list[i].name+'  '+ list[i].alt+'  '+ list[i].lat+' مطوفه : '+ findMotawif(list[i].tp)+ '</li>';
    };
    document.getElementById('Hadj').innerHTML = lis;
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