import { CommonModule } from '@angular/common';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { NavbarComponent } from './navbar/navbar.component';
import { QuestionManagementComponent } from './question-management/question-management.component';
import { UserManagementComponent } from './user-management/user-management.component';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { LoginInterceptorService } from './login/login-interceptor.service';
import { ExamComponent } from './exam/exam.component';
import { VocabularyComponent } from './vocabulary/vocabulary.component';
import { PaymentComponent } from './payment/payment.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    NavbarComponent,
    QuestionManagementComponent,
    UserManagementComponent,
    ExamComponent,
    VocabularyComponent,
    PaymentComponent,
  ],
  imports: [
    CommonModule,
    HttpClientModule,
    FormsModule,
    NgbModule,
    ReactiveFormsModule,
    NgMultiSelectDropDownModule,
    BrowserModule,
    AppRoutingModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: LoginInterceptorService,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
