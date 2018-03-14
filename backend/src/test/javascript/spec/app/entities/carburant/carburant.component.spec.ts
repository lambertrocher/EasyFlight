/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BackendTestModule } from '../../../test.module';
import { CarburantComponent } from '../../../../../../main/webapp/app/entities/carburant/carburant.component';
import { CarburantService } from '../../../../../../main/webapp/app/entities/carburant/carburant.service';
import { Carburant } from '../../../../../../main/webapp/app/entities/carburant/carburant.model';

describe('Component Tests', () => {

    describe('Carburant Management Component', () => {
        let comp: CarburantComponent;
        let fixture: ComponentFixture<CarburantComponent>;
        let service: CarburantService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BackendTestModule],
                declarations: [CarburantComponent],
                providers: [
                    CarburantService
                ]
            })
            .overrideTemplate(CarburantComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CarburantComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CarburantService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Carburant(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.carburants[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
