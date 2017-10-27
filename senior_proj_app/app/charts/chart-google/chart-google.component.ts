import { Component, OnInit, Input, Output, EventEmitter, AfterViewInit, ViewChild } from '@angular/core';

import { ChartDataService } from '../../services/chart-data.service';

import { Dataset } from '../../dataset/dataset';

import { GoogleChart } from '../google-chart-directive';

@Component({
  selector: 'chart-google',
  templateUrl: './chart-google.component.html',
  styleUrls: ['./chart-google.component.css'],
})
export class ChartGoogleComponent implements OnInit, AfterViewInit {
  @Input() dataset: Dataset;
  @Input() datasetId: string;
  @Input() chartType: string;
  @Input() enableStaticImage: boolean = false;
  @Input() enableControls: boolean = false;
  private result: string;
  public chartData: Object;
  public chartOptions: Object;

  public savedImage: Blob;

  public chartTypeMap: Object = {
    BarChart: "BarChart",
    PieChart: "PieChart",
    HistogramChart: "Histogram",
    LineChart: "LineChart",
    TableChart: "Table"
  };

  public realChartType: string;

  public controlWrapperDivIds: string[] = [];

  constructor(private post: ChartDataService) {
    this.dataset = new Dataset();
  }

  @ViewChild(GoogleChart) private googleChart: GoogleChart;

  ngOnInit() {
    if (this.datasetId != null) { 
      this.dataset.id = this.datasetId;
    }

    this.realChartType = this.chartTypeMap[this.chartType];

    this.post.getDataTableOptions(this.dataset.id).subscribe(posts => {
      this.chartOptions = posts;
    });
    
    this.post.getDataTable(this.dataset.id, this.chartType).subscribe(posts => {
      this.chartData = posts;
      //generate div ids for the controlWrappers (sliders)
      for (let i = 0; i < this.chartData["cols"].length; i++) {
        this.controlWrapperDivIds.push("controlWrapper" + i);
      }
    });
  }

  ngAfterViewInit() {
    
  }

  triggerPNGCreation(): Blob {
    return this.googleChart.getPNG();
  }

  editChart() {
    this.googleChart.openEditor();
  }

}
