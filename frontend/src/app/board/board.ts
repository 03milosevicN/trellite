import { Component } from "@angular/core";
import {Header} from "../common/header/header";
import {CardsSection} from "./cards-section/cards-section";
import {BoardsSection} from "./boards-section/boards-section";

@Component({
  selector: "app-board",
  imports: [
    Header,
    CardsSection,
    BoardsSection
  ],
  templateUrl: "./board.html",
  styleUrl: "./board.css",
})
export class Board {

}
