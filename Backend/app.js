/*
Author: Ben Gerard
Date: 17/3/17
Purpose: Provides an API that powers the Campus Chase app.
*/
var express = require('express');
var bodyParser = require('body-parser')
var mysql = require('mysql')
var jwt = require('jsonwebtoken');
var app = express();
var FastMap = require("collections/fast-map");
var multer = require('multer')
var upload = multer({
    dest: 'uploads/'
})

multer({
  limits: { fieldSize: 25 * 1024 * 1024 }
})

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

// Error Reporting
/*
process.on('uncaughtException', function (err) {
  console.error(err);
  console.log("Node NOT Exiting...");
  // Report errors here, with request to stats server
});
*/

app.post('/uploadpicture', upload.single('profilepic'), function(req, res, next) {
    addPictureToPlayer(req.body.playername, req.body.teamid, req.file.filename);
    res.send("uploaded");
})

function addPictureToPlayer(playername, teamid, filename) {
	connection.query('UPDATE players SET profilepicture = ? WHERE playername = ? and teamid = ?', [filename, playername, teamid], function(error, results, fields) {
});
}
 

app.post('/createteam', function(req, res) {
    // Generate 4 digit id
    var id = generateID();
    addIDToDatabase(id);
    //gamePlayed();
    selectTeamColour(function(colour) {
    	connection.query('UPDATE teams SET colour = ? WHERE teamid = ?', [colour,id], function(error, results, fields) {
       
    	});
    });
    addTeamName(req.body.teamname, id.toString());
    updateTeamQuestionNumber(0, id.toString());
    res.send(id.toString());
});

function addTeamName(teamname, teamid) {
	connection.query('UPDATE teams SET teamname = ? WHERE teamid = ?', [teamname, teamid], function(error, results, fields) {
       
    });
}

app.post('/assignteamleader', function(req, res) {
    // Get team id from request
    var teamid = req.body.teamid;
    // get player name from request
    var playerName = req.body.playername;

    createPlayer(teamid, playerName, function(result) {
    	console.log(result);
        if (result) {
            var token = jwt.sign({
                "team_id": teamid
            }, 'secret');
            addTeamleader(teamid, playerName);
            updateTeamScore(teamid, 0);
            res.send(token);
        } else {
            res.send('error');
        }
    });
});

app.post('/jointeam', function(req, res) {
    // Get team id from request
    var teamid = req.body.teamid;
    var playerName = req.body.playername;

    // get player name from request
    // assign team id to player name
    createPlayer(teamid, playerName, function(result) {
    	console.log(result);
        if (result == true) {
            var token = jwt.sign({
                "team_id": teamid
            }, 'secret');
            res.send(token);
        } else if (result == false) {
            res.send('player name already taken');
        }
    });

});

app.delete('/removeteam', function(req, res) {
    // get team id from request
    var teamid = req.body.teamid;
    var playername = req.body.playername;
    var token = req.body.token;

    checkToken(token, playername, function(valid) {
        if (valid) {
            connection.query('DELETE FROM teams WHERE teamid = ?', teamid, function(error, results, fields) {
                if (error) {
                    res.send('error');
                } else {
                    res.send('removed');
                }
            });
        } else {
            res.send('Forbidden');
        }
    });
});

app.delete('/removeplayer', function(req, res) {
    // get team id from request
    var playername = req.body.playername;
    var token = req.body.token;
    // remove the team and all of its players 
    checkToken(token, playername, function(valid) {
        if (valid) {
            connection.query('DELETE FROM players WHERE playername = ?', playername, function(error, results, fields) {
                if (error) {
                    res.send('error');
                } else {
                    res.send('removed');
                }
            });
        } else {
            res.sendStatus(403);
            res.send('Forbidden');
        }
    });
});

app.post('/updateplayerlocation', function(req, res) {
    var playername = req.body.playername;
    var location = req.body.location;
    var token = req.body.token;
    checkToken(token, playername, function(valid) {
        if (valid) {
        	var decoded = jwt.verify(token, 'secret');
        	var tokenteamid = decoded.team_id;
            connection.query('UPDATE players SET location = ? WHERE playername = ? AND teamid = ?', [location, playername, tokenteamid], function(error, results, fields) {
                if (error) {
                    res.send('error');
                } else {
                    res.send('location updated');
                }
            });
        } else {
            res.send('Forbidden');
        }
    });
});

app.post('/updateteamscore', function(req, res) {
    var playername = req.body.playername;
    var teamid = req.body.teamid;
    var token = req.body.token;
    var score = req.body.score;
    checkToken(token, playername, function(valid) {
        if (valid) {
            updateTeamScore(teamid, score);
        } else {
            res.send('Forbidden');
        }
    });
});

app.post('/getteamdetails', function(req, res) {
    // Get team id from request
    var teamid = req.body.teamid;
    var response = new FastMap();
    var players = [];

    getTeamDetails(teamid, function(details) {
        getTeamPlayers(teamid, function(playersArray) {
            details.players = playersArray;
            res.send(details);
        });
    });
});

app.post('/getallteamscores', function(req, res) {
  	getAllTeamScores(function(scores){
        res.send(scores);
     })
});

app.post('/gettoken', function(req, res) {
    var token = jwt.sign({
        "team_id": req.body.teamid
    }, 'secret');
    res.send(token);
});

