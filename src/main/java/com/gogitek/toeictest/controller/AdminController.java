package com.gogitek.toeictest.controller;

import com.gogitek.toeictest.config.response.ResponseEntityBuilder;
import com.gogitek.toeictest.controller.dto.request.ExamRequest;
import com.gogitek.toeictest.controller.dto.request.*;
import com.gogitek.toeictest.controller.dto.response.AdminQuestionResponse;
import com.gogitek.toeictest.controller.route.AdminRoute;
import com.gogitek.toeictest.controller.route.BaseRoute;
import com.gogitek.toeictest.service.AdminService;
import com.gogitek.toeictest.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(BaseRoute.BASE_ENDPOINT + BaseRoute.ADMIN_BASE)
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final PaymentService paymentService;
    @PostMapping(AdminRoute.CREATE_QUESTION)
    public ResponseEntity<?> createQuestion(@RequestBody List<CreateQuestionRequest> request){
        adminService.createQuestion(request);
        return ResponseEntityBuilder.getBuilder()
                .setDetails("Create Successfully!")
                .build();
    }

    @PostMapping(AdminRoute.CREATE_EXAM)
    public ResponseEntity<?> createExam(@RequestBody ExamRequest request) {
        adminService.createExam(request);
        return ResponseEntityBuilder.getBuilder()
                .setDetails("Create Successfully!")
                .build();
    }

    @GetMapping(AdminRoute.PAYMENT_LIST)
    public ResponseEntity<?> retrieveListPayment(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                 @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                 @RequestParam(name = "phoneNumber", defaultValue = "") String phoneNumber) {
        return ResponseEntityBuilder
                .getBuilder()
                .setDetails(paymentService.retrieveListResponseAdmin(page, size, phoneNumber))
                .build();
    }

    @PostMapping(AdminRoute.APPROVED)
    public ResponseEntity<?> approvedPayment(@PathVariable Long id) {
        paymentService.approvePayment(id);
        return ResponseEntityBuilder
                .getBuilder()
                .setDetails("Approve Successfully!")
                .build();
    }

    @PostMapping(AdminRoute.REJECTED)
    public ResponseEntity<?> rejectedPayment(@PathVariable Long id) {
        paymentService.rejectPayment(id);
        return ResponseEntityBuilder
                .getBuilder()
                .setDetails("Approve Successfully!")
                .build();
    }

    @GetMapping(AdminRoute.QUESTION_LIST)
    public ResponseEntity<?> questionList(@Valid QuestionFilter filter) {
        return ResponseEntityBuilder
                .getBuilder()
                .setDetails(adminService.retrieveListQuestionAdmin(filter))
                .build();
    }

    @PostMapping(AdminRoute.EDIT_QUESTION)
    public ResponseEntity<?> editQuestion(@RequestBody AdminQuestionResponse request) {
        return ResponseEntityBuilder
                .getBuilder()
                .setDetails(adminService.modifyQuestion(request))
                .build();
    }

    @PostMapping(AdminRoute.CREATE_GROUP)
    public ResponseEntity<?> createGroupVocabulary(@RequestBody CreateVocabularyGroup request) {
        return ResponseEntityBuilder
                .getBuilder()
                .setDetails(adminService.createVocabularyGroup(request))
                .build();
    }

    @PostMapping(AdminRoute.CREATE_ITEM)
    public ResponseEntity<?> createItemVocabulary(@RequestBody CreateVocabularyRequest request) {
        return ResponseEntityBuilder
                .getBuilder()
                .setDetails(adminService.createVocabulary(request))
                .build();
    }

    @GetMapping(AdminRoute.LIST_GROUP)
    public ResponseEntity<?> retrieveGroupList() {
        return ResponseEntityBuilder
                .getBuilder()
                .setDetails(adminService.retrieveGroupVocabulary())
                .build();
    }

    @GetMapping(AdminRoute.LIST_ITEM)
    public ResponseEntity<?> createItemVocabulary(@Valid VocabItemAdminFilter filter) {
        return ResponseEntityBuilder
                .getBuilder()
                .setDetails(adminService.retrieveListItemVocab(filter))
                .build();
    }

    @GetMapping(AdminRoute.USER_LIST)
    public ResponseEntity<?> getListUSer(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                         @RequestParam(name = "size", defaultValue = "10") Integer size,
                                         @RequestParam(name = "name", defaultValue = "") String name){
        return ResponseEntityBuilder
                .getBuilder()
                .setDetails(adminService.retrieveUserForAdminPage(page, size, name))
                .build();
    }

    @GetMapping(AdminRoute.EXAM_LIST)
    public ResponseEntity<?> getListExams(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                          @RequestParam(name = "size", defaultValue = "10") Integer size){
        return ResponseEntityBuilder
                .getBuilder()
                .setDetails(adminService.retrieveExamsForAdminPage(page, size))
                .build();
    }

    @PostMapping(AdminRoute.UPLOAD_FILE)
    public ResponseEntity<?> uploadFile(@ModelAttribute UploadFileRequest request){
        adminService.upload(request);
        return ResponseEntityBuilder
                .getBuilder()
                .setDetails("Upload successfully!")
                .build();
    }
}
