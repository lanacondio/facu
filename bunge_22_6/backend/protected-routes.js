var express = require('express'),
    jwt     = require('express-jwt'),
    config  = require('./config');

var app = module.exports = express.Router();
var api_user_url = 'http://localhost/bunge/users.php';
var api_transaction_url = 'http://localhost/bunge/transaction.php';

var jwtCheck = jwt({
  secret: config.secret
});

app.use('/api/protected', jwtCheck);

app.get('/api/protected/random-quote', function(req, res) {
  res.status(200).send(quoter.getRandomOne());
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

        
          credits_list.push(
          {
            id: element.id,
            user_id: element.user_id,
            category_id: element.category_id,
            quantity: element.quantity,
            event_id: element.event_id

          });  
        
        
      });

      res.status(201).send({
        credits: credits_list        
      });
 
  });

});


app.post('/transactionsbyuser', function(req, res) {
  if (!req.body.user_id) {
    return res.status(400).send("You must send the userid");
  }


  var client = new Client();

  client.get(api_user_url +'/transaction?transform=1&filter=user_id,eq,'+req.body.user_id
  , function (data, response) {

      var transaction_api = data;      
      
      var transaction_list = [];
      
      transaction_api.transaction.forEach(function(element) {        

        
          transaction_list.push(
          {
            id: element.id,
            user_id: element.user_id,
            product_id: element.product_id,
            date: element.date

          });  
        
        
      });

      res.status(201).send({
        transaction: transaction_list        
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
            title: element.title,
            stock: element.stock
          });
        });

        res.status(201).send({
          products: product_list        
        });
    });

});

app.post('/events', function(req, res) {
 
  var client = new Client();
  client.get(api_user_url+'/event?transform=1' , function (data, response) {

      var events_api = data;

      if(!events_api.event[0]){
        return res.status(401).send("no events");  
      }

      var event_list = [];

      console.log(events_api);
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


app.post('/eventbyid', function(req, res) {
 
  var client = new Client();
  client.get(api_user_url+'/event?transform=1&filter=id,eq,'+req.body.event_id , function (data, response) {

      var events_api = data;
      console.log(api_user_url+'/event?transform=1&filter=id,eq,'+req.body.event_id);
      console.log(data);
      if(!events_api.event[0]){
        return res.status(401).send("no events");  
      }

      var event_list = [];

      console.log(events_api);
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



app.post('/userbyid', function(req, res) {
 
  var client = new Client();
  client.get(api_user_url+'/user?transform=1&filter=id,eq,'+req.body.user_id , function (data, response) {

      var users_api = data;
      
      if(!users_api.user[0]){
        return res.status(401).send("no user");  
      }

      var user_list = [];

      users_api.user.forEach(function(element) {
          user_list.push(
          {
            id: element.id,
            file: element.file            
          });
        });

        res.status(201).send({
          user: user_list        
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
      if(!transaction_res){
        return res.status(401).send("no transactions");  
      }


    res.status(201).send({
      transactions: transaction_res
    });

    
  });


});



app.post('/categoriesbyevent', function(req, res) {

  if (!req.body.event_id) {
    return res.status(400).send("You must send the all parameters");
  }

  // Build the post string from an object
  var client = new Client();

  client.get(api_user_url +'/category?transform=1&filter=event_id,eq,'+req.body.event_id
  , function (data, response) {
      var category_api = data;

      if(!category_api.category[0]){
        return res.status(401).send("The event has no category");  
      }

      var category_list = [];

      category_api.category.forEach(function(element) {
          category_list.push(
          {
            id: element.id,
            event_id: element.event_id,
            description: element.description
          });
        });

        res.status(201).send({
          categories: category_list        
        });
    });


});

