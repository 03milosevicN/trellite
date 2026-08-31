import {
  Component,
  computed,
  DestroyRef,
  effect,
  ElementRef,
  inject,
  input,
  signal,
  viewChild,
} from '@angular/core';
import { BoardService } from './board.service';
import { BoardListService } from './board-list.service';
import { CardService } from './card.service';
import { UserState } from '../states/user.state';
import { rxResource } from '@angular/core/rxjs-interop';
import {
  BoardListRequestModel,
  CardRequestModel,
  CardResponseModel,
  CardUpdateModel,
  ChecklistResponseModel,
} from './board.model';
import { OrgState } from '../states/org.state';
import { MemberService } from '../member/member.service';
import { ChatWebSocketService } from '../chat/chat-ws.service';
import { ChatPanel } from '../chat/chat-panel';
import { User } from '../user/user';
import { LucideCross, LucideDelete, LucidePlus } from '@lucide/angular';
import {
  CdkDrag,
  CdkDragDrop,
  CdkDropList,
  CdkDropListGroup,
  moveItemInArray,
  transferArrayItem,
} from '@angular/cdk/drag-drop';
import { BoardWebSocketService } from './board-ws.service';
import { Card } from '../card/card';

@Component({
  selector: 'app-board',
  imports: [
    ChatPanel,
    User,
    CdkDropListGroup,
    CdkDropList,
    CdkDrag,
    LucideCross,
    LucidePlus,
    Card,
    LucideDelete,
  ],
  template: `
    <div class="flex flex-col">
      <app-user class="block" [userId]="this.userState!.activeUserState()!.userId!.toString()" />
      <div
        class="flex items-center justify-between px-6 py-3 bg-base-100 border-b border-base-200 shadow-xs z-10"
      >
        @if (boardResource.isLoading()) {
          <div class="flex items-center gap-2">
            <span class="loading loading-spinner loading-sm text-primary"></span>
            <span class="text-sm text-base-content/60 font-medium">Loading board...</span>
          </div>
        } @else if (this.boardResource.value(); as board) {
          <div class="flex items-center gap-3">
            <h2 class="text-xl font-extrabold tracking-tight text-base-content">
              {{ board.title || 'Board' }}
            </h2>
          </div>
          <div class="flex items-center gap-2">
            @if (isOrgAdmin()) {
              <button
                type="button"
                class="btn btn-primary btn-sm gap-2"
                (click)="sendInviteModal.showModal()"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  class="h-4 w-4"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z"
                  />
                </svg>
                Invite to board
              </button>
              <dialog #sendInviteModal class="modal">
                <div class="modal-box max-w-md">
                  <h3 class="text-lg font-bold mb-4">Invite Member to Board</h3>

                  <form method="dialog" (submit)="sendInvite()">
                    @if (membersResource.value()) {
                      <div class="space-y-2 max-h-60 overflow-y-auto pr-1">
                        @for (
                          member of invitableBoardMembers();
                          track member.memberId ?? member.userResponse?.userId
                        ) {
                          <label
                            class="flex items-center gap-3 p-3 rounded-lg border border-base-200 cursor-pointer hover:bg-base-200/50 transition-colors has-checked:bg-primary/5"
                          >
                            <input
                              type="radio"
                              name="selectedMember"
                              class="radio radio-primary radio-sm"
                              [value]="member.userResponse.userId"
                              [checked]="selectedMemberId() === member.userResponse.userId"
                              (change)="selectedMemberId.set(member.userResponse.userId)"
                            />
                            <span class="label-text font-semibold text-sm">
                              {{ member.userResponse.firstName }} {{ member.userResponse.lastName }}
                            </span>
                          </label>
                        } @empty {
                          <div class="text-center py-6 text-sm text-base-content/60 italic">
                            No available members to invite.
                          </div>
                        }
                      </div>
                    } @else {
                      <div class="flex justify-center py-8">
                        <span class="loading loading-spinner text-primary loading-md"></span>
                      </div>
                    }
                    <div class="modal-action mt-6">
                      <button
                        type="submit"
                        class="btn btn-primary"
                        [disabled]="!selectedMemberId()"
                      >
                        Send
                      </button>
                      <button type="button" class="btn btn-ghost" (click)="sendInviteModal.close()">
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

            <button class="btn btn-primary btn-sm" (click)="selectedMemberToKickId.set(null); kickMemberModal.showModal()">
              <svg lucideDelete></svg>
              Kick member
            </button>
            <dialog #kickMemberModal class="modal">
              <div class="modal-box max-w-md">
                <h3 class="text-lg font-bold mb-4 text-error">Kick Member from Board</h3>

                <form
                  method="dialog"
                  (submit)="kickMember(selectedMemberToKickId()!); kickMemberModal.close()"
                >
                  @if (board.members && board.members.length > 0) {
                    <div class="space-y-2 max-h-60 overflow-y-auto pr-1">
                      @for (member of board.members; track member ?? $index) {
                        <label
                          class="flex items-center gap-3 p-3 rounded-lg border border-base-200 cursor-pointer hover:bg-error/5 transition-colors has-checked:bg-error/10"
                        >
                          <input
                            type="radio"
                            name="selectedMemberToKick"
                            class="radio radio-error radio-sm"
                            [value]="member"
                            [checked]="
                              selectedMemberToKickId() === member
                            "
                            (change)="
                              selectedMemberToKickId.set(member)
                            "
                          />
                          <span class="label-text font-semibold text-sm">
                            {{ member }}
                          </span>
                        </label>
                      }
                    </div>
                  } @else {
                    <div class="text-center py-6 text-sm text-base-content/60 italic">
                      No active members on this board.
                    </div>
                  }

                  <div class="modal-action mt-6">
                    <button
                      type="submit"
                      class="btn btn-error text-white"
                      [disabled]="!selectedMemberToKickId()"
                    >
                      Kick Member
                    </button>
                    <button type="button" class="btn btn-ghost" (click)="kickMemberModal.close()">
                      Cancel
                    </button>
                  </div>
                </form>
              </div>
              <form method="dialog" class="modal-backdrop">
                <button>close</button>
              </form>
            </dialog>

            <button type="button" class="btn btn-outline btn-sm gap-2" (click)="toggleChat()">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                class="h-4 w-4"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"
                />
              </svg>
              {{ chatOpened() ? 'Close Chat' : 'Open Chat' }}
            </button>
          </div>
          @if (chatOpened()) {
            <div
              class="fixed bottom-4 right-4 z-50 h-120 w-80 bg-base-100 border border-base-200 rounded-2xl shadow-2xl overflow-hidden flex flex-col"
            >
              <app-chat-panel [boardId]="this.boardId()" />
            </div>
          }
        }
      </div>
    </div>

    <main class="p-6 bg-base-200/50 min-h-[calc(100vh-65px)]">
      @if (boardResource.value(); as f) {
        <p class="mb-4 text-xs font-semibold uppercase tracking-wider text-base-content/60">
          Active board members:
          <span class="font-bold text-base-content">{{ f.members?.length ?? 0 }}</span>
        </p>
      }

      @if (boardResource.isLoading()) {
        <div class="flex justify-center p-12">
          <span class="loading loading-spinner loading-lg text-primary"></span>
        </div>
      } @else if (boardResource.value()) {
        <div cdkDropListGroup class="flex h-[calc(100vh-160px)] w-full gap-6 overflow-hidden">
          <aside
            class="flex w-1/3 flex-col rounded-xl border border-base-200 bg-base-100 p-4 shadow-sm"
          >
            <div class="flex justify-between items-center border-b border-base-200 pb-3">
              <h2 class="font-bold text-base tracking-tight">Backlog</h2>
              <svg
                lucidePlus
                class="w-5 h-5 cursor-pointer text-base-content/70 hover:text-primary transition-colors"
                (click)="createCardModal.showModal()"
              ></svg>

              <dialog #createCardModal class="modal">
                <div class="modal-box">
                  <h3 class="text-lg font-bold mb-4">Create New Card</h3>
                  <form
                    method="dialog"
                    (submit)="
                      createCard(cardTitle.value, cardDesc.value);
                      cardTitle.value = '';
                      cardDesc.value = ''
                    "
                    class="space-y-4"
                  >
                    <input
                      #cardTitle
                      class="input input-bordered w-full"
                      placeholder="Card title"
                      required
                    />
                    <input
                      #cardDesc
                      class="input input-bordered w-full"
                      placeholder="Card description"
                    />
                    <div class="modal-action">
                      <button type="submit" class="btn btn-primary">Create</button>
                      <button type="button" class="btn btn-ghost" (click)="createCardModal.close()">
                        Close
                      </button>
                    </div>
                  </form>
                </div>
                <form method="dialog" class="modal-backdrop">
                  <button>close</button>
                </form>
              </dialog>
            </div>

            @if (cardResource.isLoading()) {
              <div class="flex justify-center items-center flex-1">
                <span class="loading loading-spinner loading-md text-primary"></span>
              </div>
            } @else if (cardResource.value()) {
              <div
                cdkDropList
                [cdkDropListData]="backlogCards()"
                (cdkDropListDropped)="drop($event, null)"
                class="flex-1 overflow-y-auto mt-3 pr-1 min-h-25 space-y-2"
              >
                @for (card of backlogCards(); track card.id) {
                  <app-card
                    cdkDrag
                    [cdkDragData]="card"
                    [card]="card"
                    (openDetails)="openEditCardModal($event)"
                    (openDelete)="openDeleteCardModal($event)"
                  />
                } @empty {
                  <div
                    class="flex justify-center items-center h-32 border-2 border-dashed border-base-200 rounded-lg text-base-content/50 text-xs italic"
                  >
                    No cards in backlog
                  </div>
                }
              </div>
            }
          </aside>

          <section
            class="flex w-2/3 flex-col rounded-xl border border-base-200 bg-base-100 p-4 shadow-sm min-w-0"
          >
            <div
              class="flex justify-between items-center border-b border-base-200 pb-3 mb-4 shrink-0"
            >
              <h2 class="font-bold text-base tracking-tight">Lists</h2>
              <svg
                lucidePlus
                class="w-5 h-5 cursor-pointer text-base-content/70 hover:text-primary transition-colors"
                (click)="createListModal.showModal()"
              ></svg>

              <dialog #createListModal class="modal">
                <div class="modal-box">
                  <h3 class="text-lg font-bold mb-4">Create New List</h3>
                  <form
                    method="dialog"
                    (submit)="createList(listTitle.value); listTitle.value = ''"
                    class="space-y-4"
                  >
                    <input
                      #listTitle
                      class="input input-bordered w-full"
                      placeholder="List title"
                      required
                    />
                    <div class="modal-action">
                      <button type="submit" class="btn btn-primary">Create</button>
                      <button type="button" class="btn btn-ghost" (click)="createListModal.close()">
                        Close
                      </button>
                    </div>
                  </form>
                </div>
                <form method="dialog" class="modal-backdrop">
                  <button>close</button>
                </form>
              </dialog>
            </div>

            <div class="flex-1 flex gap-4 overflow-x-auto overflow-y-hidden items-start pb-2">
              @if (listResource.isLoading()) {
                <div class="flex justify-center items-center w-full h-full">
                  <span class="loading loading-spinner loading-lg text-primary"></span>
                </div>
              } @else if (listsWithCards().length > 0) {
                @for (list of listsWithCards(); track list.id || $index) {
                  <div
                    class="flex w-72 shrink-0 flex-col max-h-full rounded-xl border border-base-200 bg-base-200/50 p-3"
                  >
                    <div class="flex justify-between items-center mb-3 px-1">
                      <span
                        class="font-bold text-sm tracking-tight truncate max-w-50 text-base-content"
                        >{{ list.title }}</span
                      >
                      <svg
                        lucideCross
                        class="w-4 h-4 cursor-pointer text-base-content/40 hover:text-error transition-colors shrink-0"
                        (click)="deleteList(list.id)"
                      ></svg>
                    </div>

                    <div
                      cdkDropList
                      [cdkDropListData]="list.cards"
                      (cdkDropListDropped)="drop($event, list.id)"
                      class="flex-1 overflow-y-auto min-h-30 space-y-2 pr-1"
                    >
                      @for (card of list.cards; track card.id) {
                        <app-card
                          cdkDrag
                          [cdkDragData]="card"
                          [card]="card"
                          (openDetails)="openEditCardModal($event)"
                          (openDelete)="openDeleteCardModal($event)"
                        />
                      } @empty {
                        <div
                          class="flex justify-center items-center h-20 border border-dashed border-base-300 rounded-lg text-xs text-base-content/40 italic"
                        >
                          Drop cards here
                        </div>
                      }
                    </div>
                  </div>
                }
              } @else {
                <div
                  class="flex justify-center items-center w-full h-40 text-sm text-base-content/60 italic"
                >
                  No lists yet. Click '+' above to create your first list!
                </div>
              }
            </div>
          </section>
        </div>
      }
    </main>

    <dialog #updateCardModal class="modal">
      @if (selectedCardForEdit(); as activeCard) {
        <div class="modal-box max-w-lg">
          <h3 class="text-lg font-bold mb-4">Card Information & Edit</h3>
          <form
            method="dialog"
            (submit)="
              updateCard(
                activeCard.id,
                activeCard.boardListId || '',
                editTitle.value,
                editDesc.value,
                activeCard.assignees || [],
                activeCard.dueDate,
                activeCard.checklists || []
              );
              updateCardModal.close()
            "
            class="space-y-4"
          >
            <div class="form-control">
              <label class="label text-xs font-semibold">Title</label>
              <input
                #editTitle
                class="input input-bordered w-full text-sm"
                [value]="activeCard.title"
                required
              />
            </div>

            <div class="form-control">
              <label class="label text-xs font-semibold">Description</label>
              <textarea
                #editDesc
                class="textarea textarea-bordered w-full text-sm h-24"
                [value]="activeCard.desc"
              ></textarea>
            </div>

            <div class="modal-action">
              <button type="submit" class="btn btn-primary">Save Changes</button>
              <button type="button" class="btn btn-ghost" (click)="updateCardModal.close()">
                Cancel
              </button>
            </div>
          </form>
        </div>
      }
      <form method="dialog" class="modal-backdrop">
        <button>close</button>
      </form>
    </dialog>

    <dialog #deleteCardModal class="modal">
      @if (selectedCardForDelete(); as cardToDelete) {
        <div class="modal-box max-w-sm">
          <h3 class="text-lg font-bold text-error">Delete Card</h3>
          <p class="py-4 text-sm">
            Are you sure you want to delete card "<strong>{{ cardToDelete.title }}</strong
            >"? This action cannot be undone.
          </p>

          <div class="modal-action">
            <button
              type="button"
              class="btn btn-error text-white"
              (click)="deleteCard(cardToDelete.id); deleteCardModal.close()"
            >
              Delete
            </button>
            <button type="button" class="btn btn-ghost" (click)="deleteCardModal.close()">
              Cancel
            </button>
          </div>
        </div>
      }
      <form method="dialog" class="modal-backdrop">
        <button>close</button>
      </form>
    </dialog>
  `,
})
export class Board {
  boardService = inject(BoardService);
  listService = inject(BoardListService);
  cardService = inject(CardService);
  memberService = inject(MemberService);
  userState = inject(UserState);
  orgState = inject(OrgState);

