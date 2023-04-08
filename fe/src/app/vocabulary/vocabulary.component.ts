import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, NgForm, Validators } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ShareService } from '../share/share.service';

@Component({
  selector: 'app-vocabulary',
  templateUrl: './vocabulary.component.html',
  styleUrls: ['./vocabulary.component.scss']
})
export class VocabularyComponent {
  groups: GroupModel[] = [];
  formCreateGroup = new FormGroup({
    groupImage: new FormControl('', Validators.required),
    groupName: new FormControl('', Validators.required),
  });
  currentSearchParam = {
    currentPage: 1 as number,// mặc định phân trang 
    pageSize: 10 as number,// mặc định số lượng bản ghi 1 trang 
    numFound: null as number, // Số lượng bản ghi có trong db
  };

  items: ItemModel[] = [];
  formCreateItem = new FormGroup({
    audioPath: new FormControl(''),
    description: new FormControl(''),
    groupId: new FormControl('', Validators.required),
    pronunciation: new FormControl('', Validators.required),
    word: new FormControl('', Validators.required),
  })

  @ViewChild("formFilter") formFilter: NgForm;
  @ViewChild('modalCreateItem', { static: true }) modalCreateItem: ElementRef;
  @ViewChild('modalCreateGroup', { static: true }) modalCreateGroup: ElementRef;

  constructor(
    private shareService: ShareService,
    private modalService: NgbModal,
  ) {
  }

  ngOnInit(): void {
    this.onSearch(1, 10);
    this.shareService.searchGroup().subscribe({
      next: data => {
        this.groups = data.details;
      }, error: err => {
        console.log(err);
      }
    })
  }

  onSearch(currentPage: number, pageSize: number) {
    this.shareService.searchItem(this.formFilter?.value).subscribe({
      next: data => {
        this.items = data.details.records;
        this.currentSearchParam.numFound = data.details.totalRecords;
      }, error: err => {
        console.log(err);
      }
    })
  }

  openModalItems() {
    this.formCreateItem.reset();
    this.modalService.open(this.modalCreateItem, { 'size': 'lg', backdrop: 'static' })
  }

  openModalGroups() {
    this.formCreateGroup.reset();
    this.modalService.open(this.modalCreateGroup, { 'size': 'lg', backdrop: 'static' })
  }


  onChangePage(currentPage: number, pageSize: number) {
    this.currentSearchParam.currentPage = currentPage;
    this.onSearch(this.currentSearchParam?.currentPage, this.currentSearchParam?.pageSize);
  }

  onSubmitItem() {
    this.shareService.requestSaveItem(this.formCreateItem.getRawValue()).subscribe({
      next: value => {
        this.onSearch(1, 10);
        this.modalService.dismissAll();
      }, error: error => {
        alert('Tạo thất bại')
      }
    })
  }
  onSubmitGroup() {
    this.shareService.requestSaveGroup(this.formCreateGroup.getRawValue()).subscribe({
      next: value => {
        this.modalService.dismissAll();
      }, error: error => {
        alert('Tạo thất bại')
      }
    })
  }
}

export interface ItemModel {
  audioPath: string;
  description: string;
  id: number;
  pronunciation: string;
  word: string;
  group: GroupModel
}

export interface GroupModel {
  groupImage: string;
  groupName: string;
  id: number;
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