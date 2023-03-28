package com.gogitek.toeictest.service.impl;

import com.gogitek.toeictest.config.exception.ErrorCode;
import com.gogitek.toeictest.config.exception.ToeicRuntimeException;
import com.gogitek.toeictest.config.pagination.OffsetPageRequest;
import com.gogitek.toeictest.config.pagination.PaginationPage;
import com.gogitek.toeictest.constant.Level;
import com.gogitek.toeictest.constant.PaymentStatus;
import com.gogitek.toeictest.constant.Period;
import com.gogitek.toeictest.controller.dto.response.PaymentResponse;
import com.gogitek.toeictest.controller.dto.response.UserAdminResponse;
import com.gogitek.toeictest.controller.dto.response.UserProfiles;
import com.gogitek.toeictest.entity.ExpiredTimePayment;
import com.gogitek.toeictest.entity.PaymentEntity;
import com.gogitek.toeictest.entity.UserEntity;
import com.gogitek.toeictest.mapper.PaymentMapper;
import com.gogitek.toeictest.mapper.UserMapper;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final ExpiredTimePaymentRepository expiredTimePaymentRepository;
    private final PaymentMapper paymentMapper;
    private final UserMapper userMapper;

    @Override
    public void createPayment(Period period) {
        var requester = SecurityUtils.requester().orElseThrow(() -> new ToeicRuntimeException(ErrorCode.UNAUTHORIZED));
        var paymentEntity = buildPayment(requester.user(), period);
        paymentRepository.save(paymentEntity);
        approvePayment(paymentEntity.getId());
        buildExpiredDate(requester.getId(), period);
    }

    @Override
    @Async
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

    @Override
    public void recharge(Double value) {
        if(value == null || value < 0){
            throw new ToeicRuntimeException(ErrorCode.VALIDATION_ERROR);
        }
        var requestor = SecurityUtils.requester()
                .orElseThrow(() -> new ToeicRuntimeException(ErrorCode.UNAUTHORIZED));
        var userEntity = requestor.user();
        var remainingMoney = userEntity.getRemainingMoney();
        if(remainingMoney == null) {
            remainingMoney = new BigDecimal(0);
        }
        remainingMoney = remainingMoney.add(new BigDecimal(value));
        userEntity.setRemainingMoney(remainingMoney);
        userRepository.save(userEntity);
    }

    @Override
    public UserProfiles retrieveCurrentUserInformation() {
        var requestor = SecurityUtils.requester().orElseThrow(() -> new ToeicRuntimeException(ErrorCode.UNAUTHORIZED));
        var user = requestor.user();
        return userMapper.entityToResponse(user);
    }

    private PaymentEntity buildPayment(UserEntity requester, Period period) {

        var periodValue = getCostByPeriod(period);
        if(periodValue.compareTo(requester.getRemainingMoney().doubleValue()) < 0){
            throw new ToeicRuntimeException(ErrorCode.NOT_ENOUGH_MONEY);
        }

        subtractMoney(requester, periodValue);

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
            case SIX_MONTH -> {
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
        var expiredDate = now.plus(term, ChronoUnit.MONTHS);
        var expiredEntity = ExpiredTimePayment
                .builder()
                .expiredDate(expiredDate)
                .userId(userId)
                .build();
        expiredTimePaymentRepository.save(expiredEntity);
    }

    @Async
    protected void subtractMoney(UserEntity user, Double money){
        var remaining = user.getRemainingMoney();
        if(remaining.doubleValue() < money) {
            throw new ToeicRuntimeException(ErrorCode.NOT_ENOUGH_MONEY);
        }
        var res = remaining.doubleValue() - money;
        var resInDecimal = new BigDecimal(res);
        user.setRemainingMoney(resInDecimal);
        userRepository.save(user);
    }
}
