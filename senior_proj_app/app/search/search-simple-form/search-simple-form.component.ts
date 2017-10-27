import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { Search } from '../search';

@Component({
  selector: 'search-simple-form',
  templateUrl: './search-simple-form.component.html',
  styleUrls: ['./search-simple-form.component.css']
})
export class SearchSimpleFormComponent {
  private search: Search;
  
  constructor(private router: Router) {
    this.search = new Search; 
  }

  searchCharts(): void {
    this.router.navigate(['/search', this.search.query]);
  }
}
