/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BackendTestModule } from '../../../test.module';
import { PiloteComponent } from '../../../../../../main/webapp/app/entities/pilote/pilote.component';
import { PiloteService } from '../../../../../../main/webapp/app/entities/pilote/pilote.service';
import { Pilote } from '../../../../../../main/webapp/app/entities/pilote/pilote.model';

describe('Component Tests', () => {

    describe('Pilote Management Component', () => {
        let comp: PiloteComponent;
        let fixture: ComponentFixture<PiloteComponent>;
        let service: PiloteService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BackendTestModule],
                declarations: [PiloteComponent],
                providers: [
                    PiloteService
                ]
            })
            .overrideTemplate(PiloteComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PiloteComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PiloteService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Pilote(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.pilotes[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
