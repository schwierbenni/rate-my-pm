import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAnswer, NewAnswer } from '../answer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAnswer for edit and NewAnswerFormGroupInput for create.
 */
type AnswerFormGroupInput = IAnswer | PartialWithRequiredKeyOf<NewAnswer>;

type AnswerFormDefaults = Pick<NewAnswer, 'id'>;

type AnswerFormGroupContent = {
  id: FormControl<IAnswer['id'] | NewAnswer['id']>;
  questionText: FormControl<IAnswer['questionText']>;
  rating: FormControl<IAnswer['rating']>;
  optionalMessage: FormControl<IAnswer['optionalMessage']>;
  questionare: FormControl<IAnswer['questionare']>;
};

export type AnswerFormGroup = FormGroup<AnswerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AnswerFormService {
  createAnswerFormGroup(answer: AnswerFormGroupInput = { id: null }): AnswerFormGroup {
    const answerRawValue = {
      ...this.getFormDefaults(),
      ...answer,
    };
    return new FormGroup<AnswerFormGroupContent>({
      id: new FormControl(
        { value: answerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      questionText: new FormControl(
        { value: answerRawValue.questionText, disabled: true },
        {
          validators: [Validators.required],
        }
      ),
      rating: new FormControl(answerRawValue.rating),
      optionalMessage: new FormControl(answerRawValue.optionalMessage),
      questionare: new FormControl(answerRawValue.questionare),
    });
  }

  getAnswer(form: AnswerFormGroup): IAnswer | NewAnswer {
    return form.getRawValue() as IAnswer | NewAnswer;
  }

  resetForm(form: AnswerFormGroup, answer: AnswerFormGroupInput): void {
    const answerRawValue = { ...this.getFormDefaults(), ...answer };
    form.reset(
      {
        ...answerRawValue,
        id: { value: answerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AnswerFormDefaults {
    return {
      id: null,
    };
  }
}
