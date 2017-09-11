var express = require('express');
var path = require('path');
var fs = require('fs');
var app = express();
var bodyParser = require('body-parser');

app.listen(8084, function() {
	console.log('SnackSpeak s\'execute sur 8084');
});

app.set('case sensitive routing', false);
app.set('views',path.join(__dirname,'views'))

app.use(express.static(path.join(__dirname,'public')));

app.use(bodyParser.json());

app.use(function(req,res,next) {

	fs.readFile('bd.json','utf-8',function(err,data) {
		if(err) {
			next(err);
		} else {
			data = JSON.parse(data);
			req.machines = data['vending-machines'];
			
			next();
		}
	});
});

app.get('/index.html' , function(req,res) {
	return res.sendFile('/public/index.html');
});



app.get('/vending-machines',function(req,res) {
	var obj = {'machines': []};
	for(var i = 0; i < req.machines.length; i++) {
		console.log(req.machines[i]);
		obj.machines[i] = req.machines[i].id;
	}
	return res.json(obj);
});

app.get('/vending-machine/:id',function(req,res) {
	var id = req.params.id;
	console.log("get articles for " + id);
	for(var i = 0; i < req.machines.length; i++) {
		if(req.machines[i].id == id) {
			return res.json({"items": req.machines[i].items});
		}
	}
	res.sendStatus(404);
	return res.json({'erreur': "Il n'existe aucune machine avec id " + id});
});


app.put('/vending-machine/:id', function(req,res) {
	var id = req.params.id;
	for(var i = 0;  i < machines.length; i++) {
		if(machines[i].id == id) {
			
		}
	}
});

app.get('/index',function(req,res) {
	console.log('index caught');
	return res.sendFile(path.join(__dirname,'index.html'));
});

