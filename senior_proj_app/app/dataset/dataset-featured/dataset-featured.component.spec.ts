import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatasetFeaturedComponent } from './dataset-featured.component';

describe('DatasetFeaturedComponent', () => {
  let component: DatasetFeaturedComponent;
  let fixture: ComponentFixture<DatasetFeaturedComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatasetFeaturedComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatasetFeaturedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
