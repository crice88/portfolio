import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { SearchSimpleFormComponent } from './search-simple-form/search-simple-form.component';
import { SearchFormComponent } from './search-form/search-form.component';
import { SearchComponent } from './search.component';
import { AppRoutingModule } from '../app-routing.module';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    AppRoutingModule
  ],
  declarations: [
    SearchSimpleFormComponent, 
    SearchFormComponent, 
    SearchComponent
  ],
  exports: [
    SearchSimpleFormComponent
  ]
})
export class SearchModule { }