  chatWebSocketService = inject(ChatWebSocketService);
  boardWebSocketService = inject(BoardWebSocketService);
  destroyRef = inject(DestroyRef);

  protected selectedCardForEdit = signal<CardResponseModel | null>(null);
  protected selectedCardForDelete = signal<CardResponseModel | null>(null);

  protected selectedMemberToKickId = signal<string | number | null>(null);

  chatOpened = signal(false);

  protected user = computed(() => this.userState.activeUserState());
  protected userId = computed(() => this.user()?.userId);

  protected org = computed(() => this.orgState.activeOrgState());
  protected orgId = computed(() => this.org()?.orgId);

  protected selectedMemberId = signal<number | null>(null);
  protected membersResource = rxResource({
    params: () => ({ orgId: this.orgId() }),
    stream: ({ params }) => this.memberService.getOrgMembers(params.orgId!),
  });

  protected boardId = input.required<string>();
  boardResource = rxResource({
    params: () => ({ boardId: this.boardId() }),
    stream: ({ params }) => this.boardService.getById(params.boardId),
  });

  listResource = rxResource({
    params: () => ({ boardId: this.boardId() }),
    stream: ({ params }) => this.listService.getByBoardId(params.boardId),
  });

  cardResource = rxResource({
    params: () => ({ boardId: this.boardId() }),
    stream: ({ params }) => this.cardService.getByBoardId(params.boardId),
  });

