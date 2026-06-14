import { Component } from "@angular/core";
import {Header} from "../common/header/header";
import {CardsSection} from "./cards-section/cards-section";
import {ListsSection} from "./lists-section/lists-section";

@Component({
  selector: "app-board",
  imports: [
    Header,
    CardsSection,
    ListsSection
  ],
  templateUrl: "./board.html",
  styleUrl: "./board.css",
})
export class Board {

}
