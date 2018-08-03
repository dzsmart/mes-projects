<!DOCTYPE html>
<html>
 
<head>
    <meta charset="utf-8">
    <title>Fire Base</title>
    <style>
        body {
	font-family: "calibri";
    background: #efefef;
    padding: 32px;
    padding-top: 16px;
    color: #333;
}
h1,h3 {
	text-align: center;
	color: #787878;
}

#myvar
{
	width: 98%;
	padding: 9px;
	font-size: 16px;
}
    </style>
</head>
 
<body>
    <h1>هيئة الحج و العمرة</h1>
    <h3>تنظيم الجموع</h3>
    <hr />
    <input type="text" id="_id" placeholder="Enter ID" />
    <input type="text" id="name" placeholder="Enter name"  />
    <input type="text" id="alt" placeholder="Enter ALT" />
    <input type="text" id="lat" placeholder="Enter LAT" />
    <input type="text" id="tp" placeholder="Enter Type" />
    <input type="button" onclick="return saveToList(event)" value="Submit" />
    <h4>الحجاج المتصلين حاليا</h4>
    <ol id="Hadj">
    </ol>
    <script type='text/javascript' src='https://cdn.firebase.com/js/client/1.0.15/firebase.js'></script>
    <script type="text/javascript" src="js/app.js"></script>
    <br>
    <hr>
    <input type="text" id="search" placeholder="Enter a name" onkeypress="return findFromList(event)"  />
    
    <ol id="Hadj2">
        
    </ol>
</body>
 
</html>