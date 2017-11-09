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
const styles = require('./events.css');
const template = require('./events.html');


@Component({
  selector: 'events',
  template: template,
  styles: [ styles ]
})
export class Events implements OnInit{
  jwt: string;
  decodedJwt: string;
  response: string;
  api: string;
  user_id: string;  
  private events_view: any[];  
  private events: any[];  
  
  constructor(public router: Router, public http: Http, public authHttp: AuthHttp) {      
    this.user_id = localStorage.getItem('id_usr');
    this.decodedJwt = this.jwt && window.jwt_decode(this.jwt);       
  }

  ngOnInit(){    
    this.getEvents();
  }


  logout() {
    localStorage.removeItem('id_usr');
    this.router.navigate(['login']);
  }


  callSecuredApi(url) {    
     this._callApi('Secured', url);
  }


  getEvents(){
    
    this.events=[];
    let body = JSON.stringify({});        
    this.authHttp.post('http://localhost:3001/events', body, { headers: contentHeaders })
    .subscribe(
        response => {
          
          let events_response = response.json().events;            
          events_response.forEach((event,index) =>{          
            
              var event_aux: Evt = new Evt();
              event_aux.id = event.id;
              event_aux.start_date = event.start_date;
              event_aux.end_date = event.end_date;
              event_aux.description = event.description;                          
              event_aux.banner_url = event.banner_url;
              this.events.push(event_aux);                          
          });
         
          if(!events_response){
            alert("no hay eventos");  
          }
          
          },
          error => {
            alert(error.text());
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


  showEventHome(event){    

    localStorage.setItem('event_id', event.id);
    this.router.navigate(['home']);

  }


}
