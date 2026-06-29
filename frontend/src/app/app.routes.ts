import { Routes } from '@angular/router';
import {Org} from "./org/org";
import {User} from "./user/user";
import {Settings} from "./user/settings/settings";
import {Boards} from "./user/boards/boards";
import {Board as BoardComponent} from './board/board'
import {Activity} from "./user/activity/activity";
import {CardsInfo} from "./user/cards-info/cards-info";
import {Home} from "./home/home";
import {Card} from "./common/card/card";
import {BoardList} from "./common/board-list/board-list";
import {Register} from "./auth/register/register";
import {Login} from "./auth/login/login";

export const routes: Routes = [
    {
        path: 'u/:userId', component: User,
        children: [
            { path: 'settings', component: Settings },
            { path: 'boards', component: Boards },
            { path: 'cards', component: CardsInfo },
            { path: 'activity', component: Activity },
        ]
    },
    {
        path: 'orgs/:orgId', component: Org,
    },
    {
        path: 'orgs/:orgId/boards/:boardId', component: BoardComponent,
    },
    {
        path: '',
        component: Home,
    },
    {
        path: 'auth/register', component: Register,
    },
    {
        path: 'auth/login', component: Login,
    },
    // TEMPORARY ROUTES
    {
      path: 'card-component', component: Card
    },
    {
        path: 'list-component', component: BoardList
    }
];
