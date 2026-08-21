import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AutoFormComponent } from './auto-form';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('AutoFormComponent', () => {
  let component: AutoFormComponent;
  let fixture: ComponentFixture<AutoFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        AutoFormComponent,
        ReactiveFormsModule,
        RouterTestingModule,
        HttpClientTestingModule
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AutoFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
