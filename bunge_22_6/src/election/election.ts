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
const styles = require('./election.css');
const template = require('./election.html');


@Component({
  selector: 'election',
  template: template,
  styles: [ styles ]
})
export class Election implements OnInit{
  jwt: string;
  decodedJwt: string;
  response: string;
  api: string;
  election_to_show: string;
  user_id: string;    
  private election: any[];  
  
  constructor(public router: Router, public http: Http, public authHttp: AuthHttp) {      

    this.user_id = localStorage.getItem('id_usr');
    this.decodedJwt = this.jwt && window.jwt_decode(this.jwt);       
  }

  ngOnInit(){    
    this.getElections();
  }


  logout() {
    localStorage.removeItem('id_usr');
    this.router.navigate(['login']);
  }


  callSecuredApi(url) {    
     this._callApi('Secured', url);
  }


  getElections(){

    this.election=[];
    let userid = this.user_id;
    let body = JSON.stringify({user_id:userid});

    this.authHttp.post('http://localhost:3001/transactionByUser', body, { headers: contentHeaders })
    .subscribe(
        response => {
          let election_response = response.json().transaction;            
          election_response.forEach((transaction,index) =>{                    
              var transaction_aux: Transaction = new Evt();
              transaction_aux.id = transaction.id;
              transaction_aux.date = transaction.date;
              transaction_aux.id_usr = transaction.usr_id;
              transaction_aux.Product = this.getProductById(transaction.product_id);
              this.election.push(transaction_aux);                          
          });
         
          if(!election_response){
            alert("no hay elecciones");  
          }
          
          },
          error => {
            alert(error.text());
          });     

    }


  getProductById(product_id:number){
    let result:Product;
    let body = JSON.stringify({product_id});        
    this.authHttp.post('http://localhost:3001/productById', body, { headers: contentHeaders })
    .subscribe(
        response => {
          let product_response = response.json().product;            

         
          if(!product_response){
            alert("no hay productos");  
          }
          else{

            result.id = product_response[0].id;
            result.description = product_response[0].description;             

          }
          
          },
          error => {
            alert(error.text());
          });     
      return result;
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
