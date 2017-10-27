import { Component, OnInit, Input } from '@angular/core';
import { Http, RequestOptions, Headers, ResponseContentType } from '@angular/http';
import * as FileSaver from 'file-saver'; 

@Component({
  selector: 'export-dropdown',
  templateUrl: './export-dropdown.component.html',
  styleUrls: ['./export-dropdown.component.css']
})

export class ExportDropdownComponent implements OnInit {

  @Input() datasetId: string;  

  baseUrl: string = "http://criceweb.com/uc_davis/src/export/downloaddataset";

  //an object holding the extension to its respective header
  EXT_HEADERS: Object = {
    xls: "application/vnd.ms-excel",
    xlsx: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
    csv: "text/csv",
    pdf: "application/pdf",
  };

  constructor(private http: Http) { }

  ngOnInit() {
  }

  //downloads the data set based on the desired file type
  download(fileType: string) {
    this.http.get(this.baseUrl + "/" + this.datasetId + "/" + fileType, {responseType: ResponseContentType.Blob})
      .subscribe(response => {
        let blob = new Blob([response.blob()], {type: this.EXT_HEADERS[fileType]});
        FileSaver.saveAs(blob, this.datasetId + "." + fileType);
      });
  }

}
