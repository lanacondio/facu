import { Component, OnInit } from '@angular/core';
import { Http } from '@angular/http';
import { Router } from '@angular/router';
import { AuthHttp } from 'angular2-jwt';
import { contentHeaders } from '../common/headers';
import { User } from '../models/index';
import { Credit } from '../models/index';
import { Product } from '../models/index';
import { Evt } from '../models/index';
import { Category } from '../models/index';
import { Transaction } from '../models/index';
import { Select2Module } from 'ng2-select2';

const styles = require('./home.css');
const template = require('./home.html');


@Component({
  selector: 'home',
  template: template,
  styles: [ styles ]
})
export class Home implements OnInit{
  jwt: string;
  decodedJwt: string;
  response: string;
  api: string;
  catalog: string;
  user_id: string;
  private products: Product[];
  private transactions: Transaction[];
  private credits: Credit[];
  private event: Evt;
  private event_id:string;
  private categories: Category[] = [];
  private cart:Product[] = [];
  private user:User;
  private show_buy_button:boolean = false;
  private final_user:boolean = false;
  
  constructor(public router: Router, public http: Http, public authHttp: AuthHttp) {  
    
    this.user_id = localStorage.getItem('id_usr');
    this.event_id = localStorage.getItem('event_id');
    this.decodedJwt = this.jwt && window.jwt_decode(this.jwt);       
  }


  ngOnInit(){
    this.getEvent();
    this.getUser();
  }


  logout() {
    localStorage.removeItem('id_usr');
    this.router.navigate(['login']);
  }

  getUser(){

    let user_id= this.user_id;
    let body = JSON.stringify({user_id});        
    this.authHttp.post('http://localhost:3001/userbyid', body, { headers: contentHeaders })
    .subscribe(
        response => {
          let user_response = response.json().user[0];            
          this.user = new User();
          this.user.id = user_response.id;
          this.user.file = user_response.file;
          
         },
          error => {
            alert(error.text());
          });     



  }

  getEvent(){

    let evt_id= this.event_id;
    let body = JSON.stringify({event_id:evt_id});        
    this.authHttp.post('http://localhost:3001/eventbyid', body, { headers: contentHeaders })
    .subscribe(
        response => {
          let events_response = response.json().events;            
          events_response.forEach((event,index) =>{                        
              this.event = new Evt();
              this.event.id = event.id;
              this.event.start_date = event.start_date;
              this.event.end_date = event.end_date;
              this.event.description = event.description;                          
              this.event.banner_url = event.banner_url;
              
          });
         
          if(!events_response){
            alert("no hay eventos");  
          }
          else{
            this._populateCategories();              
          }},
          error => {
            alert(error.text());
          });     

  }


  viewCatalog() {    
    this.credits = [];    
    let user_id = localStorage.getItem('id_usr');
    let body = JSON.stringify({user_id});        

    this.authHttp.post('http://localhost:3001/credits', body, { headers: contentHeaders }) 
      .subscribe(  
        response => {
          let credits_response = response.json().credits;            
          credits_response.forEach((credit,index) =>{                        
              let credit_aux: Credit = new Credit();
              credit_aux.id = credit.id;
              credit_aux.user_id = credit.user_id;
              credit_aux.quantity = credit.quantity;
              credit_aux.category_id = credit.category_id;
              console.log(credit_aux.quantity);
            
              this.credits.push(credit_aux);  
                          
          });
          
          if(!credits_response){
            alert("el usuario no tiene más creditos");  
          }
          
          
            this._populateProducts();              
          },
          error => {
            alert(error.text());
            console.log(error.text());
          }

        );    
      
  }

  buy() {    
    
    this.cart.forEach((product) => { 

      debugger;
      let valid_credit : Credit;
      this.credits.forEach((credit, index)=>{
        if(credit.category_id === product.category_id){
          valid_credit = credit;
        }

      });

      if(!valid_credit){
        alert("no tiene creditos para este producto");
      }

      let body = JSON.stringify({ product_id: product.id, credit_id: valid_credit.id, user_id:this.user_id});        

      this.authHttp.post('http://localhost:3001/buyproductuser', body, { headers: contentHeaders }) 
      .subscribe(  
        response => {
          if(response){          
            console.log(response);
            this.viewCatalog(); 
          }else{
              alert("error en la compra");
          }

        },
        error => {
              alert(error.text());
              console.log(error.text());
            });
    });

    this.viewCatalog();

  }


  addToCart(product){
  
  let actual_category = this.categories.filter(item => 
          item.id == product.category_id)[0];

  if(actual_category.available_credit >0){
    this.cart.push(product);
    
    actual_category.available_credit -= 1;
    product.stock -=1;
    if(product.stock ==0){
      actual_category.available_products = 
      actual_category.available_products.filter(item=> item.id != product.id);
    }

    actual_category.selected_products.push(product);
    this.checkBuyButton();

  }

    
  }


  checkBuyButton(){
    let result = true;
    this.categories.forEach((category_aux,index) =>{                 
        debugger;
        if(category_aux.available_credit>0){
          result = false;
        }

      });
    this.show_buy_button = result;

  }

