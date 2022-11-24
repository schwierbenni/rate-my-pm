import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { QuestionaireComponent } from './list/questionaire.component';
import { QuestionaireDetailComponent } from './detail/questionaire-detail.component';
import { QuestionaireUpdateComponent } from './update/questionaire-update.component';
import { QuestionaireDeleteDialogComponent } from './delete/questionaire-delete-dialog.component';
import { QuestionaireRoutingModule } from './route/questionaire-routing.module';
import { QuestionaireQuestionsComponent } from './questions/questionaire-questions.component';
import { QuestionaireAdminComponent } from './admin/questionaire-admin.component';
import { AnswerModule } from '../answer/answer.module';

@NgModule({
  imports: [SharedModule, QuestionaireRoutingModule, AnswerModule],
  declarations: [
    QuestionaireComponent,
    QuestionaireDetailComponent,
    QuestionaireUpdateComponent,
    QuestionaireDeleteDialogComponent,
    QuestionaireQuestionsComponent,
    QuestionaireAdminComponent,
  ],
})
export class QuestionaireModule {}
