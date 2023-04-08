import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, NgForm, Validators } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UserManagementService } from './user-management.service';

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent {
  users: UserModel[] = [];
  currentSearchParam = {
    currentPage: 1 as number,// mặc định phân trang 
    pageSize: 10 as number,// mặc định số lượng bản ghi 1 trang 
    numFound: null as number, // Số lượng bản ghi có trong db
  };
  typeSave: string;
  formCreate = new FormGroup({
    id: new FormControl('', Validators.required),
    type: new FormControl('', Validators.required),
    detail: new FormControl('', Validators.required),
    mediaPath: new FormControl(''),
    choiceA: new FormControl(''),
    choiceB: new FormControl(''),
    choiceC: new FormControl(''),
    choiceD: new FormControl(''),
    trueAnswer: new FormControl('', Validators.required),
    part: new FormControl('', Validators.required),
  })

  @ViewChild("formFilter") formFilter: NgForm;
  @ViewChild('modalCreate', { static: true }) modalCreate: ElementRef;

  constructor(
    private userManagementService: UserManagementService,
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
  ) {
  }

  ngOnInit(): void {
    this.onSearch(1, 10);
  }
  onSearch(currentPage: number, pageSize: number) {
    this.userManagementService.searchUser(currentPage, pageSize, this.formFilter?.value?.name).subscribe({
      next: data => {
        this.users = data.details.records;
        this.currentSearchParam.numFound = data.details.totalRecords;
      }, error: err => {
        console.log(err);
      }
    })
  }

  openModalCreate(type, queston: UserModel) {
    // this.formCreate.reset();
    // this.typeSave = type;
    // if (type === 'edit') {
    //   this.formCreate.patchValue(queston);
    // }
    // this.modalService.open(this.modalCreate, { 'size': 'lg', backdrop: 'static' })
  }


  onChangePage(currentPage: number, pageSize: number) {
    this.currentSearchParam.currentPage = currentPage;
    this.onSearch(this.currentSearchParam?.currentPage, this.currentSearchParam?.pageSize);
  }

  onSubmit() {
    this.userManagementService.requestSave(this.formCreate.getRawValue(), this.typeSave).subscribe({
      next: value => {
        this.onSearch(1, 10);
        this.modalService.dismissAll();
      }, error: error => {
        alert('Tạo thất bại')
      }
    })
  }
}
export class UserModel {
  id: number;
  fullName: string;
  level: string;
  phoneNumber: number;
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