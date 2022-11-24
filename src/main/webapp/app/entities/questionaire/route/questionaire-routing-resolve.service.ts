import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQuestionaire } from '../questionaire.model';
import { QuestionaireService } from '../service/questionaire.service';

@Injectable({ providedIn: 'root' })
export class QuestionaireRoutingResolveService implements Resolve<IQuestionaire | null> {
  constructor(protected service: QuestionaireService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IQuestionaire | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((questionaire: HttpResponse<IQuestionaire>) => {
          if (questionaire.body) {
            return of(questionaire.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
