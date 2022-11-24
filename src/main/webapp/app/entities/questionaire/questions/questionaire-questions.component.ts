import { Component, OnInit } from '@angular/core';
import { IQuestion } from '../../question/question.model';
import { QuestionService } from '../../question/service/question.service';
import { IAnswer } from '../../answer/answer.model';
import { AnswerService } from '../../answer/service/answer.service';
import { QuestionaireService } from '../service/questionaire.service';
import { IQuestionaire } from '../questionaire.model';

@Component({
  selector: 'jhi-questionaire-questions',
  templateUrl: './questionaire-questions.component.html',
})
export class QuestionaireQuestionsComponent implements OnInit {
  isSaving = false;
  questions?: IQuestion[] | null;
  answers?: IAnswer[] | null | undefined;
  questionaire?: IQuestionaire | null | undefined;
  answerAmount?: number | undefined;

  constructor(
    protected questionService: QuestionService,
    protected answerService: AnswerService,
    protected questionaireService: QuestionaireService
  ) {}

  ngOnInit(): void {
    this.answerService.query().subscribe(value => {
      this.answers = value.body;
    });
  }

  updateQuestionaireTotalRating(): void {
    let answer = null;
    this.answerService.query().subscribe(value => {
      this.answers = value.body;
      if (this.answers) {
        answer = this.answers[0];
        if (answer.questionare) {
          this.questionaireService.find(answer.questionare.id).subscribe(questionaire => {
            this.questionaire = questionaire.body;
            if (this.questionaire) {
              this.questionaire.totalRating = this.calculateTotalRating(this.answers);
              this.questionaireService.update(this.questionaire).subscribe(() => (this.isSaving = false));
            }
          });
        }
      }
    });
  }

  private calculateTotalRating(answers: any): number {
    let totalAddedNumbers = 0;
    let answerNotNullCounter = 0;
    if (answers) {
      for (let i = 0; i < answers.length; i++) {
        if (answers[i].rating) {
          answerNotNullCounter++;
          totalAddedNumbers += answers[i].rating;
        }
      }
      return Math.round((totalAddedNumbers / answerNotNullCounter) * 100) / 100;
    }
    return 0;
  }
}
