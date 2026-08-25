import { Injectable, signal } from '@angular/core';
import { BoardResponseModel } from '../board/board.model';

@Injectable({
  providedIn: 'root'
})
export class BoardState {

  private activeBoardSignal = signal<BoardResponseModel | null>(this.getStoredBoard());
  readonly activeBoardState = this.activeBoardSignal.asReadonly();

  setActiveBoard(board: BoardResponseModel) {
    localStorage.setItem('ACTIVE_BOARD', JSON.stringify(board));
    this.activeBoardSignal.set(board);
  }

  clearState() {
    localStorage.removeItem('ACTIVE_BOARD');
    this.activeBoardSignal.set(null);
  }

  private getStoredBoard() {
    const data = localStorage.getItem('ACTIVE_BOARD');
    return data ? JSON.parse(data) : null;
  }

}
