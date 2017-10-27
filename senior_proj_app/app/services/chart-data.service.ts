import { Injectable } from '@angular/core';
import 'rxjs/add/operator/toPromise';


import { PostService } from './post.service';

@Injectable()
export class ChartDataService {
  private getTableUrl: string = 'http://www.criceweb.com/uc_davis/src/dataset/getdatatable/';
  private getOptionsUrl: string = 'http://www.criceweb.com/uc_davis/src/dataset/getoptions/';
  private getDatasetInfoUrl: string = 'http://criceweb.com/uc_davis/src/DatasetInfo/getdatasetinfo/';
  private getChartTypesUrl: string = 'http://criceweb.com/uc_davis/src/charttype/getcharttypes/';
  private getMapAddressesByIDURL: string = 'http://criceweb.com/uc_davis/src/map/getdatatablebyid/';
  private getMapAddressesByNameURL: string = 'http://criceweb.com/uc_davis/src/map/getdatatablebyname/';
  private getRawMapDataURL: string = 'http://criceweb.com/uc_davis/src/map/getrawdata/';

  constructor(private post: PostService) { }

  getChartTypes(datasetId: string) {
    return this.post.getJSON(
      this.getChartTypesUrl + datasetId
    );
  }

  getDatasetInfo(datasetId: string = '') {
    return this.post.getJSON(
      this.getDatasetInfoUrl + datasetId
    );
  }

  getDataTable(datasetId: string, chartType: string) {
    return this.post.getJSON(
      this.getTableUrl + datasetId + '/' + chartType
    );
  }

  getDataTableOptions(datasetId: string) {
    return this.post.getJSON(
      this.getOptionsUrl + datasetId
    );
  }

  getMapAddressesByID(datasetId: string) {
    return this.post.getJSON(
      this.getMapAddressesByIDURL + datasetId
    ).toPromise();
  }

  getMapAddressesByName(chartName: string) {
    return this.post.getJSON(
      this.getMapAddressesByNameURL + chartName
    ).toPromise();
  }

  getRawMapData(datasetId: string) {
    return this.post.getJSON(
      this.getRawMapDataURL + datasetId
    ).toPromise();
  }

}