import { IQuestionaire } from 'app/entities/questionaire/questionaire.model';

export interface IQuestion {
  id: number;
  questionText?: string | null;
  questionaire?: Pick<IQuestionaire, 'id'> | null;
}

export type NewQuestion = Omit<IQuestion, 'id'> & { id: null };
