import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'about',
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.css']
})
export class AboutComponent {
  private aboutTitle: string = `About this project`;
  private aboutBody: string = `
      Bacon ipsum dolor amet tenderloin chuck flank doner. Bacon beef tongue tri-tip pancetta 
      shoulder chicken corned beef burgdoggen turkey ground round landjaeger. Shank turducken 
      pork corned beef tenderloin spare ribs brisket leberkas venison. Ham hock rump tenderloin meatball, 
      ball tip pig tongue short ribs venison meatloaf. Flank brisket hamburger ham, corned beef beef kevin jerky.
      Frankfurter kevin pork corned beef pig porchetta.
    `;
  private linksTitle: string = `Important links`;
  private contactTitle: string = `Contact us`;
}
