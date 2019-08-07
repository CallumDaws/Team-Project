var jwt = require('jsonwebtoken');

var teamid = "4718"

var token = jwt.sign({
    "team_id": teamid
}, 'secret');

console.log(token);