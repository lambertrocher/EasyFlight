/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { BackendTestModule } from '../../../test.module';
import { PiloteDetailComponent } from '../../../../../../main/webapp/app/entities/pilote/pilote-detail.component';
import { PiloteService } from '../../../../../../main/webapp/app/entities/pilote/pilote.service';
import { Pilote } from '../../../../../../main/webapp/app/entities/pilote/pilote.model';

describe('Component Tests', () => {

    describe('Pilote Management Detail Component', () => {
        let comp: PiloteDetailComponent;
        let fixture: ComponentFixture<PiloteDetailComponent>;
        let service: PiloteService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BackendTestModule],
                declarations: [PiloteDetailComponent],
                providers: [
                    PiloteService
                ]
            })
            .overrideTemplate(PiloteDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PiloteDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PiloteService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Pilote(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.pilote).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
