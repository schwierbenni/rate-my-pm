import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'questionaire',
        data: { pageTitle: 'rateMyPmApp.questionaire.home.title' },
        loadChildren: () => import('./questionaire/questionaire.module').then(m => m.QuestionaireModule),
      },
      {
        path: 'answer',
        data: { pageTitle: 'rateMyPmApp.answer.home.title' },
        loadChildren: () => import('./answer/answer.module').then(m => m.AnswerModule),
      },
      {
        path: 'question',
        data: { pageTitle: 'rateMyPmApp.question.home.title' },
        loadChildren: () => import('./question/question.module').then(m => m.QuestionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
