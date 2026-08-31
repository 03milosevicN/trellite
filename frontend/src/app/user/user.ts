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
      <div class="flex items-center justify-center p-4">
        <span class="loading loading-spinner text-accent loading-md"></span>
      </div>
    } @else if (userResource.value(); as user) {
      <div class="navbar bg-base-100 shadow-sm border-b border-base-200 px-4">
        <div class="flex-1">
          <details class="dropdown">
            <summary class="btn btn-ghost btn-circle m-1">
              <svg lucideMenu class="w-5 h-5"></svg>
            </summary>
            <ul
              class="menu dropdown-content bg-base-100 rounded-box z-50 w-64 p-2 shadow-lg border border-base-200 gap-1"
            >
              <li>
                <div
                  class="flex items-center justify-between w-full hover:bg-transparent cursor-default py-1"
                >
                  <span class="text-xs font-bold uppercase tracking-wider text-base-content/60"
                    >Organizations</span
                  >
                  <button
                    type="button"
                    class="btn btn-ghost btn-xs btn-square"
                    (click)="orgDivOpened.set(!orgDivOpened())"
                  >
                    <svg lucidePlus class="w-4 h-4"></svg>
                  </button>
                </div>
                @if (orgDivOpened()) {
                  <div
                    class="mt-2 p-3 bg-base-200/50 border border-base-200 rounded-box flex flex-col gap-3 w-full"
                  >
                    <form
                      (ngSubmit)="
                        createOrg(orgTitle.value); orgTitle.value = ''; orgDivOpened.set(false)
                      "
                      class="flex flex-col gap-3"
                    >
                      <input
                        #orgTitle
                        class="input input-bordered input-sm w-full"
                        placeholder="Organization name"
                        autofocus
                      />
                      <div class="flex justify-end gap-2">
                        <button
                          type="button"
                          class="btn btn-ghost btn-xs"
                          (click)="orgDivOpened.set(false)"
                        >
                          Cancel
                        </button>
                        <button type="submit" class="btn btn-primary btn-xs">Create</button>
                      </div>
                    </form>
                  </div>
                }
              </li>
              <li>
                <div
                  class="flex items-center justify-between w-full hover:bg-transparent cursor-default py-1"
                >
                  <span class="text-xs font-bold uppercase tracking-wider text-base-content/60"
                    >Memberships</span
                  >
                  @if (membershipsResource.isLoading()) {
                    <span class="loading loading-spinner text-accent loading-xs"></span>
                  } @else if (membershipsResource.value(); as memberships) {
                    @for (membership of memberships; track membership.orgId) {
                      <span
                        class="cursor-pointer hover:underline text-sm font-medium"
                        [routerLink]="['/u', userId(), 'orgs', membership.orgId]"
                        >{{ membership.name }}</span
                      >
                    }
                  }
                </div>
              </li>
              @if (orgsResource.isLoading()) {
                <li class="p-2">
                  <span class="loading loading-spinner text-accent loading-xs"></span>
                </li>
              } @else if (orgsResource.value(); as orgs) {
                @for (org of orgs; track org.orgId) {
                  <li class="flex flex-row justify-between items-center rounded-btn">
                    <span
                      class="cursor-pointer text-sm font-medium"
                      [routerLink]="['/u', userId(), 'orgs', org.orgId]"
                      >{{ org.name }}</span
                    >
                    <button
                      type="button"
                      class="btn btn-ghost btn-xs text-error hover:bg-error/10"
                      (click)="deleteOrg(org.orgId)"
                    >
                      <svg lucideTrash2 class="w-4 h-4"></svg>
                    </button>
                  </li>
                } @empty {
                  <li>
                    <p class="text-xs text-base-content/50 italic py-2">No organizations yet.</p>
                  </li>
                }
              }
            </ul>
          </details>
        </div>
        <div class="flex-none">
          <div class="dropdown dropdown-end">
            <div tabindex="0" role="button" class="btn btn-ghost btn-circle avatar">
              <div
                class="w-10 h-10 rounded-full flex items-center justify-center font-bold text-white shadow-inner"
                [style.background-color]="avatarColorUtil(user.firstName)"
              >
                <h1 class="text-lg leading-none">{{ user.firstName.charAt(0) }}</h1>
              </div>
            </div>
            <ul
              tabindex="-1"
              class="menu menu-sm dropdown-content bg-base-100 rounded-box z-50 mt-3 w-56 p-2 shadow-lg border border-base-200 gap-1"
            >
              <li class="px-3 py-2 border-b border-base-200 pointer-events-none">
                <p class="text-sm font-bold text-base-content leading-tight">
                  {{ user.firstName }} {{ user.lastName }}
                </p>
                <p class="text-xs text-base-content/60 leading-tight truncate mt-0.5">
                  {{ user.email }}
                </p>
              </li>
              <li>
                <button
                  type="button"
                  class="btn btn-ghost btn-sm justify-start w-full text-sm"
                  (click)="logout()"
                >
                  Logout
                </button>
              </li>
            </ul>
          </div>
        </div>
      </div>
      <main class="max-w-7xl mx-auto p-6">
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
