package com.wakuza.springboot.realProjects.modules.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor //private final 의 생성자를 만들어줌 private는 안 만들어줌
public class SignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        SignUpForm signUpForm = (SignUpForm)object;
        if(accountRepository.existsByEmail(signUpForm.getEmail())){
            errors.rejectValue("email","invalid.email",new Object[]{signUpForm.getEmail()},"이미 사용중인 EMAIL 입니다.");
        }

        if(accountRepository.existsByNickname(signUpForm.getNickname())){
            errors.rejectValue("nickname","invalid.nickname",new Object[]{signUpForm.getNickname()},"이미 사용중인 NICKNAME 입니다.");
        }
    }
}
