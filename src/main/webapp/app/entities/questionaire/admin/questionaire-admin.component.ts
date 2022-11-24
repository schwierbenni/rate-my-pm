import { Component, OnInit } from '@angular/core';
import { QuestionService } from '../../question/service/question.service';
import { IAnswer } from '../../answer/answer.model';
import { AnswerService } from '../../answer/service/answer.service';
import { QuestionaireService } from '../service/questionaire.service';
import { IQuestionaire } from '../questionaire.model';

@Component({
  selector: 'jhi-questionaire-admin',
  templateUrl: './questionaire-admin.component.html',
})
export class QuestionaireAdminComponent implements OnInit {
  isSaving = false;
  answers?: IAnswer[] | null | undefined;
  questionaires?: IQuestionaire[] | null | undefined;

  constructor(
    protected questionService: QuestionService,
    protected answerService: AnswerService,
    protected questionaireService: QuestionaireService
  ) {}

  ngOnInit(): void {
    this.questionaireService.query().subscribe(value => {
      this.questionaires = value.body;
    });
    this.answerService.query().subscribe(value => {
      this.answers = value.body;
    });
  }
}
