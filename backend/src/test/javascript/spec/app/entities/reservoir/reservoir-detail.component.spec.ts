/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { BackendTestModule } from '../../../test.module';
import { ReservoirDetailComponent } from '../../../../../../main/webapp/app/entities/reservoir/reservoir-detail.component';
import { ReservoirService } from '../../../../../../main/webapp/app/entities/reservoir/reservoir.service';
import { Reservoir } from '../../../../../../main/webapp/app/entities/reservoir/reservoir.model';

describe('Component Tests', () => {

    describe('Reservoir Management Detail Component', () => {
        let comp: ReservoirDetailComponent;
        let fixture: ComponentFixture<ReservoirDetailComponent>;
        let service: ReservoirService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BackendTestModule],
                declarations: [ReservoirDetailComponent],
                providers: [
                    ReservoirService
                ]
            })
            .overrideTemplate(ReservoirDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ReservoirDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReservoirService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Reservoir(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.reservoir).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
