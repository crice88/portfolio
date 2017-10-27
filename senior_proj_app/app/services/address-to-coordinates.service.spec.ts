import { TestBed, inject } from '@angular/core/testing';

import { AddressToCoordinatesService } from './address-to-coordinates.service';

describe('AddressToCoordinatesService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AddressToCoordinatesService]
    });
  });

  it('should be created', inject([AddressToCoordinatesService], (service: AddressToCoordinatesService) => {
    expect(service).toBeTruthy();
  }));
});
