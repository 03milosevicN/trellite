import { ComponentFixture, TestBed } from "@angular/core/testing";

import { CardsSection } from "./cards-section";

describe("CardsSection", () => {
  let component: CardsSection;
  let fixture: ComponentFixture<CardsSection>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CardsSection],
    }).compileComponents();

    fixture = TestBed.createComponent(CardsSection);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
