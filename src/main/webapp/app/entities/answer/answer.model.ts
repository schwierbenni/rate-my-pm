import { IQuestionaire } from 'app/entities/questionaire/questionaire.model';

export interface IAnswer {
  id: number;
  questionText?: string | null;
  rating?: number | null;
  optionalMessage?: string | null;
  questionare?: Pick<IQuestionaire, 'id' | 'name'> | null;
}

export type NewAnswer = Omit<IAnswer, 'id'> & { id: null };
