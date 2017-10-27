import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';

// import { GoogleChart } from '../../../node_modules/angular2-google-chart/directives/angular2-google-chart.directive';
import { GoogleChart } from './google-chart-directive';

import { GoogleMapsComponent } from './google-maps/google-maps.component';

import { AgmCoreModule } from '@agm/core';
import { MarkerClusterDirective } from './google-maps/marker-cluster';
import { ChartGoogleComponent } from './chart-google/chart-google.component';

@NgModule({
  imports: [
    CommonModule,
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyCiCJHpZLHTYml1CZqEVzGX2v7ZMlYj-nY', //api key for google maps api, etc.
      libraries: ["places", "visualization"]
    }),
    BsDropdownModule.forRoot()
  ],
  declarations: [
    GoogleChart,
    GoogleMapsComponent,
    MarkerClusterDirective,
    ChartGoogleComponent
  ],
  exports: [
    GoogleMapsComponent,
    ChartGoogleComponent
  ]
})
export class ChartModule { }
