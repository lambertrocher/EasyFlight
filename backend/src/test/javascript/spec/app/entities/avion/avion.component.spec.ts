/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BackendTestModule } from '../../../test.module';
import { AvionComponent } from '../../../../../../main/webapp/app/entities/avion/avion.component';
import { AvionService } from '../../../../../../main/webapp/app/entities/avion/avion.service';
import { Avion } from '../../../../../../main/webapp/app/entities/avion/avion.model';

describe('Component Tests', () => {

    describe('Avion Management Component', () => {
        let comp: AvionComponent;
        let fixture: ComponentFixture<AvionComponent>;
        let service: AvionService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BackendTestModule],
                declarations: [AvionComponent],
                providers: [
                    AvionService
                ]
            })
            .overrideTemplate(AvionComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AvionComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AvionService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Avion(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.avions[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