  protected backlogCards = computed(() => {
    const cards = this.cardResource.value() ?? [];
    return cards.filter((card) => !card.boardListId);
  });

  protected listsWithCards = computed(() => {
    const boardLists = this.listResource.value() ?? [];
    const cards = this.cardResource.value() ?? [];

    return boardLists.map((boardList: any) => {
      const rawId = boardList.id ?? boardList.boardListId ?? boardList._id;
      const listId = rawId ? String(rawId) : '';

      return {
        ...boardList,
        id: listId,
        boardListId: listId,
        cards: cards.filter((card: any) => String(card.boardListId) === listId),
      };
    });
  });

  protected invitableBoardMembers = computed(() => {
    const orgMembers = this.membersResource.value() ?? [];
    const currentBoard = this.boardResource.value();
    if (!currentBoard) return orgMembers;

    const existingBoardMemberIds = new Set<number>(
      Array.isArray(currentBoard.members)
        ? currentBoard.members.map((m: any) => (typeof m === 'object' ? m.userId : Number(m)))
        : [],
    );
    // Return only org members NOT yet on the board
    return orgMembers.filter((member) => !existingBoardMemberIds.has(member.userResponse.userId));
  });

  constructor() {
    effect(() => {
      const boardId = this.boardId();
      if (boardId) {
        //ws
        this.boardWebSocketService.connect(boardId);
        this.chatWebSocketService.connect(boardId);
      }
    });

    //ws
    effect(() => {
      const event = this.boardWebSocketService.latestEvent();
      if (event) {
        this.boardResource.reload();
        this.listResource.reload();
        this.cardResource.reload();
      }
    });

    this.destroyRef.onDestroy(() => {
      //websockets
      this.boardWebSocketService.disconnect();
      this.chatWebSocketService.disconnect();
    });
  }

