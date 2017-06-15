var express = require('express'),
    jwt     = require('express-jwt'),
    config  = require('./config'),
    quoter  = require('./quoter');

var app = module.exports = express.Router();

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

  var options = {
      uri: 'http://162.243.200.232/bunge_api.php/credit?filter=user_id,eq,'+req.body.user_id,
      port: 80
  };


  request(options, function(error, response, body){
    if(error){
      console.log(error);
    } 
    else{
      var credits_api = JSON.parse(body);      
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
      uri: 'http://162.243.200.232/bunge_api.php/product?filter=category_id,eq,'+req.body.category_id,
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


