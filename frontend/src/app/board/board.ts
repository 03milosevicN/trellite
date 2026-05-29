import { Component } from "@angular/core";
import {Header} from "../common/header/header";

@Component({
  selector: "app-board",
    imports: [
        Header
    ],
  templateUrl: "./board.html",
  styleUrl: "./board.css",
})
export class Board {}