  createCard(cardTitle: string, cardDesc: string = '') {
    const req: CardRequestModel = {
      boardId: this.boardId(),
      title: cardTitle,
      desc: cardDesc,
      assignees: [this.userId()!],
      checklists: [],
    };
    this.cardService.create(req).subscribe({});
  }

  updateCard(
    cardId: string,
    boardListId: string,
    cardTitle: string,
    cardDesc: string = '',
    assignees: number[] = [],
    dueDate: Date | undefined = undefined,
    checklists: ChecklistResponseModel[] = [],
  ) {
    const req: CardUpdateModel = {
      id: cardId,
      boardListId: boardListId,
      boardId: this.boardId(),
      title: cardTitle,
      desc: cardDesc,
      assignees: assignees,
      dueDate: dueDate,
      checklists: checklists,
    };
    this.cardService.update(cardId, req).subscribe({});
  }

  deleteCard(cardId: string) {
    this.cardService.delete(cardId).subscribe({});
  }

  createList(listTitle: string) {
    const req: BoardListRequestModel = {
      boardId: this.boardId(),
      title: listTitle,
      createdAt: new Date(),
    };
    this.listService.create(req).subscribe({});
  }

  deleteList(boardListId: string) {
    this.listService.delete(boardListId).subscribe({});
  }

