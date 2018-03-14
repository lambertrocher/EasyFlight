/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { BackendTestModule } from '../../../test.module';
import { AvionDetailComponent } from '../../../../../../main/webapp/app/entities/avion/avion-detail.component';
import { AvionService } from '../../../../../../main/webapp/app/entities/avion/avion.service';
import { Avion } from '../../../../../../main/webapp/app/entities/avion/avion.model';

describe('Component Tests', () => {

    describe('Avion Management Detail Component', () => {
        let comp: AvionDetailComponent;
        let fixture: ComponentFixture<AvionDetailComponent>;
        let service: AvionService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BackendTestModule],
                declarations: [AvionDetailComponent],
                providers: [
                    AvionService
                ]
            })
            .overrideTemplate(AvionDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AvionDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AvionService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Avion(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.avion).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
