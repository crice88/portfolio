import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AppRoutingModule } from './app-routing.module';
import { SearchModule } from './search/search.module';
import { CarouselModule } from 'ngx-bootstrap/carousel';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';

import { ChartModule } from './charts/chart.module';
import { DatasetModule } from './dataset/dataset.module';
import { AdminModule } from './admin/admin.module';

import { PostService } from './services/post.service';
import { ChartDataService } from './services/chart-data.service';

import { AboutComponent } from './about/about.component';
import { AppComponent } from './app.component';
import { IndexComponent } from './index/index.component';
import { CarouselComponent } from './carousel/carousel.component';
import { DatasetComponent } from './dataset/dataset.component';
import { DatasetFeaturedComponent } from './dataset/dataset-featured/dataset-featured.component';
import { DataCatalogComponent } from './data-catalog/data-catalog.component';
import { DataCatalogLinkComponent } from './data-catalog/data-catalog-link/data-catalog-link.component';
import { ChartViewComponent } from './chart-view/chart-view.component';
import { ExportDropdownComponent } from './chart-view/export-dropdown/export-dropdown.component';
import { NotFoundComponent } from './shared/not-found/not-found.component';

@NgModule({
  declarations: [
    AppComponent,
    IndexComponent,
    AboutComponent,
    CarouselComponent,
    DataCatalogComponent,
    DataCatalogLinkComponent,
    ChartViewComponent,
    ExportDropdownComponent,
    NotFoundComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpModule,
    ChartModule,
    SearchModule,
    DatasetModule,
    CarouselModule.forRoot(),
    BsDropdownModule.forRoot(),
    AdminModule
  ],
  providers: [ PostService, ChartDataService ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }
