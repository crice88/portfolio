import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { ChartModule } from '../charts/chart.module';

import { DatasetComponent } from './dataset.component';
import { DatasetFeaturedComponent } from './dataset-featured/dataset-featured.component';

@NgModule({
  imports: [
    CommonModule,
    ChartModule,
    RouterModule
  ],
  declarations: [ 
    DatasetFeaturedComponent, 
    DatasetComponent
  ]
})
export class DatasetModule { }
