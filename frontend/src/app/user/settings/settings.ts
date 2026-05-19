import {Component, inject, signal, WritableSignal} from "@angular/core";
import {Router} from "@angular/router";
import {UserService} from "../../../services/user.service";
import {UserModel} from "../../../models/user.model"

@Component({
  selector: "app-settings",
  imports: [],
  templateUrl: "./settings.html",
  styleUrl: "./settings.css",
})
export class Settings {

  private router: Router = inject(Router);
  private userService: UserService = inject(UserService);

  private routeUserId: string | null = null;
  protected user: WritableSignal<UserModel | null> = signal<UserModel | null>(null);

  constructor() {
    this.routeUserId = this.router.url[3];
    this.loadData();
  }


  loadData(): void {
    this.userService.getById(this.routeUserId!).subscribe({
      next: data => {
        this.user.set(data);
      },
      error: err => {
        console.log('Error loading data', err);
      }
    })
  }

}
