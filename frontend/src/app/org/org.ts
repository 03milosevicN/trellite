import { Component } from "@angular/core";
import {RouterLink, RouterLinkActive, RouterOutlet} from "@angular/router";
import {LucideCalendarClock, LucideCreditCard, LucideUsers, LucideWalletCards} from "@lucide/angular";

@Component({
  selector: "app-org",
  imports: [
    RouterLink,
    RouterLinkActive,
    RouterOutlet,
    LucideUsers,
    LucideCreditCard,
    LucideWalletCards,
    LucideCalendarClock
  ],
  templateUrl: "./org.html",
  styleUrl: "./org.css",
})
export class Org {}
