import { Component, computed, inject, input, signal } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { UserService } from './user.service';
import { RouterLink, RouterOutlet } from '@angular/router';
import { rxResource } from '@angular/core/rxjs-interop';
import { LucideMenu, LucideMoon, LucidePlus, LucideSun, LucideTrash2 } from '@lucide/angular';
import { OrgService } from '../org/org.service';
import { OrgState } from '../states/org.state';
import { OrgRequestModel } from '../org/org.model';
import { FormsModule } from '@angular/forms';
import { MemberService } from '../member/member.service';

@Component({
  selector: 'app-user',
  imports: [
    LucideMenu,
    LucideSun,
    LucideMoon,
    LucideTrash2,
    FormsModule,
    LucidePlus,
    RouterLink,
    RouterOutlet,
  ],
  template: `
    @if (userResource.isLoading()) {
      <span class="loading loading-spinner text-accent"></span>
    } @else if (userResource.value(); as user) {
      <div class="navbar bg-base-100 shadow-sm">
        <div class="flex-1">
          <details class="dropdown">
            <summary class="btn btn-ghost m-1"><svg lucideMenu></svg></summary>
            <ul class="menu dropdown-content bg-base-100 rounded-box z-1 w-52 p-2 shadow-sm">
              <li>
                <div class="flex items-center justify-between w-full">
                  <span class="text-lg font-semibold">Organizations</span>
                  <button
                    class="btn btn-ghost btn-sm btn-square"
                    (click)="orgDivOpened.set(!orgDivOpened())"
                  >
                    <svg lucidePlus></svg>
                  </button>
                </div>
                @if (orgDivOpened()) {
                  <div class="mt-2 p-3 bg-base-200 rounded-box flex flex-col gap-3 w-full">
                    <form
                      (ngSubmit)="
                        createOrg(orgTitle.value); orgTitle.value = ''; orgDivOpened.set(false)
                      "
                      class="flex flex-col gap-3"
                    >
                      <input
                        #orgTitle
                        class="input input-bordered w-full"
                        placeholder="Organization name"
                        autofocus
                      />
                      <div class="flex justify-end gap-2">
                        <button
                          type="button"
                          class="btn btn-ghost btn-sm"
                          (click)="orgDivOpened.set(false)"
                        >
                          Cancel
                        </button>
                        <button type="submit" class="btn btn-primary btn-sm">Create</button>
                      </div>
                    </form>
                  </div>
                }
              </li>
              <li>
                <div class="flex items-center justify-between w-full">
                  <span class="text-lg font-semibold">Memberships</span>
                  @if (membershipsResource.value(); as memberships) {
                    @for (membership of memberships; track membership.orgId) {
                      <span [routerLink]="['/u', userId(), 'orgs', membership.orgId]">{{ membership.name }}</span>
                    }
                  }
                </div>
              </li>
              @if (orgsResource.isLoading()) {
                <li><span class="loading loading-spinner text-accent"></span></li>
              } @else if (orgsResource.value(); as orgs) {
                @for (org of orgs; track org.orgId) {
                  <li class="flex-row justify-between items-center">
                    <span [routerLink]="['/u', userId(), 'orgs', org.orgId]">{{ org.name }}</span>
                    <button class="btn btn-ghost btn-xs text-error" (click)="deleteOrg(org.orgId)">
                      <svg lucideTrash2></svg>
                    </button>
                  </li>
                } @empty {
                  <li><p class="text-sm opacity-60">No organizations yet.</p></li>
                }
              }
            </ul>
          </details>
        </div>
        <div class="flex-none">
          <div class="dropdown dropdown-end">
            <div tabindex="0" role="button" class="btn btn-ghost btn-circle avatar">
              <div
                class="w-10 rounded-full flex justify-center align-middle"
                [style.background-color]="avatarColorUtil(user.firstName)"
              >
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
      <main class="m-5">
        <router-outlet />
      </main>
    }
  `,
})
export class User {
  private authService = inject(AuthService);
  private userService = inject(UserService);
  private orgService = inject(OrgService);
  private memberService = inject(MemberService);
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

  membershipsResource = rxResource({
    params: () => ({ userId: this.userId().toString() }),
    stream: ({ params }) => this.memberService.getUserMemberships(params.userId),
  });

  orgDivOpened = signal(false);

  createOrg(boardTitle: string) {
    const req: OrgRequestModel = {
      name: boardTitle,
      createdAt: new Date(),
    };
    this.orgService.create(req).subscribe({
      next: (data) => {
        this.orgState.setActiveOrg(data);
        this.orgsResource.reload();
        this.membershipsResource.reload();
      },
    });
  }

  deleteOrg(orgId: string) {
    this.orgService.delete(orgId).subscribe();
  }

  logout() {
    this.authService.logout();
  }

  avatarColorUtil(name: string) {
    const hex = Array.from(name)
      .map((c) => c.charCodeAt(0).toString(16))
      .join('');
    return `#${hex.slice(0, 6)}`;
  }
}
