import { ComponentFixture, TestBed } from "@angular/core/testing";

import { BoardsSection } from "./boards-section";

describe("BoardsSection", () => {
  let component: BoardsSection;
  let fixture: ComponentFixture<BoardsSection>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BoardsSection],
    }).compileComponents();

    fixture = TestBed.createComponent(BoardsSection);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
