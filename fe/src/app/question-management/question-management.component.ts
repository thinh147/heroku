import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, NgForm, Validators } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { QuestionManagementService } from './question-management.service';

@Component({
  selector: 'app-question-management',
  templateUrl: './question-management.component.html',
  styleUrls: ['./question-management.component.scss']
})
export class QuestionManagementComponent {

  questions: QuestionModel[] = [];
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

  selectGroup: any[] = [];
  groups = [
    {
      code: 'P1'
    },
    {
      code: 'P2'
    },
    {
      code: 'P3'
    },
    {
      code: 'P4'
    },
    {
      code: 'P5'
    },
    {
      code: 'P6'
    },
    {
      code: 'P7'
    }
  ]
  settings = {
    singleSelection: false,
    idField: 'code',
    textField: 'code',
    enableCheckAll: true,
    selectAllText: 'Chọn tất cả',
    unSelectAllText: 'Bỏ chọn tất cả',
    allowSearchFilter: true,
    maxHeight: 300,
    searchPlaceholderText: 'Tìm kiếm',
    noDataAvailablePlaceholderText: 'Không có dữ liệu',
    placeholder: false
  };

  @ViewChild("formFilter") formFilter: NgForm;
  @ViewChild('modalCreate', { static: true }) modalCreate: ElementRef;

  constructor(
    private questionManagementService: QuestionManagementService,
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
  ) {
  }

  ngOnInit(): void {
    this.onSearch(1, 10);
  }
  onSearch(currentPage: number, pageSize: number) {
    this.questionManagementService.searchUser(currentPage, pageSize, this.selectGroup).subscribe({
      next: data => {
        this.questions = data.details.records;
        this.currentSearchParam.numFound = data.details.totalRecords;
      }, error: err => {
        console.log(err);
      }
    })
  }

  openModalCreate(type, queston: QuestionModel) {
    this.formCreate.reset();
    this.typeSave = type;
    if (type === 'edit') {
      this.formCreate.patchValue(queston);
    }
    this.modalService.open(this.modalCreate, { 'size': 'lg', backdrop: 'static' })
  }


  onChangePage(currentPage: number, pageSize: number) {
    this.currentSearchParam.currentPage = currentPage;
    this.onSearch(this.currentSearchParam?.currentPage, this.currentSearchParam?.pageSize);
  }

  onSubmit() {
    this.questionManagementService.requestSave(this.formCreate.getRawValue(), this.typeSave).subscribe({
      next: value => {
        this.onSearch(1, 10);
        this.modalService.dismissAll();
      }, error: error => {
        alert('Tạo thất bại')
      }
    })
  }
}
export class QuestionModel {
  id: string;
  type: string;
  detail: string;
  mediaPath: string;
  choiceA: string;
  choiceB: string;
  choiceC: string;
  choiceD: string;
  trueAnswer: string;
  part: string;
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