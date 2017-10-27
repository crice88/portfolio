import { Component } from '@angular/core';

@Component({
  selector: 'index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.css']
})
export class IndexComponent {
  heading: string;
  intro: string;
  fillerTextP1: string;
  fillerTextP2: string;

  constructor() { 
    this.fillerTextP1 = `
      Bacon ipsum dolor amet shank ground round sausage ham, shankle boudin chicken frankfurter short loin. Brisket corned 
      beef tongue, pork t-bone cupim venison frankfurter kielbasa tenderloin bresaola swine jowl alcatra capicola. Leberkas 
      short ribs capicola, chuck pork loin tongue pork corned beef boudin salami cow rump kielbasa meatloaf. Short ribs 
      pancetta capicola chuck ground round, leberkas bacon salami drumstick. Tongue frankfurter short ribs shoulder turducken 
      ground round swine, burgdoggen tail meatloaf drumstick capicola pork belly rump. Ground round prosciutto 
      pork belly shankle ribeye turducken spare ribs flank. Venison ground round burgdoggen, t-bone jerky sirloin prosciutto.`;
    this.fillerTextP2 = `
      Landjaeger venison t-bone sausage. Sirloin hamburger ground round tri-tip pork chop sausage, doner picanha salami shank. 
      Meatloaf doner landjaeger bresaola rump tri-tip cow swine frankfurter chicken drumstick leberkas boudin cupim strip steak. 
      k chop short ribs prosciutto, t-bone pork belly pork corned beef capicola tongue. Bacon tri-tip venison cow, sirloin ham hock 
      capicola frankfurter prosciutto bresaola spare ribs turkey hamburger pastrami drumstick. Pork loin frankfurter shoulder 
      tenderloin sausage, venison sirloin ham prosciutto.`;
    this.heading = 'CSC 191 Web App';
    this.intro = 'Introduction to web app for UC Davis';
  }
}
