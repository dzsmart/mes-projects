var Hadj = new Firebase('https://mohajijapp.firebaseio.com/LoginInfo');

function refreshUI(list) {
    var lis = '';
    for (var i = 0; i < list.length; i++) {
        lis += '<li data-key="' + list[i].key + '">'+ list[i].login+' '+ list[i].password+ '</li>';
    };
    document.getElementById('Hadjj').innerHTML = lis;
};

var list = [];
// this will get fired on inital load as well as when ever there is a change in the data
Hadj.on("value", function(snapshot) {
    var data = snapshot.val();
    
    for (var key in data) {
        if (data.hasOwnProperty(key)) {
            login = data[key].login ? data[key].login : '';
            password = data[key].password ? data[key].password : '';
            if (login.trim().length > 0) {
                list.push({
                    password: password,
                    key: key,
                    login:login,
                })
            }
        }
    }
    // refresh the UI
    refreshUI(list);
});

function findItem(event) {
        var l = document.getElementById('login').value.trim();
        var p = document.getElementById('password').value.trim();
        for (var i = 0; i < list.length; i++) {
            if(list[i].login === l && list[i].password === p) {
                alert('Welcom Mr.: '+l);
                location.replace("index.html");
                return true;
            }
            
        };
        alert('Login Not found.');
        return false;
};

function saveToList(event) {
    //if (event.which == 13 || event.keyCode == 13) { // as the user presses the enter key, we will attempt to save the data
        var login = document.getElementById('login').value.trim();
        var password = document.getElementById('password').value.trim();
        if (login.length > 0) {
            saveToFB(login, password);
        }
        document.getElementById('login').value = '';
        document.getElementById('password').value = '';
        return false;
    //}
};

function saveToFB(login, password) {
    // this will save data to Firebase
    Hadj.push({
        login : login,
        password: password
    });
};