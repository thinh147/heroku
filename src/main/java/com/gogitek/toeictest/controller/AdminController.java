package com.gogitek.toeictest.controller;

import com.gogitek.toeictest.config.response.ResponseEntityBuilder;
import com.gogitek.toeictest.controller.dto.ExamRequest;
import com.gogitek.toeictest.controller.dto.request.CreateQuestionRequest;
import com.gogitek.toeictest.controller.dto.request.QuestionFilter;
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
    public ResponseEntity<?> retrieveListPayment(@RequestParam(name = "offset", defaultValue = "0") Integer offset,
                                                 @RequestParam(name = "limit", defaultValue = "10") Integer limit,
                                                 @RequestParam(name = "phoneNumber", defaultValue = "") String phoneNumber) {
        return ResponseEntityBuilder
                .getBuilder()
                .setDetails(paymentService.retrieveListResponseAdmin(offset, limit, phoneNumber))
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

}
