/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BackendTestModule } from '../../../test.module';
import { PisteComponent } from '../../../../../../main/webapp/app/entities/piste/piste.component';
import { PisteService } from '../../../../../../main/webapp/app/entities/piste/piste.service';
import { Piste } from '../../../../../../main/webapp/app/entities/piste/piste.model';

describe('Component Tests', () => {

    describe('Piste Management Component', () => {
        let comp: PisteComponent;
        let fixture: ComponentFixture<PisteComponent>;
        let service: PisteService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BackendTestModule],
                declarations: [PisteComponent],
                providers: [
                    PisteService
                ]
            })
            .overrideTemplate(PisteComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PisteComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PisteService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Piste(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.pistes[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
