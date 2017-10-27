import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DataCatalogLinkComponent } from './data-catalog-link.component';

describe('DataCatalogLinkComponent', () => {
  let component: DataCatalogLinkComponent;
  let fixture: ComponentFixture<DataCatalogLinkComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DataCatalogLinkComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DataCatalogLinkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
