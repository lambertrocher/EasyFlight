/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { BackendTestModule } from '../../../test.module';
import { VolDetailComponent } from '../../../../../../main/webapp/app/entities/vol/vol-detail.component';
import { VolService } from '../../../../../../main/webapp/app/entities/vol/vol.service';
import { Vol } from '../../../../../../main/webapp/app/entities/vol/vol.model';

describe('Component Tests', () => {

    describe('Vol Management Detail Component', () => {
        let comp: VolDetailComponent;
        let fixture: ComponentFixture<VolDetailComponent>;
        let service: VolService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BackendTestModule],
                declarations: [VolDetailComponent],
                providers: [
                    VolService
                ]
            })
            .overrideTemplate(VolDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(VolDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VolService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Vol(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.vol).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