  removeFromCart(product){
    debugger
    var index = this.cart.indexOf(product);
    this.cart.splice(index, 1);
  
    let actual_category = this.categories.filter(item => 
          item.id == product.category_id)[0];

    actual_category.available_credit += 1;

    product.stock +=1;
    if(product.stock ==1){
      actual_category.available_products.push(product);
    }

    var nindex = actual_category.selected_products.indexOf(product);
    actual_category.selected_products.splice(nindex, 1);  

    this.show_buy_button=false;

  }


  callSecuredApi(url) {    
     this._callApi('Secured', url);
  }



_populateCategories(){

      let event_id = this.event.id.toString();
      let body = JSON.stringify({event_id});    
      this.categories = [];

      this.http.post('http://localhost:3001/categoriesbyevent', body, { headers: contentHeaders })
      .subscribe(
      response => {                                    
            let categoriesr = response.json().categories;          
            categoriesr.forEach((cat) => {                              

                let category_aux: Category = new Category();
                category_aux.id = cat.id;
                category_aux.description = cat.description;
                category_aux.event = this.event;
                this.categories.push(category_aux);                
              
            });
          
            if(!categoriesr){
              alert("evento sin categorias");  
            }else{
              this.viewCatalog(); 
            }

          },
          error => {
            alert(error.text());
            console.log(error.text());
            });              
  }

  


  getAvailableProductsByCategory(cat){

    let creditsInCategory :Credit[] = [];
    let productsInCategory :Product[] = [];    
    let result :Product[] = [];    
    
    this.credits.forEach((credit,index) =>{   
      if(credit.category_id == cat.id && credit.quantity>0){
        creditsInCategory.push(credit);
      }
    });       

    this.products.forEach((product,index) =>{   
      
      if(product.category_id == cat.id && product.stock>0){
        productsInCategory.push(product);
      }
    });

    if(creditsInCategory.length >0)
    {
      result = productsInCategory;
    }
    cat.available_products = result;
  }

  getTransactionsByCategory(cat){
    debugger  
    let transactionsInCategory :Transaction[] = [];
    let productsInCategory :Product[] = [];    
    let result :Product[] = [];    

    this.transactions.forEach((transaction,index) =>{   
      
      let product_aux:Product;
      this.products.forEach((prod,index) =>{           
        if(prod.id == transaction.product_id){
          product_aux = prod;
        }
      });

      if(product_aux && product_aux.category_id == cat.id){
        result.push(product_aux);  
      }
      
    });       

    cat.selected_products=result;

  }


  getAvailableCreditsByCategory(cat){
    let result = 0;
    this.credits.forEach((cred) => {              
      if(cred.category_id == cat.id){
        result += cred.quantity;
      }
      
    });
     cat.available_credit = result;
  }



  _showOpenEvents(){    
         localStorage.setItem('events_to_show', 'open_events');
          this.router.navigate(['events']);
  }

  _showCloseEvents(){
         localStorage.setItem('events_to_show', 'close_events');
          this.router.navigate(['events']);
  }


  _getTransactions(){
    this.transactions = [];
    let userid = this.user_id;
    let body = JSON.stringify({user_id : userid});    
      
      this.http.post('http://localhost:3001/transactionsbyuser', body, { headers: contentHeaders })
      .subscribe(
      response => {                        
            debugger
            this.transactions= [];
            let transactionsr = response.json().transaction; 
            if(transactionsr){
              transactionsr.forEach((tran) => {                                    
                  debugger
                  let transaction_aux: Transaction = new Transaction();
                  transaction_aux.id = tran.id;
                  transaction_aux.user_id = tran.user_id;
                  transaction_aux.product_id = tran.product_id;
                  transaction_aux.date = tran.date;                                            
                  this.transactions.push(transaction_aux);
                              
              });
                
            }
            
            this.refreshCategories();
          },
          error => {
            alert(error.text());
            console.log(error.text());
            });


  }


  refreshCategories(){
      this.categories.forEach((category_aux,index) =>{                 
        debugger;
        this.getAvailableProductsByCategory(category_aux);
        this.getTransactionsByCategory(category_aux);
        this.getAvailableCreditsByCategory(category_aux);                
      });

      this.categories = this.categories.filter(item => 
          item.available_products.length > 0 
          || item.selected_products.length > 0
          || item.available_credit > 0);

      this.checkFinalUser();

  }


  checkFinalUser(){
    let no_credits = true;
    this.credits.forEach((credit, index)=>{
      if(credit.quantity >0){
        no_credits = false;
      }

    });

      this.final_user = no_credits;



  }


  _populateProducts(){
    
    this.credits.forEach((credit,index) =>{                 
      
      let category_id = credit.category_id.toString();
      let body = JSON.stringify({category_id});    
      this.products = [];

      this.http.post('http://localhost:3001/productsbycategory', body, { headers: contentHeaders })
      .subscribe(
      response => {                        
            
            let productsr = response.json().products;          
            productsr.forEach((prod) => {                        
                debugger;        
                let product_aux: Product = new Product();
                product_aux.id = prod.id;
                product_aux.description = prod.description;
                product_aux.photo_url = prod.photo_url;
                product_aux.category_id = prod.category_id;
                product_aux.stock = prod.stock;  
                product_aux.title = prod.title;  
                              
                this.products.push(product_aux);
                            
            });
            this._getTransactions();

          },
          error => {
            alert(error.text());
            console.log(error.text());
            });
              
      });      
 
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
