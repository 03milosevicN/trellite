import { Component, computed, inject, input, signal } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';
import { OrgService } from './org.service';
import { DatePipe } from '@angular/common';
import { BoardService } from '../board/board.service';
import { Router } from '@angular/router';
import { LucidePlus, LucideUsers } from '@lucide/angular';
import { BoardRequestModel } from '../board/board.model';
import { BoardState } from '../states/board.state';
import { switchMap } from 'rxjs';
import { MemberService } from '../member/member.service';
import { UserService } from '../user/user.service';
import { JoinOrgRequestModel } from './org.model';
import { UserState } from '../states/user.state';


@Component({
  selector: 'app-org',
  imports: [DatePipe, LucidePlus, LucideUsers],
  template: `
    @if (orgResource.isLoading()) {
      <span class="loading loading-spinner text-accent"></span>
    } @else if (orgResource.value(); as org) {
      <div>
        <h1 class="text-2xl">{{ org.name }}</h1>
        <p>Created: {{ org.createdAt | date: 'mediumDate' }}</p>
        <p class="flex">
          <svg lucidePlus class="cursor-pointer" (click)="createBoardModal.showModal()"></svg>
          Create a board
        </p>
        <dialog #createBoardModal class="modal">
          <div class="modal-box">
            <form method="dialog" (submit)="createBoard(boardTitle.value); boardTitle.value = ''">
              <input #boardTitle class="input" placeholder="Board name" />
            </form>
          </div>
        </dialog>
      </div>
      <div>
        @if (usersResource.value()) {
          <p class="flex">
            <svg
              lucideUsers
              class="cursor-pointer transition-transform hover:scale-105"
              (click)="inviteToOrgModal.showModal()"
            ></svg>
            Invite to organization
          </p>
          <dialog #inviteToOrgModal class="modal">
            <div class="modal-box max-w-md">
              <h3 class="text-lg font-bold mb-4">Invite a member</h3>
              <form method="dialog" (submit)="inviteToOrg()">
                <div class="space-y-2 max-h-60 overflow-y-auto pr-1">
                  @for (user of invitableUsers(); track user.userId) {
                    <label
                      class="flex items-center gap-3 p-3 rounded-lg border cursor-pointer transition-colors hover:bg-base-200/50 has-checked:border-primary has-:checked:bg-primary/5"
                    >
                      <input
                        type="radio"
                        name="selectedMember"
                        class="radio radio-primary radio-sm"
                        [value]="user.userId"
                        [checked]="selectedUserId() === user.userId"
                        (change)="selectedUserId.set(user.userId)"
                      />
                      <span class="label-text font-semibold text-base">
                        {{ user.firstName }} {{ user.lastName }} {{ user.userId }}
                      </span>
                    </label>
                  }
                </div>
                <div class="modal-action">
                  <button type="submit" class="btn btn-primary" [disabled]="!selectedUserId()">
                    Invite
                  </button>
                  <button type="button" class="btn btn-ghost" (click)="inviteToOrgModal.close()">
                    Close
                  </button>
                </div>
              </form>
            </div>
            <form method="dialog" class="modal-backdrop">
              <button>close</button>
            </form>
          </dialog>
        }
      </div>
      <div>
        @if (membersResource.value(); as members) {
          <p>{{ members.length }} {{ members.length === 1 ? 'org. member' : 'org. members' }}</p>
        }
      </div>
      <div>
        <div class="flex justify-baseline align-center">
          <p class="text-lg">Boards</p>
        </div>
        @if (boardsResource.isLoading()) {
          <span class="loading loading-spinner text-accent"></span>
        } @else if (boardsResource.value(); as boards) {
          @for (board of boards; track board.id) {
            <div
              class="card bg-base-100 border-base-200 w-full max-w-sm border shadow-sm transition-all hover:shadow-md"
            >
              <div class="card-body p-5 gap-3">
                <div class="flex items-start justify-between gap-2">
                  <h2 class="card-title text-lg font-bold tracking-tight leading-snug">
                    {{ board.title }}
                  </h2>
                  <span
                    class="badge badge-sm shrink-0 {{
                      board.archived ? 'badge-ghost text-opacity-60' : 'badge-success badge-soft'
                    }}"
                  >
                    {{ board.archived ? 'Archived' : 'Active' }}
                  </span>
                </div>
                <div class="text-base-content/70 flex flex-col gap-1 text-xs">
                  <p class="flex items-center gap-1.5">
                    <span class="font-medium text-base-content/90">Created:</span>
                    <span>{{ board.createdAt | date: 'mediumDate' }}</span>
                  </p>
                  <p class="flex items-center gap-1.5">
                    <span class="font-medium text-base-content/90">Members:</span>
                    <span class="badge badge-xs badge-neutral">{{ board.members }}</span>
                  </p>
                </div>
                <div class="card-actions justify-end mt-1">
                  <div class="dropdown">
                    <button tabindex="0" class="btn btn-primary btn-xs">Actions</button>
                    <ul
                      tabindex="-1"
                      class="dropdown-content menu bg-base-100 rounded-box z-1 w-52 p-2 shadow-sm"
                    >
                      <li>
                        <a class="btn btn-ghost" (click)="goToBoard(board.id)"> Go to board </a>
                      </li>
                      <li>
                        <button class="btn btn-ghost" (click)="deleteBoard(board.id)">
                          Delete board
                        </button>
                      </li>
                      <li>
                        <button class="btn btn-ghost" (click)="archiveBoard(board.id)">
                          Archive board
                        </button>
                      </li>
                    </ul>
                  </div>
                </div>
              </div>
            </div>
          } @empty {
            <p>No boards yet.</p>
          }
        }
      </div>
    }
  `,
})
export class Org {
  orgService = inject(OrgService);
  boardService = inject(BoardService);
  memberService = inject(MemberService);
  userService = inject(UserService);
  userState = inject(UserState);
  boardState = inject(BoardState);
  router = inject(Router);

