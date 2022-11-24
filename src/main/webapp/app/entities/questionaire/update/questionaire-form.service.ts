import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IQuestionaire, NewQuestionaire } from '../questionaire.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQuestionaire for edit and NewQuestionaireFormGroupInput for create.
 */
type QuestionaireFormGroupInput = IQuestionaire | PartialWithRequiredKeyOf<NewQuestionaire>;

type QuestionaireFormDefaults = Pick<NewQuestionaire, 'id'>;

type QuestionaireFormGroupContent = {
  id: FormControl<IQuestionaire['id'] | NewQuestionaire['id']>;
  name: FormControl<IQuestionaire['name']>;
  description: FormControl<IQuestionaire['description']>;
  companyName: FormControl<IQuestionaire['companyName']>;
  gender: FormControl<IQuestionaire['gender']>;
  course: FormControl<IQuestionaire['course']>;
  semester: FormControl<IQuestionaire['semester']>;
  department: FormControl<IQuestionaire['department']>;
  totalRating: FormControl<IQuestionaire['totalRating']>;
  user: FormControl<IQuestionaire['user']>;
};

export type QuestionaireFormGroup = FormGroup<QuestionaireFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class QuestionaireFormService {
  createQuestionaireFormGroup(questionaire: QuestionaireFormGroupInput = { id: null }): QuestionaireFormGroup {
    const questionaireRawValue = {
      ...this.getFormDefaults(),
      ...questionaire,
    };
    return new FormGroup<QuestionaireFormGroupContent>({
      id: new FormControl(
        { value: questionaireRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(questionaireRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(questionaireRawValue.description),
      companyName: new FormControl(questionaireRawValue.companyName, {
        validators: [Validators.required],
      }),
      gender: new FormControl(questionaireRawValue.gender),
      course: new FormControl(questionaireRawValue.course, {
        validators: [Validators.required],
      }),
      semester: new FormControl(questionaireRawValue.semester, {
        validators: [Validators.required],
      }),
      department: new FormControl(questionaireRawValue.department, {
        validators: [Validators.required],
      }),
      totalRating: new FormControl(questionaireRawValue.totalRating),
      user: new FormControl(questionaireRawValue.user),
    });
  }

  getQuestionaire(form: QuestionaireFormGroup): IQuestionaire | NewQuestionaire {
    return form.getRawValue() as IQuestionaire | NewQuestionaire;
  }

  resetForm(form: QuestionaireFormGroup, questionaire: QuestionaireFormGroupInput): void {
    const questionaireRawValue = { ...this.getFormDefaults(), ...questionaire };
    form.reset(
      {
        ...questionaireRawValue,
        id: { value: questionaireRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): QuestionaireFormDefaults {
    return {
      id: null,
    };
  }
}
