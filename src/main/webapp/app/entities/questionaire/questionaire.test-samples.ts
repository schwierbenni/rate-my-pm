import { Gender } from 'app/entities/enumerations/gender.model';

import { IQuestionaire, NewQuestionaire } from './questionaire.model';

export const sampleWithRequiredData: IQuestionaire = {
  id: 60134,
  name: 'Baden-Württemberg',
  companyName: 'solid RSS Plastic',
  course: 'indexing',
  semester: 'Ergonomic',
  department: 'payment hack Internal',
};

export const sampleWithPartialData: IQuestionaire = {
  id: 86689,
  name: 'Ergonomic',
  companyName: 'deposit bricks-and-clicks',
  course: 'Rue niches Malta',
  semester: 'success systematic navigate',
  department: 'robust Corners',
};

export const sampleWithFullData: IQuestionaire = {
  id: 19289,
  name: 'algorithm Garden withdrawal',
  description: 'Accounts Falkland Directives',
  companyName: 'Monitored redundant Usbekistan',
  gender: Gender['DIVERS'],
  course: 'Facilitator',
  semester: 'mobile',
  department: 'Programmable Brandenburg turn-key',
  totalRating: 27478,
};

export const sampleWithNewData: NewQuestionaire = {
  name: 'architect Mauritius multimedia',
  companyName: 'index',
  course: 'flexibility',
  semester: 'Administrator Cambridgeshire Applications',
  department: 'Producer Fresh Infrastructure',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
