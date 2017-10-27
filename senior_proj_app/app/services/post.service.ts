import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import 'rxjs/add/operator/map';

@Injectable()
export class PostService {
  constructor(private http: Http) {
    console.log('PostService Initialized...');
   }

  getJSON(url: string) {
    return this.http.get(url).map(
      response => response.json()
    );
  }

  getText(url: string) {
    return this.http.get(url).map(
      response => response.text()
    );
  }
} 
