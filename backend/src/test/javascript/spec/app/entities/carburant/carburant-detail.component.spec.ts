/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { BackendTestModule } from '../../../test.module';
import { CarburantDetailComponent } from '../../../../../../main/webapp/app/entities/carburant/carburant-detail.component';
import { CarburantService } from '../../../../../../main/webapp/app/entities/carburant/carburant.service';
import { Carburant } from '../../../../../../main/webapp/app/entities/carburant/carburant.model';

describe('Component Tests', () => {

    describe('Carburant Management Detail Component', () => {
        let comp: CarburantDetailComponent;
        let fixture: ComponentFixture<CarburantDetailComponent>;
        let service: CarburantService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BackendTestModule],
                declarations: [CarburantDetailComponent],
                providers: [
                    CarburantService
                ]
            })
            .overrideTemplate(CarburantDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CarburantDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CarburantService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Carburant(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.carburant).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
