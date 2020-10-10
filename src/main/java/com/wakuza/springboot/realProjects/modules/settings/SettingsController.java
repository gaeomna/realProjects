package com.wakuza.springboot.realProjects.modules.settings;

import com.wakuza.springboot.realProjects.modules.account.Account;
import com.wakuza.springboot.realProjects.modules.account.AccountService;
import com.wakuza.springboot.realProjects.modules.account.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.hibernate.procedure.spi.ParameterRegistrationImplementor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import java.nio.channels.AcceptPendingException;

@Controller
@RequiredArgsConstructor
public class SettingsController {

    @InitBinder("passwordForm")
    public void initBinder(WebDataBinder webDateBinder){
        webDateBinder.addValidators(new PasswordFormValidator());
    }

    static final String SETTINGS_PROFILE_NAME = "settings/profile";
    static final String SETTINGS_PROFILE_URL = "/settings/profile";
    static final String SETTINGS_PASSWORD_NAME = "settings/password";
    static final String SETTINGS_PASSWORD_URL = "/settings/password";
    private final AccountService accountService;


    @GetMapping(SETTINGS_PROFILE_URL)
    public String profileUpdateForm(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(new Profile(account));

        return SETTINGS_PROFILE_NAME;
    }

    @PostMapping(SETTINGS_PROFILE_URL)
    public String updateProfile(@CurrentUser Account account, @Valid Profile profile,
                                Errors errors, Model model, RedirectAttributes attributes){
        if(errors.hasErrors()){
            model.addAttribute(account);
            return SETTINGS_PROFILE_NAME;
        }

        accountService.updateProfile(account, profile);
        attributes.addFlashAttribute("message","프로필을 수정했습니다.");
        return "redirect:" + SETTINGS_PROFILE_URL;
    }

    @GetMapping(SETTINGS_PASSWORD_URL)
    public String profilePasswordForm(@CurrentUser Account account, Model model){
        model.addAttribute("account");
        model.addAttribute(new PasswordForm());
        return SETTINGS_PASSWORD_NAME;
    }

    @PostMapping(SETTINGS_PASSWORD_URL)
    public String updatePassword(@CurrentUser Account account,@Valid PasswordForm passwordForm,
                                 Errors errors, Model model, RedirectAttributes attributes){
        if(errors.hasErrors()){
            model.addAttribute(account);
            return SETTINGS_PASSWORD_NAME;
        }
        
        accountService.updatePassword(account, passwordForm.getNewPassword());
        attributes.addFlashAttribute("message","패스워드를 수정했습니다.");
        return "redirect:" + SETTINGS_PASSWORD_NAME;
    }


}
