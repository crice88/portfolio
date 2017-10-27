import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { Observable } from 'rxjs/Rx';

@Injectable()
export class UploadFileService {

  private postUrl: string = "http://www.criceweb.com/uc_davis/src/csv/insertdataset/";  

  constructor(private http: Http) { }

  result: string;

  /*
	Upload file method that simply uploads file, does not return a string

	JSON object is (currently) of the form 
	{
		base64,
		datasetName,
		description
	}
  */
  uploadFile(jsonObj: Object) {
	this.http.post(this.postUrl, JSON.stringify(jsonObj))
	.map(res => this.result = res.text())
	.catch(error => Observable.throw(error))
	.subscribe(
		data  => console.log(data),
		error => console.log(error)
	);

  }

  /*
	Upload file method so we can get the return string.
	Only purpose is to output text for debugging and testing at the moment.
  */
  getResultAsString(jsonObj: Object): Observable<string> {
	  return this.http.post(this.postUrl, JSON.stringify(jsonObj))
	  .map(res => {
		  return res.text()
		});
  }

}
