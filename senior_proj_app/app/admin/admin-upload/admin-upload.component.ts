import { Component } from '@angular/core';

import { UploadFileService } from '../../services/upload-file.service'

@Component({
  selector: 'admin-upload',
  templateUrl: './admin-upload.component.html',
  styleUrls: ['./admin-upload.component.css'],
  providers: [UploadFileService]
})
export class AdminUploadComponent {
  private initialValue: string = "Enter dataset name";
  private result: string;
  private file: string;

  private fileName: string;

  private missingField: boolean = false;

  constructor(private uploadService: UploadFileService) { }

  // Set file list on event emission.
  fileUpload(event: any) {
    this.encodeFile(event.target);
  }

  encodeFile(value: any) {
    var file: File = value.files[0];
    var reader: FileReader = new FileReader();
    
    reader.onloadend = (e: Event) => {
      this.file = reader.result;
      this.fileName = file.name;
    }

    reader.readAsDataURL(file);
  }

  // Post file list to URL
  submitFile(tableName: string, description: string, title: string) {
    if (this.file == null) {
      console.log('error uploading');
      return;
    }

    this.missingField = false;
    if (!tableName || !description || !title) {
      this.missingField = true;
      return;
    }

    let ext: string = this.fileName.split('.').pop(); //should be csv, xls, or xlsx
    if (ext != <string>"csv" && ext != <string>"xls" && ext != <string>"xlsx") { //have to cast to strings apparently
      console.log("extension: " + ext);
      return;
    }
    // TODO: Abstract out into an object.
    let jsonObj = {
      base64: this.file.substr(this.file.indexOf(',') +1),
      tableName: tableName,
      description: description,
      title: title,
      ext: ext
    };

    this.uploadService.uploadFile(jsonObj);

    /*
    This method reuploads the file so we can get it as a string to output on the screen
    for debugging purposes.  Should get rid of later.
    */
    this.uploadService.getResultAsString(jsonObj).subscribe(data => this.result = data);
    /*
    */

  }
}
