import { Component} from '@angular/core';

import { Carousel } from './carousel';

@Component({
  selector: 'carousel-ucdavis',
  templateUrl: './carousel.component.html',
  styleUrls: ['./carousel.component.css']
})
export class CarouselComponent  {
  private carouselComponents: Carousel[] = new Array ({
      pictureSource: "../../assets/images/uc_davis.jpg",
      altText: "UC_Davis",
      title: "UC Davis Web App",
      body: "Introduction to UC Davis web app"
    }, {
      pictureSource: "../../assets/images/charts.png",
      altText: "Charts",
      title: "Cool charts",
      body: "Pretty cool charts"
    }, {
      pictureSource: "../../assets/images/students.jpg",
      altText: "About",
      title: "About Us",
      body: "Pretty cool students"
    });
}
