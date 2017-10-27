import { Component, OnInit, Input } from '@angular/core';

import { Dataset } from '../../dataset/dataset';

@Component({
  selector: 'data-catalog-link',
  templateUrl: './data-catalog-link.component.html',
  styleUrls: ['./data-catalog-link.component.css']
})
export class DataCatalogLinkComponent implements OnInit {
  @Input() dataset: Dataset;

  private datasetUrl: string;
  private baseUrl: string = '/dataset/';

  ngOnInit() {
    this.datasetUrl = this.baseUrl + this.dataset.id;
  }
}
