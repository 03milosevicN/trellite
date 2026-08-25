import {
  Component,
  computed,
  effect,
  ElementRef,
  inject,
  input,
  signal,
  viewChild,
} from '@angular/core';
import { ChatService } from './chat.service';
import { ChatWebSocketService } from './chat-ws.service';
import { AuthService } from '../auth/auth.service';
import { rxResource } from '@angular/core/rxjs-interop';
import { ChatMessageModel } from './message.model';

@Component({
  selector: 'app-chat-panel',
  imports: [],
  template: `
    <div class="flex flex-col h-full">
      <div class="flex items-center justify-between px-3 py-2 border-b border-gray-300">
        <span class="font-semibold text-sm">Board Chat</span>
        <span
          class="text-xs px-2 py-0.5 rounded-full"
          [class.bg-green-200]="chatWebSocketService.isConnected()"
          [class.bg-yellow-200]="chatWebSocketService.connectionState() === 'CONNECTING'"
          [class.bg-red-200]="chatWebSocketService.connectionState() === 'ERROR'"
        >
          {{ chatWebSocketService.connectionState() }}
        </span>
      </div>

      <div #scrollContainer class="flex-1 overflow-y-auto px-3 py-2 space-y-2">
        @if (messageHistoryResource.isLoading()) {
          <p class="text-sm text-gray-500">Loading messages...</p>
        }

        @for (message of displayedMessages(); track message.id) {
          <div
            class="max-w-[80%] rounded px-2 py-1 text-sm"
            [class.ml-auto]="isOwnMessage(message)"
            [class.bg-blue-100]="isOwnMessage(message)"
            [class.bg-gray-100]="!isOwnMessage(message)"
          >
            @if (!isOwnMessage(message)) {
              <p class="text-xs font-semibold text-gray-600">{{ message.senderName }}</p>
            }
            <p>{{ message.content }}</p>
            <p class="text-[10px] text-gray-400 text-right">
              {{ formatTime(message.sentAt) }}
            </p>
          </div>
        } @empty {
          @if (!messageHistoryResource.isLoading()) {
            <p class="text-sm text-gray-400">No messages yet. Say hello!</p>
          }
        }
      </div>

      <div class="flex gap-2 p-2 border-t border-gray-300">
        <input
          type="text"
          class="flex-1 border border-gray-300 rounded px-2 py-1 text-sm"
          placeholder="Type a message..."
          [value]="draftMessage()"
          (input)="draftMessage.set($any($event.target).value)"
          (keydown.enter)="send()"
          [disabled]="!chatWebSocketService.isConnected()"
        />
        <button
          class="bg-blue-500 text-white text-sm px-3 py-1 rounded disabled:opacity-50"
          [disabled]="!chatWebSocketService.isConnected() || draftMessage().trim() === ''"
          (click)="send()"
        >
          Send
        </button>
      </div>
    </div>
  `,
})
export class ChatPanel {
  readonly boardId = input.required<string>();

  protected chatService = inject(ChatService);
  protected chatWebSocketService = inject(ChatWebSocketService);
  private authService = inject(AuthService);

  private scrollContainer = viewChild<ElementRef<HTMLDivElement>>('scrollContainer');

  protected draftMessage = signal('');

  protected messageHistoryResource = rxResource({
    params: () => {
      const id = this.boardId();
      return id ? { boardId: id } : null;
    },
    stream: ({ params }) => this.chatService.getHistory(params?.boardId!),
  });

  private messageHistory = computed<ChatMessageModel[]>(() => {
    if (this.messageHistoryResource.error()) {
      console.error('rxResource-related error:', this.messageHistoryResource.error());
    }

    const err = this.messageHistoryResource.error();
    if (err) {
      console.warn(`Error related to messageHistory resource: ${err}`);
      return [];
    }

    if (this.messageHistoryResource.isLoading()) {
      console.info(`Loading history resource...`);
      return [];
    }

    const page = this.messageHistoryResource.value();
    return page ? [...page.content].reverse() : [];
  });

  protected displayedMessages = computed<ChatMessageModel[]>(() => {
    const combined = [...this.messageHistory(), ...this.chatWebSocketService.messagesState()];

    const deduped = new Map<string, ChatMessageModel>();
    for (const msg of combined) {
      deduped.set(msg.id, msg);
    }

    return Array.from(deduped.values()).sort(
      (a, b) => new Date(a.sentAt).getTime() - new Date(b.sentAt).getTime(),
    );
  });

  constructor() {
    effect((onCleanup) => {
      const id = this.boardId();
      if (id) {
        this.chatWebSocketService.subscribeToBoard(id);

        onCleanup(() => {
          this.chatWebSocketService.unsubscribeFromBoard();
        });
      }
    });

    effect(() => {
      this.displayedMessages();

      const container = this.scrollContainer()?.nativeElement;
      if (container) {
        setTimeout(() => (container.scrollTop = container.scrollHeight), 0);
      }
    });
  }

  protected isOwnMessage(message: ChatMessageModel): boolean {
    return message.senderId === this.authService.currentUserId();
  }

  protected formatTime(sentAt: string): string {
    return new Date(sentAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
  }

  protected send(): void {
    const content = this.draftMessage().trim();

    if (content === '') return;

    this.chatWebSocketService.sendMessage(this.boardId()!, content);

    this.draftMessage.set('');
  }

}
