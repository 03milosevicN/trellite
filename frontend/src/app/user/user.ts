import { Component, inject, input } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { UserService } from './user.service';
import { UserState } from '../states/user.state';
import { Router, RouterLink } from '@angular/router';
import { rxResource } from '@angular/core/rxjs-interop';
import { LucideHamburger, LucideMenu, LucideMoon, LucideSun, LucideTrash2 } from '@lucide/angular';
import { OrgService } from '../org/org.service';
import { OrgState } from '../states/org.state';
import { OrgRequestModel } from '../org/org.model';

@Component({
  selector: 'app-user',
  imports: [LucideMenu, LucideSun, LucideMoon, LucideTrash2],
  template: `
    @if (userResource.isLoading()) {
      <span class="loading loading-spinner text-accent"></span>
    } @else if (userResource.value(); as user) {
      <div class="navbar bg-base-100 shadow-sm">
        <div class="flex-1">
          <div class="dropdown">
            <button tabindex="0" class="btn btn-ghost"><svg lucideMenu></svg></button>
            <!--TODO: org modal menu-->

            <!--TODO: org modal menu-->
          </div>
        </div>
        <div class="flex-none">
          <div class="dropdown dropdown-end">
            <div tabindex="0" role="button" class="btn btn-ghost btn-circle avatar">
              <div class="w-10 rounded-full">
                <h1 class="text-xl">{{ user.firstName.charAt(0) }}</h1>
              </div>
            </div>
            <ul
              tabindex="-1"
              class="menu menu-sm dropdown-content bg-base-100 rounded-box z-1 mt-3 w-52 p-2 shadow"
            >
              <li>
                <p class="text-lg">{{ user.firstName }} {{ user.lastName }}</p>
              </li>
              <li>
                <p class="text-lg">{{ user.email }}</p>
              </li>
              <li>
                <button class="btn btn-ghost justify-start" (click)="logout()">Logout</button>
              </li>
              <li>
                <label class="btn btn-ghost w-full justify-start gap-3">
                  <div class="swap swap-rotate">
                    <input type="checkbox" class="theme-controller" value="dark" />
                    <svg class="swap-off h-5 w-5 fill-current" lucideSun></svg>
                    <svg class="swap-on h-5 w-5 fill-current" lucideMoon></svg>
                  </div>
                </label>
              </li>
            </ul>
          </div>
        </div>
      </div>
    }
  `,
})
export class User {
  private authService = inject(AuthService);
  private userService = inject(UserService);
  private orgService = inject(OrgService);
  private orgState = inject(OrgState);

  userId = input.required<string>();
  userResource = rxResource({
    params: () => ({ userId: this.userId().toString() }),
    stream: ({ params }) => this.userService.getById(params.userId),
  });

  orgsResource = rxResource({
    params: () => ({ userId: this.userId().toString() }),
    stream: ({ params }) => this.orgService.getByOwner(params.userId),
  });

  createOrg(boardTitle: string) {
    const req: OrgRequestModel = {
      name: boardTitle,
      createdAt: new Date(),
    };
    this.orgService.create(req).subscribe({
      next: (data) => {
        this.orgState.setActiveOrg(data);
        this.orgsResource.reload();
      },
    });
  }

  deleteOrg(orgId: string) {
    this.orgService.delete(orgId).subscribe();
  }

  logout() {
    this.authService.logout();
  }
}
