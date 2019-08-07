var express = require('express');
var bodyParser = require('body-parser')
var mysql = require('mysql')
var app = express();
app.use(bodyParser.json())

// Database details
var connection = mysql.createConnection({
    host: 'localhost',
    user: 'treasurehuntbackend',
    password: 'nu123',
    database: 'treasurehunt'
});

connection.connect(function(err) {
    if (err) {
        console.error('error connecting: ' + err.stack);
        return;
    }
    console.log('connected as id ' + connection.threadId);
});

app.get('/getnumberofplayers', function(req, res) {
    connection.query('SELECT COUNT(*) as count FROM players', function(error, results, fields) {
        if (error) {
            res.send('error');
        } else {
        	res.send(results[0]);
        }
        
    });
});

app.get('/getnumberofteams', function(req, res) {
    connection.query('SELECT COUNT(*) as count FROM teams', function(error, results, fields) {
        if (error) {
            res.send('error');
        } else {
        	res.send(results[0]);
        }
        
    });
});

app.get('/getgamesplayed', function(req, res) {
    connection.query('SELECT gamesplayed FROM stats', function(error, results, fields) {
        if (error) {
            res.send('error');
        } else {
        	res.send(results[0]);
        }
        
    });
});

app.listen(process.env.PORT || 4741);