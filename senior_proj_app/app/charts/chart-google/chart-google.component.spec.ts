import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChartGoogleComponent } from './chart-google.component';

describe('ChartGoogleComponent', () => {
  let component: ChartGoogleComponent;
  let fixture: ComponentFixture<ChartGoogleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChartGoogleComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChartGoogleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
