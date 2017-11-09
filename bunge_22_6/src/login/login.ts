import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Http } from '@angular/http';
import {Md5} from 'ts-md5/dist/md5';
import { contentHeaders } from '../common/headers';

const styles   = require('./login.css');
const template = require('./login.html');

@Component({
  selector: 'login',
  template: template,
  styles: [ styles ]
})
export class Login {
  constructor(public router: Router, public http: Http) {
  }

  login(event, username, password) {
    event.preventDefault();
    password = Md5.hashStr(password);

    let body = JSON.stringify({ username, password });    
    this.http.post('http://localhost:3001/sessions/create', body, { headers: contentHeaders })
      .subscribe(        
        response => {        
          debugger;
          localStorage.setItem('id_token', response.json().id_token);
          localStorage.setItem('id_usr', response.json().id);
          this.router.navigate(['events']);
        },
        error => {
          alert(error.text());
          console.log(error.text());
        }
      );
  }

  signup(event) {
    event.preventDefault();
    this.router.navigate(['signup']);
  }
}
