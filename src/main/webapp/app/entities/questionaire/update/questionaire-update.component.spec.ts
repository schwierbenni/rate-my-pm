import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { QuestionaireFormService } from './questionaire-form.service';
import { QuestionaireService } from '../service/questionaire.service';
import { IQuestionaire } from '../questionaire.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { QuestionaireUpdateComponent } from './questionaire-update.component';

describe('Questionaire Management Update Component', () => {
  let comp: QuestionaireUpdateComponent;
  let fixture: ComponentFixture<QuestionaireUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let questionaireFormService: QuestionaireFormService;
  let questionaireService: QuestionaireService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [QuestionaireUpdateComponent],
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
      .overrideTemplate(QuestionaireUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuestionaireUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    questionaireFormService = TestBed.inject(QuestionaireFormService);
    questionaireService = TestBed.inject(QuestionaireService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const questionaire: IQuestionaire = { id: 456 };
      const user: IUser = { id: 59152 };
      questionaire.user = user;

      const userCollection: IUser[] = [{ id: 24149 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ questionaire });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const questionaire: IQuestionaire = { id: 456 };
      const user: IUser = { id: 98941 };
      questionaire.user = user;

      activatedRoute.data = of({ questionaire });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.questionaire).toEqual(questionaire);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuestionaire>>();
      const questionaire = { id: 123 };
      jest.spyOn(questionaireFormService, 'getQuestionaire').mockReturnValue(questionaire);
      jest.spyOn(questionaireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questionaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: questionaire }));
      saveSubject.complete();

      // THEN
      expect(questionaireFormService.getQuestionaire).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(questionaireService.update).toHaveBeenCalledWith(expect.objectContaining(questionaire));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuestionaire>>();
      const questionaire = { id: 123 };
      jest.spyOn(questionaireFormService, 'getQuestionaire').mockReturnValue({ id: null });
      jest.spyOn(questionaireService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questionaire: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: questionaire }));
      saveSubject.complete();

      // THEN
      expect(questionaireFormService.getQuestionaire).toHaveBeenCalled();
      expect(questionaireService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuestionaire>>();
      const questionaire = { id: 123 };
      jest.spyOn(questionaireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questionaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(questionaireService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
