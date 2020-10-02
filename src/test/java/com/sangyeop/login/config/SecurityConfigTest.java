package com.sangyeop.login.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sangyeop.login.account.Account;
import com.sangyeop.login.account.AccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Spring Security 설정 테스트")
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objMapper;

    @Autowired
    AccountRepository accountRepository;

    @DisplayName("리소스 접근 권한 테스트")
    @Test
    public void getResource() throws Exception {
        mockMvc.perform(get("/css/base.css"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("CSRF 필터 테스트")
    @Test
    public void postSignUp() throws Exception {
        String EMAIL = "test@test.email";
        String PASSWORD = "password";

        mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objMapper.writeValueAsString(Account.builder().email(EMAIL).password(PASSWORD).build())))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

}