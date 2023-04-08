import { GroupModel, ItemModel } from './../vocabulary/vocabulary.component';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { ExamModel, TypesModel } from '../exam/exam.component';
import { ResponseAPI } from '../question-management/question-management.component';

@Injectable({
  providedIn: 'root'
})
export class ShareService {

  baseURL: string;
  constructor(private http: HttpClient) {
    this.baseURL = environment.baseURL;
  }

  searchExam(currentPage: number, pageSize: number) {
    //tham số tìm kiếm null là tìm tất cả 
    const url = new URL(this.baseURL + '/admin/exam-lists');
    url.searchParams.set('page', (currentPage - 1).toString());
    url.searchParams.set('size', pageSize.toString());
    // lấy thông tin ở form để thực hiện request search
    return this.http.get<ResponseAPI<ExamModel[]>>(url.href);
  }

  searchListTestType() {
    const url = new URL(this.baseURL + '/activities/types');
    return this.http.get<ResponseAPI2<TypesModel[]>>(url.href);
  }

  requestSaveExam(question: {}) {
    const url = new URL(this.baseURL + '/admin/exam/create');
    return this.http.post<ExamModel>(url.href, question);
  }

  searchItem(search: {}) {
    //tham số tìm kiếm null là tìm tất cả 
    const url = new URL(this.baseURL + '/admin/vocabularies/item');
    if (search) {
      for (const [key, value] of Object.entries(search)) {
        if (value
        ) {
          url.searchParams.set(key, value as string);
        }
      }
    }

    return this.http.get<ResponseAPI<ItemModel[]>>(url.href);
  }

  requestSaveItem(question: {}) {
    const url = new URL(this.baseURL + '/admin/vocabularies/item/create');
    return this.http.post<ResponseAPI2<ItemModel[]>>(url.href, question);
  }

  searchGroup() {
    //tham số tìm kiếm null là tìm tất cả 
    const url = new URL(this.baseURL + '/admin/vocabularies/group');
    return this.http.get<ResponseAPI2<GroupModel[]>>(url.href);
  }

  requestSaveGroup(question: {}) {
    const url = new URL(this.baseURL + '/admin/vocabularies/group/create');
    return this.http.post<GroupModel>(url.href, question);
  }
}

export interface ResponseAPI2<T> {
  code: number;
  success: boolean;
  details: T;
  message: string;
  timestamp: number
}