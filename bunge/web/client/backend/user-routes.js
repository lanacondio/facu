var express = require('express'),
    _       = require('lodash'),
    config  = require('./config'),
    jwt     = require('jsonwebtoken');
    request = require('request');

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

    var options = {
      uri: 'http://162.243.200.232/bunge_api.php/user?file='+ "'"+req.body.username+"'",
      port: 80
    };


    request(options, function(error, response, body){
        if(error){
          console.log(error);
        } 
        else{
  
          var user_api = JSON.parse(body);

          if(!user_api.user.records[0] ){
              return res.status(404).send("User not found");  
          }

          if(!req.body.password === user_api.user.records[0][5]){          
              return res.status(401).send("The username or password don't match");                      
           } 

              res.status(201).send({
              id_token: createToken(user_api.user),
              username:  user_api.user.records[0][2],
              id: user_api.user.records[0][0],         
            });          

        };
  
    });
});

app.post('/credits', function(req, res) {
  if (!req.body.user_id) {
    return res.status(400).send("You must send the userid");
  }

  var options = {
      uri: 'http://162.243.200.232/bunge_api.php/credit?user_id='+ "'"+req.body.user_id+"'",
      port: 80
  };


  request(options, function(error, response, body){
    if(error){
      console.log(error);
    } 
    else{
      var credits_api = JSON.parse(body);
      console.log(credits_api);
      if(credits_api.records === null){
        return res.status(401).send("The user has no credits");  
      }


      var credits_list = [];

      credits_api.credit.records.forEach(function(element) {
        credits_list.push(
        {
          id: element[0],
          user_id: element[1],
          category_id: element[2],
          quantity: element[3],
          event_id: element[4]

        });
      });

      res.status(201).send({
      credits: credits_list        
      });
 

      };
  
    });

});




app.post('/productsbycategory', function(req, res) {
  if (!req.body.category_id) {
    return res.status(400).send("You must send the categoryid");
  }

  var options = {
      uri: 'http://162.243.200.232/bunge_api.php/product?category_id='+ "'"+req.body.category_id+"'",
      port: 80
  };

  request(options, function(error, response, body){
    if(error){
      console.log(error);
    } 
    else{
      var products_api = JSON.parse(body);

      if(products_api.records ===null){
        return res.status(401).send("The category has no credits");  
      }

      var product_list = [];

      products_api.product.records.forEach(function(element) {
        product_list.push(
        {
          id: element[0],
          category_id: element[1],
          description: element[2],      
          photo_url: element[3],
          stock: element[4]
        });
      });

        res.status(201).send({
          products: product_list        
        });
      };
  
    });

});


app.post('/event', function(req, res) {
  
  var options = {
      uri: 'http://162.243.200.232/bunge_api.php/event',
      port: 80
  };

  request(options, function(error, response, body){
    
    if(error){
      console.log(error);
    } 
    else{
      var events_api = JSON.parse(body);

      if(!events_api.records){
        return res.status(401).send("no events");  
      }

      var event_list = [];

      events_api.product.records.forEach(function(element) {
        event_list.push(
        {
          id: element[0],
          start_date: element[1],
          end_date: element[2],
          description: element[3]
        });
      });

        res.status(201).send({
          events: event_list        
        });
      };
  
    });

});






