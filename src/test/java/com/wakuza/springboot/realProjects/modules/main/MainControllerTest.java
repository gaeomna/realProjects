package com.wakuza.springboot.realProjects.modules.main;

import com.wakuza.springboot.realProjects.modules.account.AccountRepository;
import com.wakuza.springboot.realProjects.modules.account.AccountService;
import com.wakuza.springboot.realProjects.modules.account.SignUpForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired AccountService accountService;
    @Autowired AccountRepository accountRepository;

    @BeforeEach //테스트를 시작 전에 실행 .
    void beforeEach(){
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setNickname("gaeomna");
        signUpForm.setEmail("gaeomna@naver.com");
        signUpForm.setPassword("123123123");
        accountService.processNewAccount(signUpForm);
    }
    @AfterEach //테스트 실행 후 실행
    void afterEach(){
        accountRepository.deleteAll();
    }

    @DisplayName("이메일로 로그인 성공")
    @Test
    void login_with_email() throws Exception{
        mockMvc.perform(post("/login")
                .param("username","gaeomna@naver.com")
                .param("password","123123123")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("gaeomna"));
    }

    @DisplayName("닉네임로 로그인 성공")
    @Test
    void login_with_nickname() throws Exception{
        mockMvc.perform(post("/login")
                .param("username","gaeomna")
                .param("password","123123123")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("gaeomna"));
    }

    @DisplayName("로그인 실패")
    @Test
    void login_fail() throws Exception{
        mockMvc.perform(post("/login")
                .param("username","blackship")
                .param("password","111222")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }
    @DisplayName("로그아웃")
    @Test
    void logout() throws Exception{
        mockMvc.perform(post("/logout")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(unauthenticated());
    }

}