  orgId = input.required<string>();

  protected selectedUserId = signal<number | null>(null);
  protected currentUser = computed(() => this.userState.activeUserState());
  protected currentUserId = computed(() => {
    const curr = this.currentUser();
    if (curr) {
      return curr.userId;
    }
    return null;
  })

  protected membersResource = rxResource({
    params: () => ({ orgId: this.orgId() }),
    stream: ({ params }) => this.memberService.getOrgMembers(params.orgId),
  });

  orgResource = rxResource({
    params: () => ({ orgId: this.orgId() }),
    stream: ({ params }) => this.orgService.getById(params.orgId),
  });

  boardsResource = rxResource({
    params: () => ({ orgId: this.orgId() }),
    stream: ({ params }) => this.boardService.getByOwningOrgId(params.orgId),
  });

  usersResource = rxResource({
    params: () => ({}),
    stream: ({}) => this.userService.getAll(),
  });

  invitableUsers = computed(() => {
    const users = this.usersResource.value() ?? [];
    const current: number | null = this.currentUserId();
    if (!current) return users;
    return users.filter(user => user.userId !== current);
  });

  createBoard(boardTitle: string) {
    if (!boardTitle.trim()) return;
    const req: BoardRequestModel = {
      orgId: Number(this.orgId()),
      title: boardTitle,
      createdAt: new Date(),
      archived: false,
      members: [],
    };
    this.boardService.create(req).subscribe({
      next: (data) => {
        this.boardState.setActiveBoard(data);
        this.boardsResource.reload();
      },
    });
  }

  goToBoard(boardId: string) {
    void this.router.navigate([`/b/${boardId}`]);
  }

  deleteBoard(boardId: string) {
    this.boardService.delete(boardId).subscribe({});
  }

  archiveBoard(boardId: string) {
    this.boardService
      .getById(boardId)
      .pipe(
        switchMap((board) =>
          this.boardService.update(boardId, {
            ...board,
            archived: true,
          }),
        ),
      )
      .subscribe({
        next: () => {
          alert('Board archived.');
        },
      });
  }

  inviteToOrg() {
    const userId = this.selectedUserId();
    if (userId) {
      const req: JoinOrgRequestModel = {
        orgId: this.orgId(),
        userId: userId.toString(),
      };
      this.orgService.join(req).subscribe({
        next: () => {
          this.orgResource.reload();
          this.boardsResource.reload();
          this.membersResource.reload();
          alert(`User with ID of ${req.userId} joined organization.`);
        },
      });
    }
  }
}
