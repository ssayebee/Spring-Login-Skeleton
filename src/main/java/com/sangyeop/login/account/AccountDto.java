package com.sangyeop.login.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일이 없습니다.")
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
