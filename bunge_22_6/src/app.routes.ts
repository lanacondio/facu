import { Routes } from '@angular/router';
import { Home } from './home';
import { Events } from './events';
import { Election } from './election';
import { Login } from './login';
import { Signup } from './signup';

import { AuthGuard } from './common/auth.guard';

export const routes: Routes = [
  { path: '',       component: Login },
  { path: 'login',  component: Login },
  { path: 'signup', component: Signup },
  { path: 'home',   component: Home, canActivate: [AuthGuard] },
  { path: 'events',   component: Events, canActivate: [AuthGuard] },  
  { path: 'election',   component: Election, canActivate: [AuthGuard] },  
  { path: '**',     component: Login },
];
