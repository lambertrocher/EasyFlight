/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BackendTestModule } from '../../../test.module';
import { AerodromeComponent } from '../../../../../../main/webapp/app/entities/aerodrome/aerodrome.component';
import { AerodromeService } from '../../../../../../main/webapp/app/entities/aerodrome/aerodrome.service';
import { Aerodrome } from '../../../../../../main/webapp/app/entities/aerodrome/aerodrome.model';

describe('Component Tests', () => {

    describe('Aerodrome Management Component', () => {
        let comp: AerodromeComponent;
        let fixture: ComponentFixture<AerodromeComponent>;
        let service: AerodromeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BackendTestModule],
                declarations: [AerodromeComponent],
                providers: [
                    AerodromeService
                ]
            })
            .overrideTemplate(AerodromeComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AerodromeComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AerodromeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Aerodrome(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.aerodromes[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
