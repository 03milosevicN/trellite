import { ComponentFixture, TestBed } from "@angular/core/testing";

import { ListsSection } from "./lists-section";

describe("ListsSection", () => {
  let component: ListsSection;
  let fixture: ComponentFixture<ListsSection>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListsSection],
    }).compileComponents();

    fixture = TestBed.createComponent(ListsSection);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
