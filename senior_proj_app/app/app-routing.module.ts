import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { IndexComponent } from './index/index.component';
import { NotFoundComponent } from './shared/not-found/not-found.component';
import { AboutComponent } from './about/about.component';
import { ChartViewComponent } from './chart-view/chart-view.component';
import { SearchComponent } from './search/search.component';
import { DatasetComponent } from './dataset/dataset.component';
import { DataCatalogComponent } from './data-catalog/data-catalog.component';
import { AdminHomeComponent } from './admin/admin-home/admin-home.component';

const routes: Routes = [
    { path: '', redirectTo: '/index', pathMatch: 'full' },
    { path: 'index', component: IndexComponent },
    { path: 'about', component: AboutComponent },
    { path: 'admin', component: AdminHomeComponent },
    { path: 'browse', component: DataCatalogComponent },
    { path: 'dataset', component: DatasetComponent },
    { path: 'search/:query', component: SearchComponent },
    { path: 'dataset/:query', component: DatasetComponent },
    { path: 'view/:datasetId/:chartType', component: ChartViewComponent },
    { path: '404', component: NotFoundComponent },
    { path: '**', component: NotFoundComponent }
];

@NgModule({
    imports: [ RouterModule.forRoot(routes)],
    exports: [ RouterModule ]
})

export class AppRoutingModule { }