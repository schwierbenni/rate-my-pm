import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { QuestionaireFormService, QuestionaireFormGroup } from './questionaire-form.service';
import { IQuestionaire } from '../questionaire.model';
import { QuestionaireService } from '../service/questionaire.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { Gender } from 'app/entities/enumerations/gender.model';

@Component({
  selector: 'jhi-questionaire-update',
  templateUrl: './questionaire-update.component.html',
})
export class QuestionaireUpdateComponent implements OnInit {
  isSaving = false;
  questionaire: IQuestionaire | null = null;
  genderValues = Object.keys(Gender);

  usersSharedCollection: IUser[] = [];

  editForm: QuestionaireFormGroup = this.questionaireFormService.createQuestionaireFormGroup();

  constructor(
    protected questionaireService: QuestionaireService,
    protected questionaireFormService: QuestionaireFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questionaire }) => {
      this.questionaire = questionaire;
      if (questionaire) {
        this.updateForm(questionaire);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const questionaire = this.questionaireFormService.getQuestionaire(this.editForm);
    if (questionaire.id !== null) {
      this.subscribeToSaveResponse(this.questionaireService.update(questionaire));
    } else {
      this.subscribeToSaveResponse(this.questionaireService.create(questionaire));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuestionaire>>): void {
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

  protected updateForm(questionaire: IQuestionaire): void {
    this.questionaire = questionaire;
    this.questionaireFormService.resetForm(this.editForm, questionaire);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, questionaire.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.questionaire?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
