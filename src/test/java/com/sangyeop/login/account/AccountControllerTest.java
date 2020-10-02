package com.sangyeop.login.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("계정 테스트")
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objMapper;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @DisplayName("로그인 페이지 테스트")
    @Test
    public void getSignIn() throws Exception {
        mockMvc.perform(get("/sign-in"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-in"));
    }

    @DisplayName("회원가입 페이지 테스트")
    @Test
    public void getSignUp() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"));
    }

    @DisplayName("회원가입 테스트")
    @Test
    public void postSignUp() throws Exception {
        String EMAIL = "test@test.email";
        String PASSWORD = "password";

        mockMvc.perform(post("/sign-up")
                .param("email", EMAIL)
                .param("password", PASSWORD)
                .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isOk());

        String savedPassword = accountRepository.findByEmail(EMAIL).get().getPassword();
        Assertions.assertThat(PASSWORD).isNotEqualTo(savedPassword);

    }

    @DisplayName("로그인 실패 테스트")
    @Test
    public void postSignInFail() throws Exception {

        String EMAIL = "user@email.com";
        String PASSWORD = "password";

        accountRepository.save(Account.builder().email(EMAIL).password(passwordEncoder.encode("wrongPassword")).build());

        mockMvc.perform(formLogin().loginProcessingUrl("/sign-in").user(EMAIL).password(PASSWORD))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sign-in?error"));
    }

    @DisplayName("로그인 성공 테스트")
    @Test
    public void postSignInSuccess() throws Exception {

        String EMAIL = "user@email.com";
        String PASSWORD = "password";

        accountRepository.save(Account.builder().email(EMAIL).password(passwordEncoder.encode(PASSWORD)).build());

        mockMvc.perform(formLogin().loginProcessingUrl("/sign-in").user(EMAIL).password(PASSWORD))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}