app.post('/playersinteam', function(req, res) {
    var teamid = req.body.teamid;
           getTeamPlayers(teamid , function(players){
           		res.send(players.length.toString());
           });
});

app.post('/updateteamquestion', function(req, res) {
	console.log("update team question");
    var teamid = req.body.teamid;
    var questionNumber = parseInt(req.body.questionNumber);
    updateTeamQuestionNumber(questionNumber, teamid);
	res.send("question updated");
});

app.post('/updateplayername', function(req, res) {
    var teamid = req.body.teamid;
    var name = req.body.playername;
    connection.query('UPDATE players SET playername = ? WHERE teamid = ?', [name, teamid], function(error, results, fields) {
    	
	});
	res.send("name updated");
});

app.post('/checkteamid', function(req, res) {
    var teamid = req.body.teamid;
    
    connection.query('SELECT COUNT(*) FROM teams WHERE teamid = ?', teamid, function(error, results, fields) {
    	res.send(results[0]);
	});
});

function updateTeamQuestionNumber(questionNumber, teamid) {
	connection.query('UPDATE teams SET question = ? WHERE teamid = ?', [questionNumber, teamid], function(error, results, fields) {
    	
	});
}

function generateID() {
    var id = random(1000, 9999);
    console.log(id);
    var idExists = checkIDExists(id);
    if (idExists == true) {
        generateID();
    }
    return id;
}

function random(min, max) {
    return Math.floor(Math.random() * (max - min + 1) + min);
}

function checkIDExists(id) {
    // Connect to datbase check if id is there
    connection.query('SELECT teamid FROM teams WHERE teamid = ?', id, function(error, results, fields) {
        if (results && results.length == 0) {
            // ID does not exist
            return false;
        } else {
            return true;
        }
    });
}

function addIDToDatabase(id) {
    connection.query('INSERT INTO teams SET teamid = ?', id, function(error, results, fields) {
         console.log(error);
    });
}

function createPlayer(teamid, playername, cb) {
    connection.query('INSERT INTO players SET teamid = ?, playername = ?', [teamid, playername], function(error, results, fields) {
        if (!error) {
            cb(true);
        } else {
            cb(false);
        }
    });

}

function addTeamleader(teamid, playername) {
    connection.query('UPDATE teams SET teamleader = ? WHERE teamid = ?', [playername, teamid], function(error, results, fields) {
        if (error) throw error;

    });
}

function selectTeamColour(cb) {
    // pick colour from 
    //var colours = require("./colours").colours;
    var colour = '#' + ("000000" + Math.random().toString(16).slice(2, 8).toUpperCase()).slice(-6);

    connection.query('SELECT colour FROM teams WHERE colour = ?', colour, function(error, results, fields) {

            if (results.length > 0) {
                // Colour exists, pick new one
                selectTeamColour();
            } else {
                cb(colour);
            }
        });
}


function checkToken(token, playername, cb) {
	console.log("checkToken called");
    try {
        var decoded = jwt.verify(token, 'secret');
        var tokenteamid = decoded.team_id;
        connection.query('SELECT playername FROM players WHERE teamid = ?', tokenteamid, function(error, results, fields) {
            if (results.length > 0) {
                // Player exists at team, allow access
                cb(true);
            } else {
                cb(false);
            }
        });
    } catch (err) {
        // err
        console.log('token error');
    }
}


function getTeamDetails(teamid, cb) {
    connection.query('SELECT * FROM teams WHERE teamid = ?', teamid, function(error, results, fields) {
        if (error) {
        } else {
        
            var response = {
            	"teamname": results[0].teamname,
                "teamid": results[0].teamid,
                "teamleader": results[0].teamleader,
                "score": results[0].score,
                "colour": results[0].colour,
                "question": results[0].question
            };
            cb(response);
            
		}
    });
}


function getTeamPlayers(teamid, cb) {
    connection.query('SELECT playername, location, profilepicture FROM players WHERE teamid = ?', teamid, function(error, results, fields) {
        if (error) {
            res.send('error');
        } else {
            var players = [];
            for (var i = 0; i < results.length; i++) {
                var playerData = results[i];
                var player = {
                    "playername": playerData.playername,
                    "location": playerData.location,
                    "profilepicture": playerData.profilepicture
                };
                players.push(player);
            }
        }
        cb(players);
    });
}

function getAllTeamScores(cb) {
	  connection.query('SELECT score, teamid FROM teams', function(error, results, fields) {
        if (error) {
            res.send('error');
        } else {
        	var scores = [];
        	for (var i = 0; i < results.length; i++) {
        		var scoreData = results[i];
        		var score = {
        			"teamid":scoreData.teamid,
        			"score":scoreData.score
        		};
        		scores.push(score);
        	} 
        }
        cb(scores);
        
    });
}

function gamePlayed() {
	getGamesPlayed(function(gamesplayed) {
		connection.query('UPDATE stats SET gamesplayed = ?', gamesplayed++, function(error, results, fields) {
                if (error) {
                    // error
                } 
     });
	});
	
}

function getGamesPlayed(cb) {
	connection.query('SELECT gamesplayed FROM stats',  function(error, results, fields) {
                if (error) {
                    // error
                    cb(results[0]);
                } 
     });
}

function updateTeamScore(teamid, score) {
	connection.query('UPDATE teams SET score = ? WHERE teamid = ?', [score, teamid], function(error, results, fields) {
                
        });
}

app.listen(process.env.PORT || 4740);