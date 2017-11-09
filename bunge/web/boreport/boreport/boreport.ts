import { Component, OnInit } from '@angular/core';
import { Http } from '@angular/http';
import { Router } from '@angular/router';
import { AuthHttp } from 'angular2-jwt';
import { contentHeaders } from '../common/headers';
import { User } from '../models/index';
import { Credit } from '../models/index';
import { Product } from '../models/index';
import { Evt } from '../models/index';
import { Transaction } from '../models/index';
import { Category } from '../models/index';
import { Config } from '../config/config';
import { Error } from '../models/index';

const styles = require('./boreport.css');
const template = require('./boreport.html');



@Component({
  selector: 'boreport',
  template: template,
  styles: [ styles ]
})
export class BOReport  {
  jwt: string;
  decodedJwt: string;
  response: string;
  api: string;
  user_id: string;  
  user:User;
  private event: Evt;  
  private categories: Category[] = [];
  private events_view: any[];  
  private events: any[];  
  private transactions: any[];  
  private inactives: any[];  
  private products: any[];
  private event_id:string;  
  private error:Error;
  private users: any[];  
  private laboral_bases: any[];  
  private selected_filter:string;
  
  constructor(public router: Router, public http: Http, public authHttp: AuthHttp) {      
	
    this.user_id = localStorage.getItem('id_usr');
    this.event_id = "1";//localStorage.getItem('boevent_id');
    this.decodedJwt = this.jwt && window.jwt_decode(this.jwt);       	
    this.transactions = [];
    this.laboral_bases = ['Paraguay', 'San Jerónimo Sud', 'Tancacha', 'Rosario', 'Puerto San Martín', 'Bahía Blanca', 'Ramallo', 'TFA', 'Buenos Aires', 'Chaco', 'Cayasta', 'Tucuman','Quequen', 'Piquete Cabado', 'Bandera', 'Campana', 'Tres Arroyos'];
    this._getUser();

  }



  logout() {
    localStorage.removeItem('id_usr');
    this.router.navigate(['login']);
  }


  callSecuredApi(url) {    
     this._callApi('Secured', url);
  }


  _getUser(){

    let user_id= this.user_id;
    let body = JSON.stringify({user_id});        
    this.authHttp.post(Config.API_ENDPOINT + 'userbyid', body, { headers: contentHeaders })
    .subscribe(
        response => {
          debugger;
          let user_response = response.json().user[0];            
          this.user = new User();
          this.user.id = user_response.id;
          this.user.file = user_response.file;
          this.user.is_admin = user_response.is_admin;

          if(this.user.is_admin){
            this._getEvent();        
            this.getInactivesUsers();            
          }                      
          },
          error => {
            alert(error.text());
          });     

  }

  _getEvent(){

    let evt_id= this.event_id;
    let body = JSON.stringify({event_id:evt_id});        
    this.authHttp.post(Config.API_ENDPOINT + 'eventbyid', body, { headers: contentHeaders })
    .subscribe(
        response => {
          let events_response = response.json().events;            
          events_response.forEach((event,index) =>{                        
              debugger;
              this.event = new Evt();
              this.event.id = event.id;
              this.event.start_date = event.start_date;
              this.event.end_date = event.end_date;
              this.event.description = event.description;                          
              this.event.banner_url = event.banner_url;
              
          });
         
          if(!events_response){            
            this.error = new Error();
            this.error.message = "no hay eventos";
          }
          else{
            this.populateCategories();              
          }},
          error => {
            this.error = new Error();
            this.error.message = error.text();
          });     

  }


  populateCategories(){

      let event_id = this.event_id.toString();
      let body = JSON.stringify({event_id});    
      this.categories = [];

      this.http.post(Config.API_ENDPOINT + 'categoriesbyevent', body, { headers: contentHeaders })
      .subscribe(
      response => {                                    
            debugger;
            let categoriesr = response.json().categories;          
            categoriesr.forEach((cat) => {                              

                let category_aux: Category = new Category();
                category_aux.id = cat.id;
                category_aux.description = cat.description;
                category_aux.event = this.event;
                this.categories.push(category_aux);                
              
            });
          
            if(!categoriesr){
              this.error = new Error();
              this.error.message = "evento sin categorias";
            }else{
              this.getProducts(); 
              
            }

          },
          error => {
            this.error = new Error();
            this.error.message = error.text();
            });              
    }



  getProducts(){
    
  var processed = 0;
  this.categories.forEach((category,index, array) =>{                 
      let category_id = category.id.toString();
      let body = JSON.stringify({category_id});    
      this.products = [];
      this.http.post(Config.API_ENDPOINT + 'productsbycategory', body, { headers: contentHeaders })
      .subscribe(
        response => {                        
          let productsr = response.json().products;          

        productsr.forEach((prod) => {                                    
          let product_aux: Product = new Product();
          product_aux.id = prod.id;
          product_aux.description = prod.description;
          product_aux.photo_url = prod.photo_url;
          product_aux.category_id = prod.category_id;
          product_aux.stock = prod.stock;  
          product_aux.title = prod.title;  
          this.products.push(product_aux);
        });

          

        },
          error => {
            this.error = new Error();
            this.error.message = error.text();
            }
        );
      
          processed++;
          if(processed === array.length) {
               this._getUsers();              
          }

      });          

    }


