import { IQuestion, NewQuestion } from './question.model';

export const sampleWithRequiredData: IQuestion = {
  id: 47363,
  questionText: 'Account budgetary invoice',
};

export const sampleWithPartialData: IQuestion = {
  id: 43032,
  questionText: 'Buckinghamshire',
};

export const sampleWithFullData: IQuestion = {
  id: 60965,
  questionText: 'Chicken quantify',
};

export const sampleWithNewData: NewQuestion = {
  questionText: 'Motorway',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
