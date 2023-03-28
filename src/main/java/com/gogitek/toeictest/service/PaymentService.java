package com.gogitek.toeictest.service;

import com.gogitek.toeictest.config.pagination.PaginationPage;
import com.gogitek.toeictest.constant.Period;
import com.gogitek.toeictest.controller.dto.response.PaymentResponse;
import com.gogitek.toeictest.controller.dto.response.UserProfiles;

public interface PaymentService {
    void createPayment(Period period);
    void approvePayment(Long paymentId);
    void rejectPayment(Long paymentId);
    PaginationPage<PaymentResponse> retrieveListResponseAdmin(Integer page, Integer size, String userName);
    PaginationPage<PaymentResponse> retrieveListResponseUser(Integer page, Integer size);
    void recharge(Double value);
    UserProfiles retrieveCurrentUserInformation();
}
