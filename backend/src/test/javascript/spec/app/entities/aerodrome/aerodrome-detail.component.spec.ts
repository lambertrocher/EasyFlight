/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { BackendTestModule } from '../../../test.module';
import { AerodromeDetailComponent } from '../../../../../../main/webapp/app/entities/aerodrome/aerodrome-detail.component';
import { AerodromeService } from '../../../../../../main/webapp/app/entities/aerodrome/aerodrome.service';
import { Aerodrome } from '../../../../../../main/webapp/app/entities/aerodrome/aerodrome.model';

describe('Component Tests', () => {

    describe('Aerodrome Management Detail Component', () => {
        let comp: AerodromeDetailComponent;
        let fixture: ComponentFixture<AerodromeDetailComponent>;
        let service: AerodromeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BackendTestModule],
                declarations: [AerodromeDetailComponent],
                providers: [
                    AerodromeService
                ]
            })
            .overrideTemplate(AerodromeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AerodromeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AerodromeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Aerodrome(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.aerodrome).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
