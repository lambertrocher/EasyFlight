/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BackendTestModule } from '../../../test.module';
import { ReservoirComponent } from '../../../../../../main/webapp/app/entities/reservoir/reservoir.component';
import { ReservoirService } from '../../../../../../main/webapp/app/entities/reservoir/reservoir.service';
import { Reservoir } from '../../../../../../main/webapp/app/entities/reservoir/reservoir.model';

describe('Component Tests', () => {

    describe('Reservoir Management Component', () => {
        let comp: ReservoirComponent;
        let fixture: ComponentFixture<ReservoirComponent>;
        let service: ReservoirService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BackendTestModule],
                declarations: [ReservoirComponent],
                providers: [
                    ReservoirService
                ]
            })
            .overrideTemplate(ReservoirComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ReservoirComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReservoirService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Reservoir(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.reservoirs[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
