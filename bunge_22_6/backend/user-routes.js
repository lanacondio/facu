var express = require('express'),
    _       = require('lodash'),
    config  = require('./config'),
    jwt     = require('jsonwebtoken');
    request = require('request');
    Client = require('node-rest-client').Client;
    querystring = require('querystring');
    http = require('http');
    fs = require('fs');


var app = module.exports = express.Router();
var api_user_url = 'http://localhost/bunge/users.php';
var api_transaction_url = 'http://localhost/bunge/transaction.php';

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

    var client = new Client();    

    client.get(api_user_url + '/user?transform=1&filter=file,eq,'+req.body.username
      , function (data, response) {
    
      var user_api = data;      
      if(!user_api.user[0]){
          return res.status(404).send("User not found");  
      }

      if(!req.body.password === user_api.user[0].password){          
        return res.status(401).send("The username or password don't match");                      
      } 

      //hacer un put de last login date
      res.status(201).send({
        id_token: createToken(user_api.user[0]),
        username:  user_api.user[0].name,
        id: user_api.user[0].id,         
      });              
      
    });

});

app.post('/credits', function(req, res) {
  if (!req.body.user_id) {
    return res.status(400).send("You must send the userid");
  }


  var client = new Client();

  client.get(api_user_url +'/credit?transform=1&filter=user_id,eq,'+req.body.user_id
  , function (data, response) {

      var credits_api = data;      
      
      if(!credits_api.credit[0]){
        return res.status(401).send("The user has no credits");  
      }

      var credits_list = [];

      credits_api.credit.forEach(function(element) {        

        if(element.quantity > 0){
          credits_list.push(
          {
            id: element.id,
            user_id: element.user_id,
            category_id: element.category_id,
            quantity: element.quantity,
            event_id: element.event_id

          });  
        }
        
      });

      res.status(201).send({
      credits: credits_list        
      });
 
  });

});




app.post('/productsbycategory', function(req, res) {
  if (!req.body.category_id) {
    return res.status(400).send("You must send the categoryid");
  }


  var client = new Client();

  client.get(api_user_url +'/product?transform=1&filter=category_id,eq,'+req.body.category_id
  , function (data, response) {
      var products_api = data;

      if(!products_api.product[0]){
        return res.status(401).send("The category has no credits");  
      }

      var product_list = [];

      products_api.product.forEach(function(element) {
          product_list.push(
          {
            id: element.id,
            category_id: element.category_id,
            description: element.description,      
            photo_url: element.photo_url,
            stock: element.stock
          });
        });

        res.status(201).send({
          products: product_list        
        });
    });

});


app.post('/event', function(req, res) {
  

  var client = new Client();

  client.get(api_user_url+'/event?transform=1' , function (data, response) {

      var events_api = data;

      if(!events_api.event[0]){
        return res.status(401).send("no events");  
      }

      var event_list = [];

      events_api.event.forEach(function(element) {
          event_list.push(
          {
            id: element.id,
            start_date: element.start_date,
            end_date: element.end_date,
            description: element.description,
            banner_url: element.banner_url

          });
        });

        res.status(201).send({
          events: event_list        
        });
      
  });

});




app.post('/buyproductuser', function(req, res) {

  if (!req.body.user_id || !req.body.product_id || !req.body.credit_id) {
    return res.status(400).send("You must send the all parameters");
  }

  // We need this to build our post string
  // Build the post string from an object
  var request = require('request');
  request.post({
    headers: {'content-type' : 'application/x-www-form-urlencoded'},
    url:     api_transaction_url+"/sale",
    body:    "user_id="+req.body.user_id+"&product_id="+req.body.product_id+"&credit_id="+req.body.credit_id,
  }, function(error, response, body){
    
    if(error){
      return res.status(400).send(error);
    }


    var transaction_res = JSON.parse(body);
      if(!transaction_res.transaction){
        return res.status(401).send("no transactions");  
      }


    res.status(201).send({
      transactions: transaction_res
    });

    
  });


});

