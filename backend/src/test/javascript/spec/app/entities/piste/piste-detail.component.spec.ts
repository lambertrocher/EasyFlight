/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { BackendTestModule } from '../../../test.module';
import { PisteDetailComponent } from '../../../../../../main/webapp/app/entities/piste/piste-detail.component';
import { PisteService } from '../../../../../../main/webapp/app/entities/piste/piste.service';
import { Piste } from '../../../../../../main/webapp/app/entities/piste/piste.model';

describe('Component Tests', () => {

    describe('Piste Management Detail Component', () => {
        let comp: PisteDetailComponent;
        let fixture: ComponentFixture<PisteDetailComponent>;
        let service: PisteService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BackendTestModule],
                declarations: [PisteDetailComponent],
                providers: [
                    PisteService
                ]
            })
            .overrideTemplate(PisteDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PisteDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PisteService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Piste(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.piste).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
