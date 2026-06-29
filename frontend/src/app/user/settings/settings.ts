import {Component, inject, signal, WritableSignal} from "@angular/core";
import {ActivatedRoute, Route, Router} from "@angular/router";
import {UserService} from "../../../services/user.service";
import {UserModel} from "../../../models/user.model"

@Component({
  selector: "app-settings",
  imports: [],
  templateUrl: "./settings.html",
  styleUrl: "./settings.css",
})
export class Settings {

  private route: ActivatedRoute = inject(ActivatedRoute);
  private userService: UserService = inject(UserService);

  private readonly routeUserId: string | null | undefined = null;
  protected user: WritableSignal<UserModel | null> = signal<UserModel | null>(null);

  constructor() {
    this.routeUserId = this.route.parent?.snapshot.paramMap.get('userId');
    this.loadData();
  }

  loadData(): void {
    this.userService.getById(this.routeUserId!).subscribe({
      next: data => {
        console.log('user data: ', data);
        this.user.set(data);
      },
      error: err => {
        console.log('Error loading data', err);
      }
    })
  }

}
