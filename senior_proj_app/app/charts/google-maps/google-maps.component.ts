import { Component, OnInit, Input } from '@angular/core';
import {} from '@types/googlemaps'; //so TypeScript can recognize the google namespace
//(install the above with npm install --save-dev @types/googlemaps)
import { MapsAPILoader} from '@agm/core';
import { ChartDataService } from '../../services/chart-data.service';

import { Dataset } from '../../dataset/dataset';

import { AddressLocationObject } from './addresslocationobject';

declare const MarkerClusterer;

@Component({
  selector: 'chart-map',
  templateUrl: './google-maps.component.html',
  styleUrls: ['./google-maps.component.css'],
  providers: [ChartDataService]
})
export class GoogleMapsComponent implements OnInit {

  @Input() dataset: Dataset;
  @Input() datasetId: string;  

  addresses: string[] = [];
  
  locations: google.maps.LatLng[] = [];

  fusionTableId: string = '1xdysxZ94uUFIit9eXmnw1fYc6VcQiXhceFd_CVKa'; //for drawing Solano county outline

  map: google.maps.Map; //holding the map object

  bounds: google.maps.LatLngBounds; //the boundaries of the displayed map

  showMarkers: boolean = true;

  locationPromises: Promise<google.maps.LatLng>[]; //holds the locationPromises for Geocoded addresses (waiting for latlng obj)

  datasetObjects: AddressLocationObject[]; //objects holding the address string and their respectiv lat's and lng's

  markers: google.maps.Marker[] = [];
  markerClusterer: any;
  isToggleMarkers: boolean = true;  

  heatMap: google.maps.visualization.HeatmapLayer;  
  

  constructor(private apiLoader: MapsAPILoader,
              private post: ChartDataService) {
     
  }

  ngOnInit() {

    //if the dataset is null, use a directly passed-in id - otherwise, use the dataset object's id (they're the same)
    this.post.getMapAddressesByID(this.dataset == null ? this.datasetId : this.dataset.id).then(posts => {
      this.datasetObjects = posts;   
    }).then(() => {
      console.log("datasetObjects" + this.datasetObjects);
      this.apiLoader.load().then(() => {
        for (let i = 0; i < this.datasetObjects.length; i++) {
          this.locations.push(new google.maps.LatLng(this.datasetObjects[i].Latitude, this.datasetObjects[i].Longitude));
          this.addresses.push(this.datasetObjects[i].Address);
        }
          this.bounds = new google.maps.LatLngBounds();
          for (let loc of this.locations) {
            this.bounds.extend(loc);
          }
      });
    });

  }

  //method used to save the map object upon loading
  //needed so certain map characteristics can be updated upon loading initial map
  //Note: just add methods to the body of this one to do more on-load map events that can't be handled by AGM
  public handleOnLoadTasks(map: google.maps.Map) {
    this.map = map;
    // this.drawSolanoOutline(); //requires the map instance, so drawing is done here
    this.addMarkerClusters();
    this.addHeatMap();
    // this.getHealthPlaces(); // use Google's Places Service to get health-related places - should remove in favor of specifically stored places in the database
  }

  //draws the outline of Solano county
  public drawSolanoOutline() {
    this.getLayer().then((layer) => {
      console.log("resolved fusion table layer");
      this.apiLoader.load().then(() => {
        layer.setOptions({
          styles: [{
            polygonOptions: {
              fillColor: '#00FF00',
              fillOpacity: 0.01, //value of 0.0 makes it completely opaque for some reason
              strokeColor: '#000000',
              strokeOpacity: 0.5,
              strokeWeight: 3
            },
          }]
        });
        layer.setMap(this.map);
      });
    });
  }

  //gets the fusion table layer for the Solano county outline geometry
  private getLayer(): Promise<google.maps.FusionTablesLayer> {
    return new Promise<google.maps.FusionTablesLayer>((resolve, reject) => {
      this.apiLoader.load().then(() => {
        resolve(new google.maps.FusionTablesLayer({
          clickable: false, //no popup boxes
          query: {
            select: '\'geometry\'',
            from: this.fusionTableId,
            where: "'State-County'='CA-Solano'"
          }
        }));
      });
    });
  }

  public addHeatMap() {
    console.log("addHeatMap() called");
    this.apiLoader.load().then(() => {
    // console.log("this.locations: " + this.locations);
    this.heatMap = new google.maps.visualization.HeatmapLayer({
      data: this.locations
    });
    this.heatMap.setMap(this.map);
    });

  }

  public addMarkerClusters() {
    const markerIcon = {
      url: '/assets/images/marker.png', // url
      scaledSize: new google.maps.Size(15, 15)
    };
    const style = {
      url: '/assets/images/cluster.png',
      height: 40,
      width: 40,
      textColor: '#FFF',
      textSize: 11,
      backgroundPosition: 'center center'
    };
    const options = {
      imagePath: '/assets/images/cluster',
      gridSize: 70,
      styles: [style, style, style],
      ignoreHidden: true,
      // minimumClusterSize: 5
    };

    let contentStringArray: any;
    let contentString: string = ''; //buffer for forming the content string for the info window
    let finalContentStrings: string[] = []; //array holding all final info window contents
    // let addrs: string[] = [];
    this.post.getRawMapData(this.dataset == null ? this.datasetId : this.dataset.id).then((content) => {
      contentStringArray = content;
      for (let i = 0; i < contentStringArray.length; i++) {
        contentString += '<div class="infoWindow">';
        for (let key in contentStringArray[i]) {
          contentString += key + ": " + contentStringArray[i][key] + '<br>';
        }
        contentString += '</div>';
        finalContentStrings.push(contentString);
        contentString = '';
        // addrs.push(contentStringArray[i].Address);
      }
    }).then(() => {
      this.apiLoader.load().then(() => {
        console.log("addMarkerCluster() called");
        
            for (let i = 0; i < this.locations.length; i++) {
              let infoWindow = new google.maps.InfoWindow({content: finalContentStrings[i]});
              let mrkr = new google.maps.Marker({position: this.locations[i], icon: markerIcon})
              mrkr.addListener('mouseover', () => {
                infoWindow.open(this.map, mrkr);
              });
              mrkr.addListener('mouseout', () => {
                infoWindow.close();
              });
              this.markers.push(mrkr);
            }
        console.log("this.markers = " + this.markers);
            this.markerClusterer = new MarkerClusterer(this.map, this.markers, options);
      });
    });
    
  }

  public toggleMarkers() {
    if (this.isToggleMarkers) {
      this.markerClusterer.clearMarkers();
      this.isToggleMarkers = !this.isToggleMarkers;
    }
    else {     
      this.markerClusterer.addMarkers(this.markers);
      this.isToggleMarkers = !this.isToggleMarkers;      
    }
  }

  isHeatMap: boolean = true;
  public toggleHeatMap() {
    this.heatMap.setMap(this.heatMap.getMap() ? null : this.map);
    this.isHeatMap = !this.isHeatMap;
  }

}
