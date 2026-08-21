import { Routes } from '@angular/router';
import { AutoListComponent } from './components/auto-list/auto-list';
import { AutoFormComponent } from './components/auto-form/auto-form';
import { LoginComponent } from './components/login/login';
import { RegisterComponent } from './components/register/register';
import { authGuard } from './auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: '', component: AutoListComponent, canActivate: [authGuard] },
  { path: 'add', component: AutoFormComponent, canActivate: [authGuard] },
  { path: 'edit/:id', component: AutoFormComponent, canActivate: [authGuard] },
  { path: '**', redirectTo: '' }
];
