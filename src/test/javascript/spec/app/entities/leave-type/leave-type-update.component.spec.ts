/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { SoptorshiTestModule } from '../../../test.module';
import { LeaveTypeUpdateComponent } from 'app/entities/leave-type/leave-type-update.component';
import { LeaveTypeService } from 'app/entities/leave-type/leave-type.service';
import { LeaveType } from 'app/shared/model/leave-type.model';

describe('Component Tests', () => {
    describe('LeaveType Management Update Component', () => {
        let comp: LeaveTypeUpdateComponent;
        let fixture: ComponentFixture<LeaveTypeUpdateComponent>;
        let service: LeaveTypeService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SoptorshiTestModule],
                declarations: [LeaveTypeUpdateComponent]
            })
                .overrideTemplate(LeaveTypeUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(LeaveTypeUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LeaveTypeService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new LeaveType(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.leaveType = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new LeaveType();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.leaveType = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
