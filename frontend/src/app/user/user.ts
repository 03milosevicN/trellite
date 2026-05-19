import {Component, inject, OnInit, signal, WritableSignal} from "@angular/core";
import {UserService} from "../../services/user.service";
import {ActivatedRoute, RouterLink, RouterLinkActive, RouterOutlet} from "@angular/router";
import {UserModel} from "../../models/user.model";
import {LucideCalendarClock, LucideCog, LucideCreditCard, LucideUsers, LucideWalletCards} from "@lucide/angular";

@Component({
  selector: "app-user",
  imports: [
    LucideCog,
    LucideCreditCard,
    LucideWalletCards,
    LucideCalendarClock,
    LucideUsers,
    RouterLink,
    RouterOutlet,
    RouterLinkActive
  ],
  templateUrl: "./user.html",
  styleUrl: "./user.css",
})
export class User implements OnInit {

  protected userSignal: WritableSignal<UserModel | null> = signal<UserModel | null>(null);

  protected userId: string = '';

  private userService: UserService = inject(UserService);

  private activatedRoute: ActivatedRoute = inject(ActivatedRoute);


  ngOnInit(): void {
    this.userId = this.activatedRoute.snapshot.paramMap.get('userId')!;
    this.loadData();
  }


  loadData(): void {
    this.userService.getById(this.userId!).subscribe({
      next: data => {
        this.userSignal.set(data);
      },
      error: err => {
        console.log(err);
      }
    });
  }

}
