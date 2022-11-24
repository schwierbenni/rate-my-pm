import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { QuestionFormService } from './question-form.service';
import { QuestionService } from '../service/question.service';
import { IQuestion } from '../question.model';
import { IQuestionaire } from 'app/entities/questionaire/questionaire.model';
import { QuestionaireService } from 'app/entities/questionaire/service/questionaire.service';

import { QuestionUpdateComponent } from './question-update.component';

describe('Question Management Update Component', () => {
  let comp: QuestionUpdateComponent;
  let fixture: ComponentFixture<QuestionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let questionFormService: QuestionFormService;
  let questionService: QuestionService;
  let questionaireService: QuestionaireService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [QuestionUpdateComponent],
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
      .overrideTemplate(QuestionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuestionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    questionFormService = TestBed.inject(QuestionFormService);
    questionService = TestBed.inject(QuestionService);
    questionaireService = TestBed.inject(QuestionaireService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Questionaire query and add missing value', () => {
      const question: IQuestion = { id: 456 };
      const questionaire: IQuestionaire = { id: 40595 };
      question.questionaire = questionaire;

      const questionaireCollection: IQuestionaire[] = [{ id: 994 }];
      jest.spyOn(questionaireService, 'query').mockReturnValue(of(new HttpResponse({ body: questionaireCollection })));
      const additionalQuestionaires = [questionaire];
      const expectedCollection: IQuestionaire[] = [...additionalQuestionaires, ...questionaireCollection];
      jest.spyOn(questionaireService, 'addQuestionaireToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ question });
      comp.ngOnInit();

      expect(questionaireService.query).toHaveBeenCalled();
      expect(questionaireService.addQuestionaireToCollectionIfMissing).toHaveBeenCalledWith(
        questionaireCollection,
        ...additionalQuestionaires.map(expect.objectContaining)
      );
      expect(comp.questionairesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const question: IQuestion = { id: 456 };
      const questionaire: IQuestionaire = { id: 32678 };
      question.questionaire = questionaire;

      activatedRoute.data = of({ question });
      comp.ngOnInit();

      expect(comp.questionairesSharedCollection).toContain(questionaire);
      expect(comp.question).toEqual(question);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuestion>>();
      const question = { id: 123 };
      jest.spyOn(questionFormService, 'getQuestion').mockReturnValue(question);
      jest.spyOn(questionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ question });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: question }));
      saveSubject.complete();

      // THEN
      expect(questionFormService.getQuestion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(questionService.update).toHaveBeenCalledWith(expect.objectContaining(question));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuestion>>();
      const question = { id: 123 };
      jest.spyOn(questionFormService, 'getQuestion').mockReturnValue({ id: null });
      jest.spyOn(questionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ question: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: question }));
      saveSubject.complete();

      // THEN
      expect(questionFormService.getQuestion).toHaveBeenCalled();
      expect(questionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuestion>>();
      const question = { id: 123 };
      jest.spyOn(questionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ question });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(questionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareQuestionaire', () => {
      it('Should forward to questionaireService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(questionaireService, 'compareQuestionaire');
        comp.compareQuestionaire(entity, entity2);
        expect(questionaireService.compareQuestionaire).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
