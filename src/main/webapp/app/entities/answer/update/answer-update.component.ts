import { Component, Input, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { AnswerFormService, AnswerFormGroup } from './answer-form.service';
import { IAnswer } from '../answer.model';
import { AnswerService } from '../service/answer.service';
import { IQuestionaire } from 'app/entities/questionaire/questionaire.model';
import { QuestionaireService } from 'app/entities/questionaire/service/questionaire.service';

@Component({
  selector: 'jhi-answer-update',
  templateUrl: './answer-update.component.html',
})
export class AnswerUpdateComponent implements OnInit {
  @Input() answerInput: any;
  isSaving = false;
  answer: IAnswer | null = null;

  questionairesSharedCollection: IQuestionaire[] = [];

  editForm: AnswerFormGroup = this.answerFormService.createAnswerFormGroup();

  constructor(
    protected answerService: AnswerService,
    protected answerFormService: AnswerFormService,
    protected questionaireService: QuestionaireService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareQuestionaire = (o1: IQuestionaire | null, o2: IQuestionaire | null): boolean =>
    this.questionaireService.compareQuestionaire(o1, o2);

  ngOnInit(): void {
    this.answer = this.answerInput;
    if (this.answerInput) {
      this.updateForm(this.answerInput);
    }

    this.loadRelationshipsOptions();
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const answer = this.answerFormService.getAnswer(this.editForm);
    if (answer.id !== null) {
      this.subscribeToSaveResponse(this.answerService.update(answer));
    } else {
      this.subscribeToSaveResponse(this.answerService.create(answer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnswer>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveFinalize(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(answer: IAnswer): void {
    this.answer = answer;
    this.answerFormService.resetForm(this.editForm, answer);

    this.questionairesSharedCollection = this.questionaireService.addQuestionaireToCollectionIfMissing<IQuestionaire>(
      this.questionairesSharedCollection,
      answer.questionare
    );
  }

  protected loadRelationshipsOptions(): void {
    this.questionaireService
      .query()
      .pipe(map((res: HttpResponse<IQuestionaire[]>) => res.body ?? []))
      .pipe(
        map((questionaires: IQuestionaire[]) =>
          this.questionaireService.addQuestionaireToCollectionIfMissing<IQuestionaire>(questionaires, this.answer?.questionare)
        )
      )
      .subscribe((questionaires: IQuestionaire[]) => (this.questionairesSharedCollection = questionaires));
  }
}
