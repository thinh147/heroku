package com.gogitek.toeictest.controller.dto.response;

import com.gogitek.toeictest.constant.Gender;
import com.gogitek.toeictest.constant.Level;
import com.gogitek.toeictest.constant.Roles;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfiles {
    private String fullName;

    private String username;

    private Gender gender;

    private LocalDate dateOfBirth;

    private String address;

    private String phoneNumber;

    private Roles role;
    private Level level;
    private BigDecimal remainingMoney;
}
