import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { AdminHomeComponent } from './admin-home/admin-home.component';
import { AdminUploadComponent } from './admin-upload/admin-upload.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule
  ],
  declarations: [AdminHomeComponent, AdminUploadComponent]
})
export class AdminModule { }
