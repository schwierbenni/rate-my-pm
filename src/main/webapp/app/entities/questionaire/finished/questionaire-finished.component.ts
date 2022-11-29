import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IQuestionaire } from '../questionaire.model';
import { QuestionaireService } from '../service/questionaire.service';

@Component({
  selector: 'jhi-questionaire-finished',
  templateUrl: './questionaire-finished.component.html',
})
export class QuestionaireFinishedComponent implements OnInit {
  questionaire: IQuestionaire | null = null;
  ratingVar: number | null | undefined = 0;

  constructor(protected activatedRoute: ActivatedRoute, protected questionaireService: QuestionaireService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questionaire }) => {
      this.questionaire = questionaire;
    });
  }

  displayTotalRatingByAverage(): void {
    let totalRatingVar: number | null | undefined = 0;
    if (typeof this.questionaire?.id === 'number') {
      this.questionaireService.find(this.questionaire.id).subscribe(value => {
        this.questionaire = value.body;
        totalRatingVar = this.questionaire?.totalRating;
        if (totalRatingVar !== null && totalRatingVar !== undefined) {
          if (totalRatingVar >= 1 || totalRatingVar <= 2.49) {
            this.ratingVar = 1;
          }
          if (totalRatingVar >= 2.5 && totalRatingVar <= 3.49) {
            this.ratingVar = 2;
          }
          if (totalRatingVar >= 3.5 && totalRatingVar <= 5) {
            this.ratingVar = 3;
          }
        }
      });
    }
  }
}
