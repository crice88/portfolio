import { Component } from '@angular/core';

import { ChartDataService } from '../services/chart-data.service';

import { Dataset } from '../dataset/dataset';

@Component({
  selector: 'data-catalog',
  templateUrl: './data-catalog.component.html',
  styleUrls: ['./data-catalog.component.css']
})
export class DataCatalogComponent {
  private datasets: Dataset[];

  constructor(private post: ChartDataService) {
    this.post.getDatasetInfo().subscribe(
      posts => { this.datasets = posts; }
    );
  }
}
