var express = require('express'),
    _       = require('lodash'),
    config  = require('./config'),
    jwt     = require('jsonwebtoken');

var app = module.exports = express.Router();

// XXX: This should be a database of users :).
var users = [{
  id: 1,
  username: 'gonto',
  password: 'gonto'
}];

function createToken(user) {
  return jwt.sign(_.omit(user, 'password'), config.secret, { expiresInMinutes: 60*5 });
}

app.post('/users', function(req, res) {
  if (!req.body.username || !req.body.password) {
    return res.status(400).send("You must send the username and the password");
  }
  if (_.find(users, {username: req.body.username})) {
   return res.status(400).send("A user with that username already exists");
  }

/*
    var options = {
      host: '162.243.200.232',
      port: 80,
      path: '/bunge_api.php/user?file='+ "'"+req.body.username+"'"
  };


  http.get(options, function(resp){
    resp.on('data', function(chunk){
      //do something with chunk
      debugger;
        if(!chunk.json().user){
          return res.status(401).send("The username or password don't match");  
        }

        if(req.body.password === chunk.json().user.records[0][5]){
          
          res.status(201).send({
            id_token: createToken(user),
            username: chunk.json().user.records[0][2],
            id: chunk.json().user.records[0][0],         
          });
          
      }
      else{
        return res.status(401).send("The username or password don't match");
      }
    
      });
    }).on("error", function(e){
      console.log("Got error: " + e.message);
    });


  this.http.get('http://162.243.200.232/bunge_api.php/user?file='+ "'"+username+"'", { headers: contentHeaders })
  .subscribe(
  response => {
    debugger*/

    
  var profile = _.pick(req.body, 'username', 'password', 'extra');
  profile.id = _.max(users, 'id').id + 1;

  users.push(profile);

  res.status(201).send({
    id_token: createToken(profile)
  });
});

app.post('/sessions/create', function(req, res) {
  if (!req.body.username || !req.body.password) {
    return res.status(400).send("You must send the username and the password");
  }


  //connect with sql
  var user = _.find(users, {username: req.body.username});
  if (!user) {
    return res.status(401).send("The username or password don't match");
  }

  if (!(user.password === req.body.password)) {
    return res.status(401).send("The username or password don't match");
  }

  res.status(201).send({
    id_token: createToken(user),
    username: req.body.username,
    id: 1,
     
  });
});


app.post('/credits', function(req, res) {
  if (!req.body.user_id) {
    return res.status(400).send("You must send the userid");
  }

  //connect with sql
  //var credits = _.find(users, {username: req.body.username});
  //if (!credits) {
  //  return res.status(401).send("The user don't have credits");
  //}
  

  res.status(201).send({
    credits: 
    [{
      id: 2,
      user_id: 1,
      category_id: 1,
      quantity: 1
    },
    {
      id: 3,
      user_id: 1,
      category_id: 2,
      quantity: 2
    }
    ]
      
  });
});




app.post('/productsbycategory', function(req, res) {
  if (!req.body.category_id) {
    return res.status(400).send("You must send the categoryid");
  }
  //connect with sql



  //var credits = _.find(users, {username: req.body.username});
  //if (!credits) {
  // return res.status(401).send("The user don't have credits");
  //}
  

  res.status(201).send({
    products: 
    [{
      id: 2,      
      category_id: 1,
      description: "auto a control remoto",
      photo_url:"http://www.sanborns.com.mx/img/1200/7506300106358.jpg",    
      stock: 1
    },
    {
      id: 3,      
      category_id: 1,
      description: 'camiseta',
      photo_url:'http://www.sanborns.com.mx/img/1200/7506300106358.jpg',
      stock: 3
    }
    ]
      
  });
});







  