var jwt = require('jsonwebtoken');

console.log(getIdFromToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZWFtX2lkIjoiNDU0MCIsImlhdCI6MTQ5MjE4MjE0MH0.AvX3oQnSxphaqCjwFsNKNxpfsiL8OVIhS2aJyghWok0"));

function getIdFromToken(token) {
	 var decoded = jwt.verify(token, 'secret');
     return decoded.team_id;
}