  sendInvite() {
    const memberId: number | null = this.selectedMemberId();
    if (!memberId) return;

    this.boardService.assignToBoard(memberId.toString(), this.boardId()).subscribe({
      next: () => {
        this.selectedMemberId.set(null);
        this.membersResource.reload();
        this.boardResource.reload();
      },
    });
  }

  kickMember(memberId: string | number) {
    if (!memberId) return;
    this.boardService.leaveBoard(this.boardId(), memberId.toString()).subscribe({
      next: () => {
        this.selectedMemberId.set(null);
        alert(`Kicked member with ID of ${memberId}`);
        this.membersResource.reload();
        this.boardResource.reload();
      },
    });
  }

  toggleChat() {
    this.chatOpened.update((open) => !open);
  }

  isOrgAdmin() {
    return !!this.memberService.hasRole(this.orgId()?.toString()!, 'ADMIN');
  }

  drop(event: CdkDragDrop<CardResponseModel[]>, targetListId: string | null) {
    const draggedCard = event.item.data as CardResponseModel;
    if (!draggedCard) return;

    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex,
      );
      const updateRequest: CardUpdateModel = {
        id: draggedCard.id,
        boardId: this.boardId(),
        boardListId: targetListId ?? undefined,
        title: draggedCard.title,
        desc: draggedCard.desc,
        assignees: draggedCard.assignees ?? [],
        checklists: draggedCard.checklists ?? [],
      };
      this.cardService.update(draggedCard.id, updateRequest).subscribe({
        next: () => {
          this.boardResource.reload();
          this.listResource.reload();
          this.cardResource.reload();
        },
      });
    }
  }

  protected updateCardModal = viewChild<ElementRef<HTMLDialogElement>>('updateCardModal');
  openEditCardModal(card: CardResponseModel) {
    this.selectedCardForEdit.set(card);
    this.updateCardModal()?.nativeElement.showModal();
  }

  protected deleteCardModal = viewChild<ElementRef<HTMLDialogElement>>('deleteCardModal');
  openDeleteCardModal(card: CardResponseModel) {
    this.selectedCardForDelete.set(card);
    this.deleteCardModal()?.nativeElement.showModal();
  }
}
