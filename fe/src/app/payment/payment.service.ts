import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { PaymentModel, ResponseAPI } from './payment.component';

@Injectable({
    providedIn: 'root'
})
export class PaymentService {

    baseURL: string;
    constructor(private http: HttpClient) {
        this.baseURL = environment.baseURL;
    }

    search(currentPage: number, pageSize: number, name: string) {
        //tham số tìm kiếm null là tìm tất cả /api/admin/user-lists
        const url = new URL(this.baseURL + '/admin/payments');
        url.searchParams.set('page', (currentPage - 1).toString());
        url.searchParams.set('size', pageSize.toString());
        name && name.length > 0 ? url.searchParams.set('phoneNumber', name) : null;
        return this.http.get<ResponseAPI<PaymentModel[]>>(url.href);
    }
}
