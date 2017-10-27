import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { SearchSimpleFormComponent } from './search-simple-form/search-simple-form.component';

@Component({
  selector: 'search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  private subscription: any;
  private searchParam: string;
  private body: string = `Bacon ipsum dolor amet beef sirloin shank, tenderloin meatball picanha tongue. Pig venison andouille, 
    picanha cow ribeye spare ribs ham hock landjaeger jowl chuck bacon kevin short loin boudin. Ham hock chuck 
    hamburger sirloin. Boudin pork belly landjaeger andouille venison ribeye tongue alcatra biltong sausage jerky 
    short loin drumstick t-bone.

    Pork boudin doner frankfurter hamburger pork belly leberkas pig. Tri-tip capicola shoulder kevin, pork loin 
    corned beef short ribs pastrami fatback shankle short loin beef ribs. Ground round leberkas meatball burgdoggen 
    salami. Beef salami pork loin pork ground round fatback. Drumstick flank shankle beef corned beef kielbasa frankfurter 
    chuck hamburger meatloaf pork chop sirloin meatball.

    Chuck beef ribs leberkas shank. Pork tongue fatback swine tail pork chop, porchetta corned beef bacon ribeye ham hock pig 
    turkey burgdoggen. Capicola strip steak cow doner alcatra chuck. Salami turducken prosciutto brisket ball tip. Sausage 
    shankle tail swine chicken bacon fatback pancetta ham hock beef tongue shoulder prosciutto.

    Bacon ipsum dolor amet beef sirloin shank, tenderloin meatball picanha tongue. Pig venison andouille, 
    picanha cow ribeye spare ribs ham hock landjaeger jowl chuck bacon kevin short loin boudin. Ham hock chuck 
    hamburger sirloin. Boudin pork belly landjaeger andouille venison ribeye tongue alcatra biltong sausage jerky 
    short loin drumstick t-bone.

    Pork boudin doner frankfurter hamburger pork belly leberkas pig. Tri-tip capicola shoulder kevin, pork loin 
    corned beef short ribs pastrami fatback shankle short loin beef ribs. Ground round leberkas meatball burgdoggen 
    salami. Beef salami pork loin pork ground round fatback. Drumstick flank shankle beef corned beef kielbasa frankfurter 
    chuck hamburger meatloaf pork chop sirloin meatball.

    Chuck beef ribs leberkas shank. Pork tongue fatback swine tail pork chop, porchetta corned beef bacon ribeye ham hock pig 
    turkey burgdoggen. Capicola strip steak cow doner alcatra chuck. Salami turducken prosciutto brisket ball tip. Sausage 
    shankle tail swine chicken bacon fatback pancetta ham hock beef tongue shoulder prosciutto.`;

  constructor(private route: ActivatedRoute) { }
  
  ngOnInit(): void {
    this.subscription = this.route.params.subscribe(
      params => { this.searchParam = params['query']; }
    );
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
