import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  footerText: string;
  logoUrl: string;

  constructor() { 
    this.footerText = 'Created By SB6 Development Team - 2017';
    this.logoUrl = '../assets/images/ucdavis_logo_gold.png';
  }
}
