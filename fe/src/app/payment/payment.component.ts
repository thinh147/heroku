import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { PaymentService } from './payment.service';

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.scss']
})
export class PaymentComponent {
  payments: PaymentModel[] = [];
  currentSearchParam = {
    currentPage: 1 as number,// mặc định phân trang 
    pageSize: 10 as number,// mặc định số lượng bản ghi 1 trang 
    numFound: null as number, // Số lượng bản ghi có trong db
  };

  @ViewChild("formFilter") formFilter: NgForm;

  constructor(
    private paymentService: PaymentService,
  ) {
  }

  ngOnInit(): void {
    this.onSearch(1, 10);
  }

  onSearch(currentPage: number, pageSize: number) {
    this.paymentService.search(currentPage, pageSize, this.formFilter?.value?.phoneNumber).subscribe({
      next: data => {
        this.payments = data.details.records;
        this.currentSearchParam.numFound = data.details.totalRecords;
      }, error: err => {
        console.log(err);
      }
    })
  }

  onChangePage(currentPage: number, pageSize: number) {
    this.currentSearchParam.currentPage = currentPage;
    this.onSearch(this.currentSearchParam?.currentPage, this.currentSearchParam?.pageSize);
  }

}
export class PaymentModel {
  id: number;
  cost: number;
  createdDate: number[];
  status: string;
  userInfo: {
    userId: number;
    name: string;
  }
}

export class ResponseAPI<T> {
  code: number;
  success: boolean;
  details: {
    totalRecords: number;
    offset: number,
    limit: number,
    records: T
  }
  message: string;
  timestamp: number
}