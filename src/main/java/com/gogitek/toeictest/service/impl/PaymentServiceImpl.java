package com.gogitek.toeictest.service.impl;

import com.gogitek.toeictest.config.exception.ErrorCode;
import com.gogitek.toeictest.config.exception.ToeicRuntimeException;
import com.gogitek.toeictest.config.pagination.OffsetPageRequest;
import com.gogitek.toeictest.config.pagination.PaginationPage;
import com.gogitek.toeictest.constant.Level;
import com.gogitek.toeictest.constant.PaymentStatus;
import com.gogitek.toeictest.constant.Period;
import com.gogitek.toeictest.controller.dto.response.PaymentResponse;
import com.gogitek.toeictest.entity.ExpiredTimePayment;
import com.gogitek.toeictest.entity.PaymentEntity;
import com.gogitek.toeictest.entity.UserEntity;
import com.gogitek.toeictest.mapper.PaymentMapper;
import com.gogitek.toeictest.repository.ExpiredTimePaymentRepository;
import com.gogitek.toeictest.repository.PaymentRepository;
import com.gogitek.toeictest.repository.UserRepository;
import com.gogitek.toeictest.security.SecurityUtils;
import com.gogitek.toeictest.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final ExpiredTimePaymentRepository expiredTimePaymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public void createPayment(Period period) {
        var requester = SecurityUtils.requester().orElseThrow(() -> new ToeicRuntimeException(ErrorCode.UNAUTHORIZED));
        var paymentEntity = buildPayment(requester.user(), period);
        paymentRepository.save(paymentEntity);
        buildExpiredDate(requester.getId(), period);
    }

    @Override
    public void approvePayment(Long paymentId) {
        var paymentEntity = paymentRepository
                .findById(paymentId)
                .orElseThrow(() -> new ToeicRuntimeException(ErrorCode.ID_NOT_FOUND));

        var approved = SecurityUtils.requester().orElseThrow(() -> new ToeicRuntimeException(ErrorCode.UNAUTHORIZED));
        var paymentUser = paymentEntity.getUserEntity();
        paymentEntity.setAdminEntity(approved.user());
        paymentRepository.save(paymentEntity);
        upgradeLevel(paymentUser);
    }

    @Override
    public void rejectPayment(Long paymentId) {
        var paymentEntity = paymentRepository
                .findById(paymentId)
                .orElseThrow(() -> new ToeicRuntimeException(ErrorCode.ID_NOT_FOUND));

        var rejected = SecurityUtils.requester().orElseThrow(() -> new ToeicRuntimeException(ErrorCode.UNAUTHORIZED));
        paymentEntity.setAdminEntity(rejected.user());
        paymentRepository.save(paymentEntity);
    }

    @Override
    public PaginationPage<PaymentResponse> retrieveListResponseAdmin(Integer offset, Integer limited, String userName) {
        var pageable = new OffsetPageRequest(offset, limited);
        var paymentEntity = paymentRepository.retrievePaymentEntityByUserPhoneNumber(userName, pageable);
        return new PaginationPage<PaymentResponse>()
                .setLimit(limited)
                .setOffset(offset)
                .setTotalRecords(paymentEntity.getTotalElements())
                .setRecords(paymentEntity
                        .getContent()
                        .stream()
                        .map(paymentMapper::entityToDto)
                        .toList());
    }

    @Override
    public PaginationPage<PaymentResponse> retrieveListResponseUser(Integer offset, Integer limited) {
        var requestor = SecurityUtils.requester().orElseThrow(() -> new ToeicRuntimeException(ErrorCode.UNAUTHORIZED));
        var user = requestor.user();
        var paymentEntityList = user.getPaymentUserEntities();
        var pageable = new OffsetPageRequest(offset, limited);
        var page = new PageImpl<>(paymentEntityList, pageable, paymentEntityList.size());
        return new PaginationPage<PaymentResponse>()
                .setRecords(page.getContent()
                        .stream()
                        .map(paymentMapper::entityToDto)
                        .toList())
                .setOffset(offset)
                .setLimit(limited)
                .setTotalRecords(paymentEntityList.size());
    }

    private PaymentEntity buildPayment(UserEntity requester, Period period) {

        var term = Period.getPeriodInt(period);
        var periodValue = getCostByPeriod(period);

        if (term != null) {
            periodValue = periodValue * term;
        }

        return PaymentEntity
                .builder()
                .userEntity(requester)
                .period(period)
                .cost(periodValue)
                .status(PaymentStatus.PENDING)
                .build();
    }

    private Double getCostByPeriod(Period period) {
        if (period == null) throw new ToeicRuntimeException(ErrorCode.VALIDATION_ERROR);
        switch (period) {
            case ONE_MONTH -> {
                return 60000D;
            }
            case THREE_MONTH -> {
                return 4000D;
            }
            case FOREVER -> {
                return 390000D;
            }
            default -> throw new ToeicRuntimeException(ErrorCode.VALIDATION_ERROR);
        }
    }

    @Transactional
    @Async
    protected void upgradeLevel(UserEntity user) {
        user.setLevel(Level.PREMIUM);
        userRepository.save(user);
    }

    @Async
    protected void buildExpiredDate(Long userId, Period period) {
        var term = Period.getPeriodInt(period);
        var now = LocalDateTime.now();
        if (term != null) {
            var expiredDate = now.plus(term, ChronoUnit.MONTHS);
            var expiredEntity = ExpiredTimePayment
                    .builder()
                    .expiredDate(expiredDate)
                    .userId(userId)
                    .build();
            expiredTimePaymentRepository.save(expiredEntity);
        }
    }
}
