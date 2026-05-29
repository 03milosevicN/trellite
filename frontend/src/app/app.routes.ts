import { Routes } from '@angular/router';
import {Org} from "./org/org";
import {Header} from "./common/header/header";
import {User} from "./user/user";
import {Settings} from "./user/settings/settings";
import {Boards} from "./user/boards/boards";
import {Board as BoardComponent} from './board/board'
import {Activity} from "./user/activity/activity";
import {CardsInfo} from "./user/cards-info/cards-info";

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
    // TEMPORARY ROUTE
    {
        path: 'header/:orgId', component: Header
    }
];
