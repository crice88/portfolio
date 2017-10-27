import { Component, Input } from '@angular/core';

@Component({
  selector: 'dataset-featured',
  templateUrl: './dataset-featured.component.html',
  styleUrls: ['./dataset-featured.component.css']
})
export class DatasetFeaturedComponent {
  @Input() title: string;
  @Input() datasetName: string;

  private panelHeader: string = `BACON!!!`;
  private panelBody: string = `Bacon ipsum dolor amet burgdoggen cupim chuck tri-tip 
  boudin, picanha short ribs tail fatback ham hock meatball beef ribs filet mignon 
  strip steak. Rump pancetta jerky shankle turkey, short loin pork belly corned beef 
  tongue kevin landjaeger pork loin alcatra venison.`;
}
