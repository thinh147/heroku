package com.gogitek.toeictest.service.job;

import com.gogitek.toeictest.entity.ExpiredTimePayment;
import com.gogitek.toeictest.repository.ExpiredTimePaymentRepository;
import com.gogitek.toeictest.repository.UserRepository;
import com.gogitek.toeictest.security.custom.TokenAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@EnableScheduling
@Configuration
@AllArgsConstructor
public class DowngradeLevelScheduled {
    private final ExpiredTimePaymentRepository expiredTimePaymentRepository;
    private final UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    @Scheduled(cron = "0 0 0 * * *")
    public void downgradeLevel() {
        var now = LocalDateTime.now();
        var listExpired = expiredTimePaymentRepository.findByExpiredTime(now);
        if (listExpired == null || ObjectUtils.isEmpty(listExpired)) {
            return;
        }
        var userIds = listExpired.stream().map(ExpiredTimePayment::getUserId).toList();
        try {
            userRepository.updateUserLevel(userIds);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
