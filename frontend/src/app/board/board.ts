import { Component } from "@angular/core";
import {Header} from "../common/header/header";
import {CardsSection} from "./cards-section/cards-section";
import {ListsSection} from "./lists-section/lists-section";
import {CdkDropListGroup} from "@angular/cdk/drag-drop";

@Component({
  selector: "app-board",
  imports: [
    Header,
    CardsSection,
    ListsSection,
    CdkDropListGroup
  ],
  templateUrl: "./board.html",
  styleUrl: "./board.css",
})
export class Board {

}
