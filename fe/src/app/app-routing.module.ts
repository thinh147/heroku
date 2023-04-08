import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { LoginGuardService } from './share/login-guard.service';
import { QuestionManagementComponent } from './question-management/question-management.component';
import { UserManagementComponent } from './user-management/user-management.component';
import { ExamComponent } from './exam/exam.component';
import { VocabularyComponent } from './vocabulary/vocabulary.component';
import { PaymentComponent } from './payment/payment.component';

const routes: Routes = [
  { path: '', redirectTo: 'vocabulary', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  {
    path: 'question',
    component: QuestionManagementComponent,
    canActivate: [LoginGuardService]
  },
  {
    path: 'payment',
    component: PaymentComponent,
    canActivate: [LoginGuardService]
  },
  {
    path: 'user',
    component: UserManagementComponent,
    canActivate: [LoginGuardService]
  },
  {
    path: 'exam',
    component: ExamComponent,
    canActivate: [LoginGuardService]
  },
  {
    path: 'vocabulary',
    component: VocabularyComponent,
    canActivate: [LoginGuardService]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
