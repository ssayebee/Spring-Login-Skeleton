package com.sangyeop.login.account;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String main() {
        return "main";
    }

    @GetMapping("/auth")
    public String auth() {
        return "auth";
    }

    @GetMapping("/sign-in")
    public String getSignIn() {
        return "account/sign-in";
    }

    @GetMapping("/sign-up")
    public String getSignUp(Model model) {
        model.addAttribute("accountDto", new AccountDto());
        return "account/sign-up"; }

    @PostMapping("/sign-up")
    public String postSignUp(Model model, @Valid AccountDto accountDto, Errors errors) {
        StringBuilder sb = new StringBuilder();
        if (errors.hasErrors()) {
            for (ObjectError err: errors.getAllErrors()) {
                sb.append(err.getDefaultMessage()).append("\n");
            }
            model.addAttribute("error", sb);
            return "account/sign-up";
        }
        accountDto.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        Account account = accountDto.toEntity();
        accountRepository.save(account);
        return "account/sign-in";
    }
}
