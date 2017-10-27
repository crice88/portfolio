import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ChartDataService } from '../services/chart-data.service';

import { Dataset } from './dataset';
import { ChartType } from '../chart-type/chart-type';

@Component({
  selector: 'dataset',
  templateUrl: './dataset.component.html',
  styleUrls: ['./dataset.component.css']
})
export class DatasetComponent {
  public dataset: Dataset;

  private baseRouteUrl: string = "../../view/";
  private routeUrl: string;
  private datasetId: string;
  private chartTypes: ChartType[];

  constructor(private route: ActivatedRoute, private post: ChartDataService) {
    this.route.params.subscribe(
      params => { this.datasetId = params['query']; }
    );
    
    this.post.getChartTypes(this.datasetId).subscribe(
      post => { this.chartTypes = post; }
    );

    this.post.getDatasetInfo(this.datasetId).subscribe(
      post => { 
        this.dataset = post[0];
      }
    );
  }
}
