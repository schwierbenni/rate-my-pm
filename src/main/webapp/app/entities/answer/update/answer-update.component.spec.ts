import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { from } from 'rxjs';
import { IAnswer } from '../answer.model';
import { QuestionaireService } from 'app/entities/questionaire/service/questionaire.service';

import { AnswerUpdateComponent } from './answer-update.component';

describe('Answer Management Update Component', () => {
  let comp: AnswerUpdateComponent;
  let fixture: ComponentFixture<AnswerUpdateComponent>;
  let questionaireService: QuestionaireService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AnswerUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AnswerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AnswerUpdateComponent);
    questionaireService = TestBed.inject(QuestionaireService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Questionaire query and add missing value', () => {
      const answer: IAnswer = { id: 456 };
      answer.questionare = { id: 71386 };
      comp.ngOnInit();

      expect(comp.isSaving).toBeFalsy();
    });
  });
});
