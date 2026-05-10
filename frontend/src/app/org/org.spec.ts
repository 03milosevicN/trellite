import { ComponentFixture, TestBed } from "@angular/core/testing";

import { Org } from "./org";

describe("Org", () => {
  let component: Org;
  let fixture: ComponentFixture<Org>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Org],
    }).compileComponents();

    fixture = TestBed.createComponent(Org);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
