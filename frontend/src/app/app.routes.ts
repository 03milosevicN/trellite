import { Routes } from '@angular/router';
import {Org} from "./org/org";
import {Board} from "./board/board";
import {Header} from "./common/header/header";
import {User} from "./user/user";

export const routes: Routes = [
    {
        path: 'u/:userId', component: User
    },
    {
        path: 'orgs/:orgId', component: Org
    },
    {
        path: 'boards/:boardId', component: Board
    },
    // TEMPORARY ROUTES
    {
        path: 'header', component: Header
    }
];
