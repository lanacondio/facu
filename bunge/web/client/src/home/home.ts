import { Component, OnInit } from '@angular/core';
import { Http } from '@angular/http';
import { Router } from '@angular/router';
import { AuthHttp } from 'angular2-jwt';
import { contentHeaders } from '../common/headers';
import { User } from '../models/index';
import { Credit } from '../models/index';
import { Product } from '../models/index';

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
  products: any;
  credits: Credits[];
  
  constructor(public router: Router, public http: Http, public authHttp: AuthHttp) {  
    
    
    this.user_id = localStorage.getItem('id_usr');
    this.decodedJwt = this.jwt && window.jwt_decode(this.jwt);    
    //products: Products[] = [];
    //credits: Credits[] = [];  
  }


  ngOnInit(){
    this.viewCatalog();
  }


  logout() {
    localStorage.removeItem('id_usr');
    this.router.navigate(['login']);
  }

  viewCatalog() {
    this.response = null;
    this.products=  [];
    this.credits= [];   
    let user_id = localStorage.getItem('id_usr');
    let body = JSON.stringify({user_id});        
    this.authHttp.post('http://localhost:3001/credits', body, { headers: contentHeaders }) 
      .subscribe(  
        response => {
          let credits_response = response.json().credits;  
          debugger;       
          credits_response.forEach((credit,index) =>{
            debugger;       
            let credit_aux: Credit = new Credit();
            credit_aux.id = credit.id;
            credit_aux.user_id = credit.user_id;
            credit_aux.quantity = credit.quantity;
            credit_aux.category_id = credit.category_id;

            this.credits.push(credit_aux);

          });
          
          if(!credits_response){
            alert("el usuario no tiene más creditos");  
          }
          else{
            this._populateProducts();              
          }},
          error => {
            alert(error.text());
            console.log(error.text());
          }

        );    
      this.response = this.products;
  }

  callSecuredApi() {    
    debugger;
    this._callApi('Secured', 'http://localhost:3001/api/protected/random-quote');
  }

  _populateProducts(){
    
    this.credits.forEach((credit,index) =>{           
      debugger;
      let category_id = credit.category_id.toString();
      let body = JSON.stringify({category_id});    
      this.http.post('http://localhost:3001/productsbycategory', body, { headers: contentHeaders })
      .subscribe(
      response => {                
        debugger;
          let productsr = response.json().products;          
          productsr.forEach((prod) => {
                      
            let product_aux: Product = new Product();
            product_aux.id = prod.id;
            product_aux.description = prod.description;
            product_aux.photo_url = prod.photo_url;
            product_aux.category_id = prod.category_id;
            //product_aux.stock = product.stock;

            this.products.push(product_aux);
            });
          },
          error => {
            alert(error.text());
            console.log(error.text());
            });
              
      });      
 
  }

  _callApi(type, url) {
    debugger
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
