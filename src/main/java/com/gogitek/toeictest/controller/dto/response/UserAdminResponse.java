package com.gogitek.toeictest.controller.dto.response;

import com.gogitek.toeictest.constant.Level;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAdminResponse {
    private Long id;
    private String fullName;
    private Level level;
    private String phoneNumber;
}
