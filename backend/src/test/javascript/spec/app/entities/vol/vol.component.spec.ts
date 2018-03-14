/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BackendTestModule } from '../../../test.module';
import { VolComponent } from '../../../../../../main/webapp/app/entities/vol/vol.component';
import { VolService } from '../../../../../../main/webapp/app/entities/vol/vol.service';
import { Vol } from '../../../../../../main/webapp/app/entities/vol/vol.model';

describe('Component Tests', () => {

    describe('Vol Management Component', () => {
        let comp: VolComponent;
        let fixture: ComponentFixture<VolComponent>;
        let service: VolService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BackendTestModule],
                declarations: [VolComponent],
                providers: [
                    VolService
                ]
            })
            .overrideTemplate(VolComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(VolComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VolService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Vol(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.vols[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
