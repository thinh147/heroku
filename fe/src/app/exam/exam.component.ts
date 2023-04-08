import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormControl, FormGroup, NgForm, Validators } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ShareService } from '../share/share.service';

@Component({
  selector: 'app-exam',
  templateUrl: './exam.component.html',
  styleUrls: ['./exam.component.scss']
})
export class ExamComponent {
  exams: ExamModel[] = [];
  typesModel: TypesModel[] = [];
  currentSearchParam = {
    currentPage: 1 as number,// mặc định phân trang 
    pageSize: 10 as number,// mặc định số lượng bản ghi 1 trang 
    numFound: null as number, // Số lượng bản ghi có trong db
  };
  typeSave: string;
  formCreate = new FormGroup({
    description: new FormControl(''),
    duration: new FormControl('', Validators.required),
    typeId: new FormControl('', Validators.required),
  })
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
    private shareService: ShareService,
    private modalService: NgbModal,
  ) {
  }

  ngOnInit(): void {
    this.onSearch(1, 10);
  }
  onSearch(currentPage: number, pageSize: number) {
    this.shareService.searchExam(currentPage, pageSize).subscribe({
      next: data => {
        this.exams = data.details.records;
        // this.currentSearchParam.numFound = data.details.totalRecords;
      }, error: err => {
        console.log(err);
      }
    })
  }

  openModal(type, queston: ExamModel) {
    this.formCreate.reset();
    this.shareService.searchListTestType().subscribe({
      next: data => {
        this.typesModel = data.details;
      },
      error: err => {

      }
    })
    this.typeSave = type;
    if (type === 'edit') {
      // this.formCreate.patchValue(queston);
    }
    this.modalService.open(this.modalCreate, { 'size': 'lg', backdrop: 'static' })
  }


  onChangePage(currentPage: number, pageSize: number) {
    this.currentSearchParam.currentPage = currentPage;
    this.onSearch(this.currentSearchParam?.currentPage, this.currentSearchParam?.pageSize);
  }

  onSubmit() {
    this.shareService.requestSaveExam(this.formCreate.getRawValue()).subscribe({
      next: value => {
        this.onSearch(1, 10);
        this.modalService.dismissAll();
      }, error: error => {
        alert('Tạo thất bại')
      }
    })
  }
}
export class ExamModel {
  description:string;
  duration:number;
  id:number;
}
export class TypesModel {
  description: string;
  id: number;
  quantity: number;
}