import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchSimpleFormComponent } from './search-simple-form.component';

describe('SearchSimpleFormComponent', () => {
  let component: SearchSimpleFormComponent;
  let fixture: ComponentFixture<SearchSimpleFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SearchSimpleFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchSimpleFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
