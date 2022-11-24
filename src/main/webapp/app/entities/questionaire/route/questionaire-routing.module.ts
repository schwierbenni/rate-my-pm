import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { QuestionaireComponent } from '../list/questionaire.component';
import { QuestionaireDetailComponent } from '../detail/questionaire-detail.component';
import { QuestionaireUpdateComponent } from '../update/questionaire-update.component';
import { QuestionaireRoutingResolveService } from './questionaire-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';
import { QuestionaireQuestionsComponent } from '../questions/questionaire-questions.component';
import { QuestionaireAdminComponent } from '../admin/questionaire-admin.component';

const questionaireRoute: Routes = [
  {
    path: '',
    component: QuestionaireComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QuestionaireDetailComponent,
    resolve: {
      questionaire: QuestionaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QuestionaireUpdateComponent,
    resolve: {
      questionaire: QuestionaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QuestionaireUpdateComponent,
    resolve: {
      questionaire: QuestionaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/questions',
    component: QuestionaireQuestionsComponent,
    resolve: {
      questionaire: QuestionaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'questionaireManage',
    component: QuestionaireAdminComponent,
    resolve: {
      questionaire: QuestionaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(questionaireRoute)],
  exports: [RouterModule],
})
export class QuestionaireRoutingModule {}
