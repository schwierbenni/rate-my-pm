import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { QuestionFormService, QuestionFormGroup } from './question-form.service';
import { IQuestion } from '../question.model';
import { QuestionService } from '../service/question.service';
import { IQuestionaire } from 'app/entities/questionaire/questionaire.model';
import { QuestionaireService } from 'app/entities/questionaire/service/questionaire.service';

@Component({
  selector: 'jhi-question-update',
  templateUrl: './question-update.component.html',
})
export class QuestionUpdateComponent implements OnInit {
  isSaving = false;
  question: IQuestion | null = null;

  questionairesSharedCollection: IQuestionaire[] = [];

  editForm: QuestionFormGroup = this.questionFormService.createQuestionFormGroup();

  constructor(
    protected questionService: QuestionService,
    protected questionFormService: QuestionFormService,
    protected questionaireService: QuestionaireService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareQuestionaire = (o1: IQuestionaire | null, o2: IQuestionaire | null): boolean =>
    this.questionaireService.compareQuestionaire(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ question }) => {
      this.question = question;
      if (question) {
        this.updateForm(question);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const question = this.questionFormService.getQuestion(this.editForm);
    if (question.id !== null) {
      this.subscribeToSaveResponse(this.questionService.update(question));
    } else {
      this.subscribeToSaveResponse(this.questionService.create(question));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuestion>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(question: IQuestion): void {
    this.question = question;
    this.questionFormService.resetForm(this.editForm, question);

    this.questionairesSharedCollection = this.questionaireService.addQuestionaireToCollectionIfMissing<IQuestionaire>(
      this.questionairesSharedCollection,
      question.questionaire
    );
  }

  protected loadRelationshipsOptions(): void {
    this.questionaireService
      .query()
      .pipe(map((res: HttpResponse<IQuestionaire[]>) => res.body ?? []))
      .pipe(
        map((questionaires: IQuestionaire[]) =>
          this.questionaireService.addQuestionaireToCollectionIfMissing<IQuestionaire>(questionaires, this.question?.questionaire)
        )
      )
      .subscribe((questionaires: IQuestionaire[]) => (this.questionairesSharedCollection = questionaires));
  }
}
