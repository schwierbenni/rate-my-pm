import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IQuestionaire } from '../questionaire.model';

@Component({
  selector: 'jhi-questionaire-detail',
  templateUrl: './questionaire-detail.component.html',
})
export class QuestionaireDetailComponent implements OnInit {
  questionaire: IQuestionaire | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questionaire }) => {
      this.questionaire = questionaire;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
