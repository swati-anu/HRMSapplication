import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMasterLookup } from '../master-lookup.model';

@Component({
  selector: 'jhi-master-lookup-detail',
  templateUrl: './master-lookup-detail.component.html',
})
export class MasterLookupDetailComponent implements OnInit {
  masterLookup: IMasterLookup | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ masterLookup }) => {
      this.masterLookup = masterLookup;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
