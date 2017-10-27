import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'admin-home',
  templateUrl: './admin-home.component.html',
  styleUrls: ['./admin-home.component.css']
})
export class AdminHomeComponent implements OnInit {

  private title: string = "Admin";
  private uploadHeading: string = "Upload CSV, XLS, or XLSX";

  constructor() { }

  ngOnInit() {
  }

}
