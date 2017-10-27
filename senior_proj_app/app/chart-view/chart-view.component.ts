import { Component, ViewChild, AfterViewInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { ChartGoogleComponent } from '../charts/chart-google/chart-google.component';
import * as FileSaver from 'file-saver'; 
import { ExportDropdownComponent } from './export-dropdown/export-dropdown.component';



@Component({
  selector: 'chart-view',
  templateUrl: './chart-view.component.html',
  styleUrls: ['./chart-view.component.css'],
})
export class ChartViewComponent implements AfterViewInit {
  private chartType: string;
  private datasetId: string;
  storedImage: Blob;
  

  constructor(private route: ActivatedRoute, private location: Location) { 
    this.route.params.subscribe(
      params => { 
        this.datasetId = params['datasetId'];
        this.chartType = params['chartType'];
      }
    );
  }

  @ViewChild(ChartGoogleComponent) private googleChart: ChartGoogleComponent; 

  ngAfterViewInit() {

  }

  saveImage() {
    this.storedImage = this.googleChart.triggerPNGCreation();
    FileSaver.saveAs(this.storedImage, "image.png");
  }

  back() {
    this.location.back();
  }

  openEditor() {
    this.googleChart.editChart();
  }

}
