import { IAnswer, NewAnswer } from './answer.model';

export const sampleWithRequiredData: IAnswer = {
  id: 52963,
  questionText: 'Lake Music',
};

export const sampleWithPartialData: IAnswer = {
  id: 56812,
  questionText: 'green',
  rating: 45223,
  optionalMessage: 'invoice Kwacha',
};

export const sampleWithFullData: IAnswer = {
  id: 30029,
  questionText: 'withdrawal groupware teal',
  rating: 23429,
  optionalMessage: 'Plastic',
};

export const sampleWithNewData: NewAnswer = {
  questionText: 'Chips Rubber',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
