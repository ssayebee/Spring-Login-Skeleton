package com.sangyeop.login.account;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class AccountDto {
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호가 없습니다.")
    private String password;

    public Account toEntity() {
        return Account.builder()
                .email(this.email)
                .password(this.password)
                .build();
    }
}
