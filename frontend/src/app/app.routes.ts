import { Routes } from '@angular/router';
import { Register } from './auth/register';
import { Login } from './auth/login';
import { User } from './user/user';
import { Org } from './org/org';
import { Board } from './board/board';

export const routes: Routes = [
  { path: 'register', component: Register },
  { path: 'login', component: Login },
  { path: 'u/:userId', component: User },
  { path: 'org/:orgId', component: Org },
  { path: 'b/:boardId', component: Board },
];