  _getUsers(){
    
    this.users=[];
    let body = JSON.stringify({});        
    this.authHttp.post(Config.API_ENDPOINT + 'users', body, { headers: contentHeaders })
    .subscribe(
        response => {
          
          let user_response = response.json().users;            
          user_response.forEach((usr,index) =>{                      
      

              var user_aux: User = new User();
              user_aux.id = usr.id;
              user_aux.file = usr.file;
              user_aux.email = usr.email;
              user_aux.laboral_base = usr.laboral_base;                          
              user_aux.is_admin = usr.is_admin;
              this.users.push(user_aux);                          
              
          });
         
          if(!user_response){
            alert("no hay usuarios");  
          }else{
            this.getAllTransactions();
          }
          
          },
          error => {
            alert(error.text());
          });     

    }


  getAllTransactions(){
  
    this.transactions = [];
    let body = JSON.stringify({});    
      
      this.http.post(Config.API_ENDPOINT + 'transactions', body, { headers: contentHeaders })
      .subscribe(
      response => {          
            this.transactions= [];
            let transactionsr = response.json().transaction; 

            debugger;  
            if(transactionsr){
              transactionsr.forEach((tran) => {                                    
                  debugger
                  let transaction_aux: Transaction = new Transaction();
                  transaction_aux.id = tran.id;
                  transaction_aux.user_id = tran.user_id;
                  transaction_aux.product_id = tran.product_id;
                  transaction_aux.date = tran.date;                                            
                  transaction_aux.user = this.users.filter(item => 
                  item.id ===tran.user_id)[0];
                  transaction_aux.product = this.products.filter(item => 
                  item.id ===tran.product_id)[0];
                  this.transactions.push(transaction_aux);
                              
              });

                
            }                      
          },
          error => {

            this.error = new Error();
            this.error.message = error.text();
            
            });
    }



  getTransactions(filter){
  
    this.transactions = [];
    let body = JSON.stringify({});    
      
      this.http.post(Config.API_ENDPOINT + 'transactions', body, { headers: contentHeaders })
      .subscribe(
      response => {          
            this.transactions= [];
            let transactionsr = response.json().transaction; 

            if(transactionsr){
              transactionsr.forEach((tran) => {                                    
                
                  let transaction_aux: Transaction = new Transaction();
                  transaction_aux.id = tran.id;
                  transaction_aux.user_id = tran.user_id;
                  transaction_aux.product_id = tran.product_id;
                  transaction_aux.date = tran.date;                                            
                  transaction_aux.user = this.users.filter(item => 
                  item.id ===tran.user_id)[0];
                  transaction_aux.product = this.products.filter(item => 
                  item.id ===tran.product_id)[0];

                  if(transaction_aux.user.laboral_base == filter){
                    this.transactions.push(transaction_aux);  
                  }
                                                
              });

                
            }                      
          },
          error => {

            this.error = new Error();
            this.error.message = error.text();
            
            });
    }



  getInactivesUsers(){
    
    this.inactives=[];
    let event_id = this.event_id.toString();
    let body = JSON.stringify({event_id});        
    this.authHttp.post(Config.API_ENDPOINT + 'inactiveUsers', body, { headers: contentHeaders })
    .subscribe(
        response => {
          
          let users_response = response.json().users;            
          users_response.forEach((user,index) =>{                      

              var user_aux: User = new User();
              user_aux.id = user.id;
              user_aux.file = user.file;
              user_aux.laboral_base = user.laboral_base;
              user_aux.email = user.email;                                        
              this.inactives.push(user_aux);                          

          });
         
          if(!users_response){
            alert("no hay usuarios inactivos");  
          }
          
          },
          error => {
            alert(error.text());
          });     

    }


getFilterInactivesUsers(filter){
this.inactives=[];
    let event_id = this.event_id.toString();
    let body = JSON.stringify({event_id});        
    this.authHttp.post(Config.API_ENDPOINT + 'inactiveUsers', body, { headers: contentHeaders })
    .subscribe(
        response => {
          
          let users_response = response.json().users;            
          users_response.forEach((user,index) =>{                      

              var user_aux: User = new User();
              user_aux.id = user.id;
              user_aux.file = user.file;
              user_aux.laboral_base = user.laboral_base;
              user_aux.email = user.email; 
              if(user_aux.laboral_base == filter){
                this.inactives.push(user_aux);                            
              }                                       
              

          });
         
          if(!users_response){
            alert("no hay usuarios inactivos");  
          }
          
          },
          error => {
            alert(error.text());
          });     


}


onChange(val){
  debugger
  this.selected_filter = val;
}

filter(){
  debugger;

  this.transactions = [];
  this.getTransactions(this.selected_filter);
  this.getFilterInactivesUsers(this.selected_filter);

}

  _callApi(type, url) {
    
    this.response = null;
    if (type === 'Anonymous') {
      // For non-protected routes, just use Http
      this.http.get(url)
        .subscribe(
          response => this.response = response.text(),
          error => this.response = error.text()
        );
    }
    if (type === 'Secured') { 
      // For protected routes, use AuthHttp
      this.authHttp.get(url)
        .subscribe(
          response => this.response = response.text(),
          error => this.response = error.text()
        );
    }
  }



}
