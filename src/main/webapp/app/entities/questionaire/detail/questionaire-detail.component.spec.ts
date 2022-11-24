import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { QuestionaireDetailComponent } from './questionaire-detail.component';

describe('Questionaire Management Detail Component', () => {
  let comp: QuestionaireDetailComponent;
  let fixture: ComponentFixture<QuestionaireDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [QuestionaireDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ questionaire: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(QuestionaireDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(QuestionaireDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load questionaire on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.questionaire).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
