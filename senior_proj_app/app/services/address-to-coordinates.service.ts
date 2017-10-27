import { Injectable } from '@angular/core';
import { MapsAPILoader } from '@agm/core';
import {} from '@types/googlemaps'; //so TypeScript can recognize the google namespace

@Injectable()
export class AddressToCoordinatesService {

  constructor(private apiLoader: MapsAPILoader) { }

  //Returns a geocoded lat/lng location as a promise for a given address
  geocodeAddress(address: string): Promise<google.maps.LatLng> {
    return new Promise((resolve, reject) => {
      this.apiLoader.load().then(() => {
        new google.maps.Geocoder().geocode({"address": address}, 
        (results, status) => {
          if (status == google.maps.GeocoderStatus.OK) {
            resolve(results[0].geometry.location);
          }
          else if (status == google.maps.GeocoderStatus.ERROR) {
            console.log("GEOCODER: ERROR");
          }
          else if (status == google.maps.GeocoderStatus.OVER_QUERY_LIMIT) {
            console.log("GEOCODER: OVER LIMIT");
          }
          else if (status == google.maps.GeocoderStatus.REQUEST_DENIED) {
            console.log("GEOCODER: REQUEST DENIED");
          }
          else if (status == google.maps.GeocoderStatus.INVALID_REQUEST) {
            console.log("GEOCODER: INVALID REQUEST");
          }
          else if (status == google.maps.GeocoderStatus.UNKNOWN_ERROR) {
            console.log("GEOCODER: INVALID REQUEST");
          }
          else if (status == google.maps.GeocoderStatus.ZERO_RESULTS) {
            console.log("geocoder: ZERO RESULTS");
          }
        });
      });
    });
  }

}
