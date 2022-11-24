import { IUser } from 'app/entities/user/user.model';
import { Gender } from 'app/entities/enumerations/gender.model';

export interface IQuestionaire {
  id: number;
  name?: string | null;
  description?: string | null;
  companyName?: string | null;
  gender?: Gender | null;
  course?: string | null;
  semester?: string | null;
  department?: string | null;
  totalRating?: number | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewQuestionaire = Omit<IQuestionaire, 'id'> & { id: null };
